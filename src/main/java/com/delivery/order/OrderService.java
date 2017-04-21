package com.delivery.order;

import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.action.ActionType;
import com.delivery.common.dao.OrdersDao;
import com.delivery.common.dao.OrdersLogDao;
import com.delivery.common.entity.OrdersEntity;
import com.delivery.common.entity.OrdersOperationLogEntity;
import com.delivery.common.entity.UsersEntity;
import com.delivery.common.response.Response;
import com.delivery.dispatch.Dispatcher;
import com.delivery.event.Event;
import com.delivery.event.EventContext;
import com.delivery.event.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.delivery.common.response.ErrorCode.*;
import static com.delivery.order.OrderUtil.*;
import static com.delivery.order.OrderUtil.pushlishOrdersLog;

/**
 * Created by finderlo on 17/04/2017.
 */
@Component
public class OrderService implements ActionHandler, EventPublisher {

    @Autowired
    Dispatcher dispatcher;

    @Autowired
    OrdersDao ordersDao;

    @Autowired
    OrdersLogDao ordersLogDao;

    @Autowired
    TimelineMatcher timelineMatcher;

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
        //todo
        return null;
    }

    /**
     * 能否创建一个订单:
     * 1,检测信用值大于60
     * 2,检查未完成订单数小于10
     * 3,不存在申诉中的订单
     *
     * @param action 订单信息结构体
     *               订单类型
     * @return success 能；error 失败信息
     * @author finderlo
     */
    public Response check_create(Action action) {
        //todo
        String userID = getUserID(action);

        if (OrderUtil.getCredit(action, dispatcher) < 60) return Response.error(SYSTEM_LOWCREDIT);
        if (getOrderCount(userID, 0) > 10) return Response.error(ORDER_CREATE_FAILED_MAX_ORDER_COUNT);
        int count = ordersDao.findByIdAndState(userID, OrderState.COMPLAINING).size();
        if (count != 0) return Response.error(ORDER_CREATE_EXIST_COMPLAINING_ORDER);

        return Response.success();
    }

    public Response create(Action action) {
        OrdersEntity order = getOrders(action, dispatcher);
        ordersDao.save(order);
        OrderUtil.pushlishOrdersLog(order.getOrdersId(), OrderState.WAIT_ACCEPT, ordersLogDao);
        //todo 创建定时任务1 到达取件时间 没有接单 取消订单 设置状态为取消
        //
        //todo 发布创建成功事件
        return Response.success();
    }

    /**
     * 发布一个订单
     * 判断对应订单是否支付
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public Response publish(Action action) {
//         发布订单发布事件
        return null;
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
        Map<String, String> attr = getAttr(action);
        List<OrdersEntity> orders = ordersDao.findBy(attr, true);
        return Response.success(orders);
    }

    /**
     * 查找当前用户当前订单
     *
     * @param action 查询条件，返回列表
     *               用户
     * @author finderlo
     * @date 17/04/2017
     */
    public Response findByUsers(Action action) {
        String userId = getUserID(action);
        int state = getState(action);
        return Response.success(getOyders(userId, state));
    }

    /**
     * 获取当前用户可以接收的订单
     * 订单的匹配规则
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public Response timeline(Action action) {
        if (getCredit(action, dispatcher) < 60) {
            return Response.error(SYSTEM_LOWCREDIT);
        }

        UsersEntity user = getUser(action, dispatcher);
        Map<String, String> attr = timelineMatcher.timelineCondition(user);
        List<OrdersEntity> orders = ordersDao.findBy(attr, false);
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
        //todo
        //todo 使用OrderAccepter来判断代取人能否接单

        //代取人ID
        String replacementId = getUserID(action);

        if (getOrderCount(replacementId, 0) > 10) return Response.error(ORDER_CREATE_FAILED_MAX_ORDER_COUNT);
        int count = ordersDao.findByIdAndState(replacementId, OrderState.COMPLAINING).size();
        if (count != 0) return Response.error(ORDER_CREATE_EXIST_COMPLAINING_ORDER);

        OrdersEntity orders = ordersDao.findById(getOrdersId(action));

        if (orders.getOrdersState().equals(OrderState.WAIT_ACCEPT)) {
            synchronized (orders) {
                if (orders.getOrdersState().equals(OrderState.WAIT_ACCEPT)) {
                    orders.setOrdersState(OrderState.ACCEPTED);
                    orders.setReplacementId(replacementId);
                    pushlishOrdersLog(orders.getOrdersId(), OrderState.ACCEPTED, ordersLogDao);
                } else return Response.error(ORDER_ALEADY_ACCEPTED);
            }
        } else return Response.error(ORDER_ALEADY_ACCEPTED);
        //TODO 发布订单接收事件
        //TODO 创建时间事件 达到取件时间12小时后，取消订单，设置订单状态为取消
        return Response.success();
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
        //todo
        OrdersEntity order = getOrders(action,dispatcher);
        order.setOrdersState(OrderState.TAKE_PARCEL_WAIT_DELIVERY);
        ordersDao.update(order);
        OrderUtil.pushlishOrdersLog(order.getOrdersId(),OrderState.TAKE_PARCEL_WAIT_DELIVERY,ordersLogDao);
        //todo 创建订单时间事件 到达预约时间24小时后  设置订单状态为已完成
        // 预约时间
        return Response.success();
    }

    /**
     * 更新订单状态
     *
     * @param action 订单ID
     *               状态信息
     * @return success;error
     * @author finderlo
     */
    public Response update(Action action) {
        //todo
        return null;
    }

    /**
     * @author lx
     * 收件人确认订单
     * @date 21/04/2017
     */
    public Response affirm(Action action){
         //todo 收件人确认
        //todo 创建时间事件 确认后12小时 设置订单状态为已完成 默认完美评价
        return null;
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
        //todo
        //todo 如果双方都已经评价，都设置完成状态，发布事件
        return null;
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
        //todo
        return null;
    }

    /**
     * 取消订单
     * 收件人取消：待支付、待接单状态之一，设为状态取消，待接单状态则退款
     * 代取人取消：已接单状态，设为状态待接单，通知收件人，记录代取人信用
     *
     * @param action 订单号，
     * @author finderlo
     * @date 17/04/2017
     */
    public Response cancel(Action action) {
        //todo

        //todo 发布订单取消事件
        return null;
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
}
