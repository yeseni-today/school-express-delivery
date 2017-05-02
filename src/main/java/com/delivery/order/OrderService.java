package com.delivery.order;

import com.delivery.common.SedException;
import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.action.ActionType;
import com.delivery.common.constant.Constant;
import com.delivery.common.dao.OrderDao;
import com.delivery.common.dao.OrderLogDao;
import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.common.Response;
import com.delivery.common.util.TimeUnit;
import com.delivery.common.util.Util;
import com.delivery.dispatch.Dispatcher;
import com.delivery.event.Event;
import com.delivery.event.EventContext;
import com.delivery.event.EventPublisher;
import com.delivery.order.ordertask.OrderAutoComfirmTask;
import com.delivery.order.ordertask.OrderAutoCommentTask;
import com.delivery.order.ordertask.OrderOvertimeTask;
import com.delivery.order.ordertask.OrdersAcceptOverTimeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.delivery.common.constant.Constant.MANUAL_COMPLAIN;
import static com.delivery.common.ErrorCode.*;
import static com.delivery.common.constant.Constant.*;
import static com.delivery.common.util.Util.*;
import static com.delivery.order.OrderUtil.getOrderAndSave;

/**
 * Created by finderlo on 17/04/2017.
 */
@Component
public class OrderService implements ActionHandler, EventPublisher {

    Dispatcher dispatcher;

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderLogDao orderLogDao;

    TimelineMatcher timelineMatcher = new DefaultTimelineMatcher();


    /**
     * 能否执行Action
     *
     * @author finderlo
     */
    @Override
    public boolean canHandleAction(Action action) {
        return action.getType() == ActionType.ORDER;
    }

    /**
     * 执行Action
     *
     * @author finderlo
     */
    @Override
    public Response execute(Action action) {
        OrderActionType type = getActionSubType(action, OrderActionType.class);
        preExecute(action);
        switch (type) {
            case check_create:
                return check_create(action);
            case create:
                return create(action);
            case find:
                return find(action);
            case findUserOrder:
                return findUserOrder(action);
            case timeline:
                return timeline(action);
            case accept:
                return accept(action);
            case update:
                return update(action);
            case affirm:
                return affirm(action);
            case comment:
                return comment(action);
            case cancel:
                return cancel(action);
            case unknown:
                return Response.error(ORDER_UNKNOWN_ACTION_TYPE);
            case complain:
                return complain(action);
            case delivery:
                return delivery(action);
            default:
                Response.error(ORDER_UNKNOWN_ACTION_TYPE);
        }
        return Response.error(ORDER_UNKNOWN_ACTION_TYPE);

    }

    private void preExecute(Action action) {
        //订单号，则查找订单实体类
        String orderId = getOrdersId(action);
        if (!orderId.equals("")) {
            OrderEntity order = orderDao.findById(orderId);
            action.put(ORDER_ENTITY, order);
        }

    }


    /**
     * 能否创建一个订单:
     * 1,检测信用值大于60
     * 2,检查未完成订单数小于10
     * 3,不存在申诉中的订单
     *
     * @param action 订单类型
     * @return success 能；error 失败信息
     * @author finderlo
     */
    public Response check_create(Action action) {
        //todo
        UserEntity user = getUser(action);
        checkNull(user, SYSTEM_USER_NO_EXIST);

        if (getCredit(action, dispatcher) < 60) return Response.error(SYSTEM_LOWCREDIT);
        if (getOrderCountByReplace(user.getUserId(), 0, orderDao) > 10)
            return Response.error(ORDER_CREATE_FAILED_MAX_ORDER_COUNT);
        int count = orderDao.findByIdAndState(user.getUserId(), OrderState.COMPLAINING).size();
        if (count != 0) return Response.error(ORDER_CREATE_EXIST_COMPLAINING_ORDER);

        return Response.success();
    }


    public Response create(Action action) {
        OrderEntity order = getOrderAndSave(action, orderDao);

        try {

            //订单操作日志
            pushlishOrdersLog(order.getOrdersId(), OrderState.WAIT_ACCEPT, orderLogDao);
            //发布定时任务
            dispatcher.getTimer().submit(
                    new OrderOvertimeTask(order.getOrdersId(), orderDao),
                    12, TimeUnit.HOURS
            );

            //发布事件
            EventContext eventContext = new EventContext();
            eventContext.put(Constant.ORDER_ID, order.getOrdersId());
            publish(Event.OrderPublishedEvent, eventContext);

            return Response.success(order);
        } catch (SedException e) {
            //rollback
            orderDao.delete(order);
            return Response.error(e.getErrorCode());
        } catch (Exception e) {
            return Response.error(ORDER_CREATE_FAILED_UNKNOW_ERROR);
        }
    }


    /**
     * 查找订单
     *
     * @param action 查询条件，返回列表
     *               管理员
     * @author finderlo
     * @date 17/04/2017
     */
    public Response find(Action action) {
        checkAdmin(action);
        Map<String, String> attr = getAttr(action);
        List<OrderEntity> orders = orderDao.findBy(attr, true);
        return Response.success(orders);
    }

    /**
     * 查找当前用户当前订单
     *
     * @param action 用户
     *               状态订单状态
     *               0/1 0 = 未完成/已完成
     * @author finderlo
     * @date 17/04/2017
     */
    public Response findUserOrder(Action action) {
        UserEntity userId = getUser(action);
        checkNull(userId);
        int state = getOrderCompleteState(action);
        return Response.success(getOrdersByUser(userId.getUserId(), state, orderDao));
    }


    /**
     * 获取当前用户可以接收的订单
     * 订单的匹配规则
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public Response timeline(Action action) {
        System.out.println("timeline------------");
        if (getCredit(action, dispatcher) < 60) {
            return Response.error(SYSTEM_LOWCREDIT);
        }

        UserEntity user = getUser(action);
//        Map<String, String> attr = timelineMatcher.timelineCondition(user);
        List<OrderEntity> orders = orderDao.findByUserMatch(user);
        return Response.success(orders);
    }


    /**
     * 代取人接单
     * 进行校验，代取人能否接单，校验信息由代取人信息和订单信息判断
     *
     * @param action 订单ID
     *               代取人ID
     * @return success;error。
     */
    public Response accept(Action action) {

        //代取人ID
        UserEntity user = getUser(action);
        checkNull(user);
        String replacementId = user.getUserId();
        //判断代取人没完成的订单数量
        if (getOrderCountByReplace(replacementId, 0, orderDao) > 10)
            return Response.error(ORDER_CREATE_FAILED_MAX_ORDER_COUNT);
        int count = orderDao.findByIdAndState(replacementId, OrderState.COMPLAINING).size();
        if (count != 0) return Response.error(ORDER_CREATE_EXIST_COMPLAINING_ORDER);

        OrderEntity orders = orderDao.findById(getOrdersId(action));

        if (orders.getOrdersState().equals(OrderState.WAIT_ACCEPT))
            synchronized (orderDao) {
                if (orders.getOrdersState().equals(OrderState.WAIT_ACCEPT)) {
                    orders.setOrdersState(OrderState.ACCEPTED);
                    orders.setReplacementId(replacementId);
                    orderDao.update(orders);
                    pushlishOrdersLog(orders.getOrdersId(), OrderState.ACCEPTED, orderLogDao);
                } else return Response.error(ORDER_ALEADY_ACCEPTED);
            }
        else return Response.error(ORDER_ALEADY_ACCEPTED);
        //发布订单接收事件
        EventContext eventContext = new EventContext();
        eventContext.put(Constant.ORDER_ID, orders.getOrdersId());
        publish(Event.OrderAcceptedEvent, eventContext);
        //创建时间事件 达到取件时间12小时后，取消订单，设置订单状态为取消
        dispatcher.getTimer().submit(
                new OrdersAcceptOverTimeTask(orders.getOrdersId(), orderDao, dispatcher),
                12, TimeUnit.HOURS
        );

        return Response.success(orders);
    }

    /**
     * 代取人拿到快递，准备送到收件人
     * 成功时订单状态为待送达
     * 开始计时待送达时间（由订单预约送达时间），提醒代取人送达时间。
     * 对订单检查：订单状态为待接收
     * 定时任务：预约时间送达之后12小时自动设为订单为待评价
     *
     * @param action 订单号
     *               代取人TOKEN
     * @return fail:订单前置状态不是待接收；TOKEN不是代取人；
     * @author finderlo
     * @date 17/04/2017
     */
    public Response delivery(Action action) {
        OrderEntity order = getOrder(action);
        checkNull(order);
        if (!order.getOrdersState().equals(OrderState.ACCEPTED)) {
            return Response.error(ORDER_UNABLE_DELIVERY);
        }
        order.setOrdersState(OrderState.TAKE_PARCEL_WAIT_DELIVERY);
        orderDao.update(order);
        pushlishOrdersLog(order.getOrdersId(), OrderState.TAKE_PARCEL_WAIT_DELIVERY, orderLogDao);
        // 创建订单时间任务 到达预约时间24小时后  设置订单状态为已完成
        dispatcher.getTimer().submit(
                new OrderAutoComfirmTask(order.getOrdersId(), dispatcher, orderDao),
                24, TimeUnit.HOURS
        );

        return Response.success(order);
    }

    /**
     * 更新订单状态，只有网页端可以操作
     *
     * @param action 订单ID
     *               状态信息
     * @return success;error
     * @author finderlo
     * @see Constant#ORDER_ID 订单ID参数名称
     * @see Constant#ORDER_ATTR_STATE 订单状态参数名称，为订单状态枚举的英文值
     * @see OrderState 订单状态枚举类
     */
    public Response update(Action action) {
        checkAdmin(action);
        OrderEntity orders = getOrdersById(action, orderDao);
        OrderState state = getOrderState(action);
        orders.setOrdersState(state);
        return Response.success();
    }

    /**
     * @author lx
     * 收件人确认订单
     * @date 21/04/2017
     */
    public Response affirm(Action action) {
        OrderEntity order = getOrdersById(action, orderDao);
        checkNull(order);
        order.setOrdersState(OrderState.WAIT_COMMENT);
        orderDao.update(order);
        //发布操作日志
        pushlishOrdersLog(order.getOrdersId(), OrderState.WAIT_COMMENT, orderLogDao);
        // 创建时间事件 确认后12小时 设置订单状态为已完成 默认完美评价
        dispatcher.getTimer().submit(
                new OrderAutoCommentTask(order.getOrdersId(), orderDao),
                12, TimeUnit.HOURS
        );

        return Response.success();
    }

    /**
     * 评价订单，订单必须处于待评价状态
     * 如果双方都已经评价，则设置为完成，并发布订单完成事件
     *
     * @param action 订单ID
     *               评价内容
     * @author finderlo
     */
    public Response comment(Action action) {
        OrderEntity order = getOrdersById(action, orderDao);
        checkNull(order);
        order.setOrdersGrade(getOrdersGrade(action));
        order.setOrdersState(OrderState.COMPLETED);
        orderDao.update(order);
        //发布操作日志
        pushlishOrdersLog(order.getOrdersId(), OrderState.COMPLETED, orderLogDao);

        EventContext eventContext = new EventContext();
        eventContext.put(Constant.EVENT_ORDER_ENTITY,order);
        publish(Event.OrderCommentSuccessEvent,eventContext);
        publish(Event.OrderCompleteSuccessEvent, eventContext);
        return Response.success();
    }


    /**
     * 申诉订单，将其转移至人工处理
     *
     * @param action 订单号
     *               申诉类型
     *               描述
     *               用户ID
     * @return fail:订单状态不是待接单、已接单、待送达、代取人确认送达等状态之一；success：将其转移至人工处理
     * @author finderlo
     * @date 17/04/2017
     */
    public Response complain(Action action) {
        action.setType(ActionType.MANUAL);
        action.put(Constant.ACTION_SUB_TYPE, MANUAL_COMPLAIN);
        return dispatcher.execute(action);
    }

    /**
     * 取消订单
     * 收件人取消：待支付、待接单状态之一，设为状态取消.待接单状态则退款
     * 代取人取消：已接单状态，设为状态待接单，通知收件人，记录代取人信用
     *
     * @param action 订单号，
     * @author finderlo
     * @date 17/04/2017
     */
    public Response cancel(Action action) {
        String type = (String) action.getOrDefault(ORDER_CANCEL_TYPE, "");

        if (type.equals(ORDER_CANCEL_TYPE_RECEIVER)) {
            return receiverCancel(action);
        }

        if (type.equals(ORDER_CANCEL_TYPE_REPLACEMENT)) {
            return replacementCancel(action);
        }
        return Response.error(ORDER_UNKNOWN_CANCEL_TYPE);
    }

    public Response receiverCancel(Action action) {
        OrderEntity order = getOrdersById(action, orderDao);
        checkNull(order);
        if (order.getOrdersState().equals(OrderState.WAIT_PAY)) {
            order.setOrdersState(OrderState.CANCELED);
            orderDao.update(order);
            return Response.success();
        } else if (order.getOrdersState().equals(OrderState.WAIT_ACCEPT)) {
            order.setOrdersState(OrderState.CANCELED);
            orderDao.update(order);
            //执行退款
            return Response.success();
        }

        return Response.error(ORDER_CANCEL_CANNOT);

    }

    public Response replacementCancel(Action action) {
        OrderEntity order = getOrdersById(action, orderDao);
        checkNull(order);
        String replId = order.getReplacementId();
        if (order.getOrdersState().equals(OrderState.ACCEPTED)) {
            order.setOrdersState(OrderState.WAIT_ACCEPT);
            order.setReplacementId(" ");
            orderDao.update(order);
            //发布代件人订单取消事件
            EventContext eventContext = new EventContext();
            eventContext.put(Constant.EVENT_ORDER_CANCEL_TYPE_REPLACEMENT, replId);
            publish(Event.OrderReplacementCancelEvent, eventContext);
            //发布信息通知收件人
            Util.sendSysMsg("订单取消成功", replId, dispatcher);
            Util.sendSysMsg("你的订单被代取人取消", order.getRecipientId(), dispatcher);
            return Response.success();
        }
        return Response.success();
    }


    /**
     * 发布事件
     *
     * @author finderlo
     * @date 17/04/2017
     */
    private void publish(Event event, EventContext context) {
        dispatcher.getEventManager().publish(event, context);
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }


}
