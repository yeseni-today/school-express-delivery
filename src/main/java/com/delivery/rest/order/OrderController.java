package com.delivery.rest.order;

import com.delivery.common.Response;
import com.delivery.common.constant.Constant;
import com.delivery.common.dao.OrderDao;
import com.delivery.common.dao.OrderLogDao;
import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.common.util.TimeUnit;
import com.delivery.common.util.Timer;
import com.delivery.common.util.Util;
import com.delivery.config.annotation.AdminAuthorization;
import com.delivery.config.annotation.Authorization;
import com.delivery.config.annotation.CurrentUser;
import com.delivery.config.annotation.EnumParam;
import com.delivery.event.Event;
import com.delivery.event.EventManager;
import com.delivery.event.context.OrderEventContext;
import com.delivery.event.context.OrderReplacementCancelEventContext;
import com.delivery.rest.order.task.OrderAutoComfirmTask;
import com.delivery.rest.order.task.OrderAutoCommentTask;
import com.delivery.rest.order.task.OrderOvertimeTask;
import com.delivery.rest.order.task.OrdersAcceptOverTimeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.delivery.common.util.Assert;

import java.sql.Timestamp;
import java.util.List;

import static com.delivery.common.ErrorCode.ORDER_CANCEL_CANNOT;
import static com.delivery.common.util.Util.saveOrderLog;
import static com.delivery.common.constant.HttpStatus.*;

/**
 * @author finderlo
 * @date 15/05/2017
 */
@RestController
@RequestMapping("/orders")
public class OrderController {


    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderLogDao logDao;

    @Autowired
    private OrderUtil orderUtil;

    @Autowired
    private EventManager eventManager;

    @Autowired
    private Timer timer;

    /**
     * 返回订单列表
     *
     * @author Ticknick Hou
     * @date 15/05/2017
     */
    @GetMapping("/token")
    @Authorization
    public Response find(
            @CurrentUser UserEntity user) {

        List<OrderEntity> orders = orderDao.findByRecipientId(user.getUid());
        orders.addAll(orderDao.findByReplacementId(user.getUid()));
        return Response.ok(orders);
    }

    /**
     * 创建一个订单:
     * 1,检测信用值大于60
     * 2,检查未完成订单数小于10
     * 3,不存在申诉中的订单
     * <p>
     * s timestamp in format <code>yyyy-[m]m-[d]d hh:mm:ss[.f...]</code>.
     *
     * @author Ticknick Hou
     */
    @PostMapping
    @Authorization
    public Response newOne(
            @CurrentUser UserEntity user,
            @RequestParam String express_name,
            @RequestParam String pickup_address,
            @RequestParam String delivery_address,
            @RequestParam Timestamp delivery_time,
            @RequestParam(required = false) String express_code,
            @RequestParam(required = false) String pickup_code,
            @RequestParam Timestamp pickup_time,
            @RequestParam(required = false) String remark,
            @RequestParam double price
    ) {
        Assert.isTrue(orderUtil.canCreate(user), "user can not create an order");
        OrderEntity order = new OrderEntity();
        order.setRecipientId(user.getUid());
        order.setRecipient(user);
        order.setExpressName(express_name);
        order.setPickupTime(pickup_time);
        order.setPickupAddress(pickup_address);
        order.setDeliveryAddress(delivery_address);
        order.setDeliveryTime(delivery_time);
        order.setExpressCode(express_code);
        order.setPickupCode(pickup_code);
        order.setRemark(remark);
        order.setPrice(price);

        order.setCreateTime(Util.now());
        order.setState(OrderEntity.OrderState.WAIT_ACCEPT);
        String id = orderDao.newId(OrderDao.IdType.ORDER);
        order.setId(id);
        orderDao.save(order);

        OrderEventContext context = new OrderEventContext(order);
        eventManager.publish(Event.OrderPublishedEvent,context);
        //订单操作日志
        saveOrderLog(order.getId(), OrderEntity.OrderState.WAIT_ACCEPT, logDao);
        //发布定时任务
        timer.submit(
                new OrderOvertimeTask(order.getId(), orderDao),
                12, TimeUnit.HOURS
        );
        return Response.ok(order);
    }

    @PutMapping("/{order_id}")
    @AdminAuthorization
    public Response modifyOrderState(
            @CurrentUser UserEntity user,
            @PathVariable String order_id,
            @EnumParam OrderEntity.OrderState state) {
        OrderEntity order = orderDao.findById(order_id);
        Assert.notNull(order, WRONG_AUGUMENT, "wrong order id, can't find the order");
        Assert.isTrue(order.getState().equals(OrderEntity.OrderState.COMPLAINING), "only complainting order can modify state");
        order.setState(state);
        orderDao.update(order);
        saveOrderLog(order_id, state, logDao);
        return Response.ok(order);
    }


    @GetMapping("/{order_id}/process")
    @Authorization
    public Response get_process(
            @CurrentUser UserEntity user,
            @PathVariable String order_id) {
        OrderEntity order = orderDao.findById(order_id);
        Assert.notNull(order, WRONG_AUGUMENT, "the wrong order id");
        if (user.getIdentity().isUser()) {
            Assert.isTrue(orderUtil.isParticipation(user, order), FORBBID, "user only get their own order");
        }
        return Response.ok(logDao.findByOrderId(order_id));
    }


    @PutMapping("/{order_id}/process")
    @Authorization
    public Response modify(
            @CurrentUser UserEntity user,
            @PathVariable String order_id,
            @EnumParam OrderEntity.OrderState state,
            @RequestParam(required = false) String grade) {
        OrderEntity order = orderDao.findById(order_id);
        Assert.notNull(order, 400, "wrong order id");
        Assert.isTrue(user.getUid().equals(order.getRecipientId())
                        || user.getUid().equals(order.getReplacementId()),
                "user only can modify your participate order");

        switch (state) {
            case ACCEPTED:
                return acceptOrder(user, order, state);
            case TAKE_PARCEL_WAIT_DELIVERY:
                return deliveryOrder(user, order, state);
            //评价订单
            case COMPLETED:
                commentOrder(user, order, grade);
                //确认订单，
            case WAIT_COMMENT:
                return affirmOrder(user, order);
            case CANCELED:
                return cancelOrder(user, order);
            case COMPLAINING:
                return Response.error(400, "complaining order please use complaints API");
            default:
                return Response.error(400, "");
        }
    }

    /**
     * 代取人拿到快递，准备送到收件人
     * 成功时订单状态为待送达
     * 开始计时待送达时间（由订单预约送达时间），提醒代取人送达时间。
     * 对订单检查：订单状态为待接收
     * 定时任务：预约时间送达之后12小时自动设为订单为待评价
     *
     * @author Ticknick Hou
     * @date 17/04/2017
     */
    private Response deliveryOrder(UserEntity user, OrderEntity order, OrderEntity.OrderState state) {
        Assert.isTrue(state.equals(OrderEntity.OrderState.TAKE_PARCEL_WAIT_DELIVERY), " wrong state ");
        Assert.isTrue(order.getState().equals(OrderEntity.OrderState.ACCEPTED), "状态更改的前置条件不满足");

        order.setState(OrderEntity.OrderState.TAKE_PARCEL_WAIT_DELIVERY);
        orderDao.update(order);
        saveOrderLog(order.getId(), OrderEntity.OrderState.TAKE_PARCEL_WAIT_DELIVERY, logDao);
        // 创建订单时间任务 到达预约时间24小时后  设置订单状态为已完成
        timer.submit(
                new OrderAutoComfirmTask(order.getId(), timer, orderDao),
                24, TimeUnit.HOURS
        );
        return Response.ok();
    }

    /**
     * 代取人接单
     * 进行校验，代取人能否接单，校验信息由代取人信息和订单信息判断
     *
     * @return success;error。
     */
    private Response acceptOrder(UserEntity user, OrderEntity order, OrderEntity.OrderState state) {
        //todo
        Assert.isTrue(user.getIdentity().equals(UserEntity.Identity.REPLACEMENT), "only replacement can accept an order");
        Assert.isTrue(order.getState().equals(OrderEntity.OrderState.WAIT_ACCEPT), "order is accepted");
        Assert.isTrue(orderDao.findByReplacementId(user.getUid()).size() < Constant.ORDER_ACCEPT_UNCOMPLETED_COUNT_LIMIT, "user can accept that their uncompleted order less than 10");
        Assert.isTrue(orderDao.findByIdAndState(user.getUid(), OrderEntity.OrderState.COMPLAINING).size() == 0, "if user have complaining orders, he can not accept an order");

        order.setState(OrderEntity.OrderState.ACCEPTED);
        order.setReplacementId(user.getUid());
        orderDao.update(order);
        saveOrderLog(order.getId(), OrderEntity.OrderState.ACCEPTED, logDao);
        //发布订单接收事件
        OrderEventContext context = new OrderEventContext(order);
        eventManager.publish(Event.OrderAcceptedEvent, context);
        //创建时间事件 达到取件时间12小时后，取消订单，设置订单状态为取消
        timer.submit(
                new OrdersAcceptOverTimeTask(order.getId(), orderDao, eventManager),
                12, TimeUnit.HOURS
        );
        return Response.ok();
    }


    /**
     * @author lx
     * 收件人确认订单
     * @serialData 21/04/2017
     */
    private Response affirmOrder(UserEntity user, OrderEntity order) {
        order.setState(OrderEntity.OrderState.WAIT_COMMENT);
        orderDao.update(order);

        //发布操作日志
        saveOrderLog(order.getId(), OrderEntity.OrderState.WAIT_COMMENT, logDao);
        // 创建时间事件 确认后12小时 设置订单状态为已完成 默认完美评价
        timer.submit(
                new OrderAutoCommentTask(order.getId(), orderDao),
                12, TimeUnit.HOURS
        );
        return Response.ok();
    }

    /**
     * 评价订单，订单必须处于待评价状态,发布订单完成事件
     *
     * @author Ticknick Hou
     */
    private Response commentOrder(UserEntity user, OrderEntity order, String grade) {
        order.setGrade(grade);
        order.setState(OrderEntity.OrderState.COMPLETED);
        orderDao.update(order);
        //发布操作日志
        saveOrderLog(order.getId(), OrderEntity.OrderState.COMPLETED, logDao);

        OrderEventContext context = new OrderEventContext(order);
        eventManager.publish(Event.OrderCommentSuccessEvent, context);
        eventManager.publish(Event.OrderCompleteSuccessEvent, context);
        return Response.ok();
    }

    /**
     * 取消订单
     * 收件人取消：待支付、待接单状态之一，设为状态取消.待接单状态则退款
     * 代取人取消：已接单状态，设为状态待接单，通知收件人，记录代取人信用
     *
     * @author Ticknick Hou
     * @date 17/04/2017
     */
    private Response cancelOrder(UserEntity user, OrderEntity order) {
        if (user.getUid().equals(order.getReplacementId())) {
            return replacementCancel(order);
        }
        if (user.getUid().equals(order.getRecipientId())) {
            return receiverCancel(order);
        }
        return Response.error();
    }

    private Response receiverCancel(OrderEntity order) {
        if (order.getState().equals(OrderEntity.OrderState.WAIT_PAY)) {
            order.setState(OrderEntity.OrderState.CANCELED);
            orderDao.update(order);
            return Response.ok();
        } else if (order.getState().equals(OrderEntity.OrderState.WAIT_ACCEPT)) {
            order.setState(OrderEntity.OrderState.CANCELED);
            orderDao.update(order);
            //todo 执行退款
            return Response.ok();
        }

        return Response.error(ORDER_CANCEL_CANNOT);

    }

    private Response replacementCancel(OrderEntity order) {
        if (order.getState().equals(OrderEntity.OrderState.ACCEPTED)) {
            order.setState(OrderEntity.OrderState.WAIT_ACCEPT);
            UserEntity replace = order.getRecipient();
            order.setReplacementId(null);
            orderDao.update(order);

            OrderReplacementCancelEventContext context = new OrderReplacementCancelEventContext(order, replace);
            eventManager.publish(Event.OrderReplacementCancelEvent, context);
            //todo 发布信息通知收件人
            //Util.sendSysMsg("订单取消成功", replId, dispatcher);
            //Util.sendSysMsg("你的订单被代取人取消", order.getRecipientId(), dispatcher);
            return Response.ok();
        }
        return Response.ok();
    }

//    /**
//     * 申诉处理结果，事件执行器，主要执行对用户双方发送信息
//     *
//     * @author Ticknick
//     * @date 02/05/2017
//     */
//    public EventExecutor handleComplaintResultEvent() {
//        EventExecutor executor = new EventExecutor() {
//            @Override
//            public void execute(Event event, EventContext context) {
//                if (event != Event.ComplaintResultEvent) return;
//                ComplaintEntity complaint = (ComplaintEntity) context.get(Constant.EVENT_COMPLAINT_ENTITY);
//                int stateInt = (int) context.get(EVENT_COMPLAINT_RESULT_ORDER_STATE_INT);
//                OrderState state = OrderState.values()[stateInt];
//                OrderEntity order = complaint.getOrder();
//                order.setState(state);
//                orderDao.save(order);
//                //订单操作日志
//                saveOrderLog(order.getId(), state, orderLogDao);
//            }
//        };
//        return executor;
//    }

}
