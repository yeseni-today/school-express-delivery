package com.delivery.order;


import com.delivery.common.SedException;
import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.action.ActionType;
import com.delivery.common.constant.Constant;
import com.delivery.common.dao.OrderDao;
import com.delivery.common.dao.OrderLogDao;
import com.delivery.common.entity.*;
import com.delivery.common.Response;
import com.delivery.common.util.TimeUnit;
import com.delivery.common.util.UserUtil;
import com.delivery.common.util.Util;
import com.delivery.dispatch.Dispatcher;
import com.delivery.event.*;
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
import static com.delivery.common.util.UserUtil.checkAdmin;
import static com.delivery.common.util.Util.*;
import static com.delivery.common.util.OrderUtil.*;

/**
 * Created by Ticknick Hou on 17/04/2017.
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
     * @author Ticknick Hou
     */
    @Override
    public boolean canHandleAction(Action action) {
        return action.getType() == ActionType.ORDER;
    }


    /**
     * 初始化信用体系相关的事件接收器
     *
     * @author finderlo
     * @date 02/05/2017
     */
    public void initEventListener() {
        EventManager manager = dispatcher.getEventManager();
        manager.register(Event.ManualHandleComplainResultEvent, manualHandleComplainResultEventEventExecutor());
    }
    /**
     * 执行Action
     *
     * @author Ticknick Hou
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
            case findOrderLog:
                return findOrderLog(action);
            case findOrderAndUser:
                return findOrderAndUser(action);
            default:
                Response.error(ORDER_UNKNOWN_ACTION_TYPE);
        }
        return Response.error(ORDER_UNKNOWN_ACTION_TYPE);

    }

    private void preExecute(Action action) {
        //订单号，则查找订单实体类
        String orderId = getOrderId(action);
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
     * @author Ticknick Hou
     */
    public Response check_create(Action action) {
        //todo
        UserEntity user = getUser(action);
        checkNull(user, SYSTEM_USER_NO_EXIST);

        if (getCredit(action, dispatcher) < 60) return Response.error(SYSTEM_LOWCREDIT);
        if (getOrderCountByReplace(user.getId(), 0, orderDao) > 10)
            return Response.error(ORDER_CREATE_FAILED_MAX_ORDER_COUNT);
        int count = orderDao.findByIdAndState(user.getId(), OrderState.COMPLAINING).size();
        if (count != 0) return Response.error(ORDER_CREATE_EXIST_COMPLAINING_ORDER);

        return Response.success();
    }


    public Response create(Action action) {
        OrderEntity order = getOrderAndSave(action, orderDao);

        try {

            //订单操作日志
            pushlishOrdersLog(order.getId(), OrderState.WAIT_ACCEPT, orderLogDao);
            //发布定时任务
            dispatcher.getTimer().submit(
                    new OrderOvertimeTask(order.getId(), orderDao),
                    12, TimeUnit.HOURS
            );

            //发布事件
            EventContext eventContext = new EventContext();
            eventContext.put(Constant.ORDER_ID, order.getId());
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
     * 查找订单,返回订单的信息
     *
     * @param action 查询条件，返回列表
     *               管理员
     * @author Ticknick Hou
     * @date 17/04/2017
     */
    public Response find(Action action) {
        checkAdmin(action);
        Map<String, String> attr = getAttr(action);
        List<OrderEntity> orders = orderDao.findBy(attr, true);
        return Response.success(orders);
    }


    /**
     * 通过ID查找订单,返回订单的信息，包括收件人和代取人的用户信息
     *
     * @param action 查询条件，返回列表
     *               管理员
     * @author Ticknick Hou
     * @date 17/04/2017
     */
    public Response findOrderAndUser(Action action) {
        OrderEntity order = getOrder(action);
        Map<String, Object> attr = Util.entityToMap(order);
        UserEntity replace = UserUtil.findUserById(order.getReplacementId());
        UserEntity recipient = UserUtil.findUserById(order.getRecipientId());
        attr.put("replacementName", replace.getName());
        attr.put("replacementPhone", replace.getPhone());
        attr.put("recipientName", recipient.getName());
        attr.put("recipientPhone", recipient.getPhone());
        return Response.success(attr);
    }


    /**
     * 查找订单详情的状态信息
     *
     * @author Ticknick Hou
     * @date 11/05/2017
     */
    public Response findOrderLog(Action action) {
        String orderId = getOrderId(action);
        List<OrderLogEntity> orderStates = orderLogDao.findByOrderId(orderId);
        return Response.success(orderStates);
    }

    /**
     * 查找当前用户当前订单
     *
     * @param action 用户
     *               状态订单状态
     *               0/1 0 = 未完成/已完成
     * @author Ticknick Hou
     * @date 17/04/2017
     */
    public Response findUserOrder(Action action) {
        UserEntity userId = getUser(action);
        checkNull(userId);
        int state = getOrderCompleteState(action);
        return Response.success(getOrdersByUser(userId.getId(), state, orderDao));
    }


    /**
     * 获取当前用户可以接收的订单
     * 订单的匹配规则
     *
     * @author Ticknick Hou
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
        String replacementId = user.getId();
        //判断代取人没完成的订单数量
        if (getOrderCountByReplace(replacementId, 0, orderDao) > 10)
            return Response.error(ORDER_CREATE_FAILED_MAX_ORDER_COUNT);
        int count = orderDao.findByIdAndState(replacementId, OrderState.COMPLAINING).size();
        if (count != 0) return Response.error(ORDER_CREATE_EXIST_COMPLAINING_ORDER);

        OrderEntity orders = orderDao.findById(getOrderId(action));

        if (orders.getState().equals(OrderState.WAIT_ACCEPT))
            synchronized (orderDao) {
                if (orders.getState().equals(OrderState.WAIT_ACCEPT)) {
                    orders.setState(OrderState.ACCEPTED);
                    orders.setReplacementId(replacementId);
                    orderDao.update(orders);
                    pushlishOrdersLog(orders.getId(), OrderState.ACCEPTED, orderLogDao);
                } else return Response.error(ORDER_ALEADY_ACCEPTED);
            }
        else return Response.error(ORDER_ALEADY_ACCEPTED);
        //发布订单接收事件
        EventContext eventContext = new EventContext();
        eventContext.put(Constant.ORDER_ID, orders.getId());
        publish(Event.OrderAcceptedEvent, eventContext);
        //创建时间事件 达到取件时间12小时后，取消订单，设置订单状态为取消
        dispatcher.getTimer().submit(
                new OrdersAcceptOverTimeTask(orders.getId(), orderDao, dispatcher),
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
     * @author Ticknick Hou
     * @date 17/04/2017
     */
    public Response delivery(Action action) {
        OrderEntity order = getOrder(action);
        checkNull(order);
        if (!order.getState().equals(OrderState.ACCEPTED)) {
            return Response.error(ORDER_UNABLE_DELIVERY);
        }
        order.setState(OrderState.TAKE_PARCEL_WAIT_DELIVERY);
        orderDao.update(order);
        pushlishOrdersLog(order.getId(), OrderState.TAKE_PARCEL_WAIT_DELIVERY, orderLogDao);
        // 创建订单时间任务 到达预约时间24小时后  设置订单状态为已完成
        dispatcher.getTimer().submit(
                new OrderAutoComfirmTask(order.getId(), dispatcher, orderDao),
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
     * @author Ticknick Hou
     * @see Constant#ORDER_ID 订单ID参数名称
     * @see Constant#ORDER_ATTR_STATE 订单状态参数名称，为订单状态枚举的英文值
     * @see OrderState 订单状态枚举类
     */
    public Response update(Action action) {
        checkAdmin(action);
        OrderEntity orders = getOrdersById(action, orderDao);
        OrderState state = getOrderState(action);
        orders.setState(state);
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
        order.setState(OrderState.WAIT_COMMENT);
        orderDao.update(order);
        //发布操作日志
        pushlishOrdersLog(order.getId(), OrderState.WAIT_COMMENT, orderLogDao);
        // 创建时间事件 确认后12小时 设置订单状态为已完成 默认完美评价
        dispatcher.getTimer().submit(
                new OrderAutoCommentTask(order.getId(), orderDao),
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
     * @author Ticknick Hou
     */
    public Response comment(Action action) {
        OrderEntity order = getOrdersById(action, orderDao);
        checkNull(order);
        order.setGrade(getOrdersGrade(action));
        order.setState(OrderState.COMPLETED);
        orderDao.update(order);
        //发布操作日志
        pushlishOrdersLog(order.getId(), OrderState.COMPLETED, orderLogDao);

        EventContext eventContext = new EventContext();
        eventContext.put(Constant.EVENT_ORDER_ENTITY, order);
        publish(Event.OrderCommentSuccessEvent, eventContext);
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
     * @author Ticknick Hou
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
     * @author Ticknick Hou
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
        if (order.getState().equals(OrderState.WAIT_PAY)) {
            order.setState(OrderState.CANCELED);
            orderDao.update(order);
            return Response.success();
        } else if (order.getState().equals(OrderState.WAIT_ACCEPT)) {
            order.setState(OrderState.CANCELED);
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
        if (order.getState().equals(OrderState.ACCEPTED)) {
            order.setState(OrderState.WAIT_ACCEPT);
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
     * 申诉处理结果，事件执行器，主要执行对用户双方发送信息
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    public EventExecutor manualHandleComplainResultEventEventExecutor() {
        EventExecutor executor = new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.ManualHandleComplainResultEvent) return;
                ComplaintEntity complaint = (ComplaintEntity) context.get(Constant.EVENT_COMPLAINT_ENTITY);
                int stateInt = (int) context.get(EVENT_COMPLAINT_RESULT_ORDER_STATE_INT);
                OrderState state = OrderState.values()[stateInt];
                OrderEntity order = complaint.getOrder();
                order.setState(state);
                orderDao.save(order);
                //订单操作日志
                pushlishOrdersLog(order.getId(), state, orderLogDao);
            }
        };
        return executor;
    }

    /**
     * 发布事件
     *
     * @author Ticknick Hou
     * @date 17/04/2017
     */
    private void publish(Event event, EventContext context) {
        dispatcher.getEventManager().publish(event, context);
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }


}
