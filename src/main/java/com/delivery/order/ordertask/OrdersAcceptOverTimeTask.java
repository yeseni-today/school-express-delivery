package com.delivery.order.ordertask;

import com.delivery.common.constant.Constant;
import com.delivery.common.dao.OrdersDao;
import com.delivery.common.entity.OrdersEntity;
import com.delivery.common.util.Task;
import com.delivery.dispatch.Dispatcher;
import com.delivery.event.Event;
import com.delivery.event.EventContext;
import com.delivery.order.OrderState;

/**
 * 任务，判断订单状态是否是已接单。
 * 是，设为取消状态，发布订单取消事件，记录信用值
 * 否，
 * @author finderlo
 * @date 21/04/2017
 */
public class OrdersAcceptOverTimeTask extends Task {

    String ordersId;

    OrdersDao dao;

    Dispatcher dispatcher;

    public OrdersAcceptOverTimeTask(String orderId, OrdersDao ordersDao, Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.ordersId = orderId;
        this.dao = ordersDao;
    }

    @Override
    public void run() {
        super.run();
        OrdersEntity orders = dao.findById(ordersId);
        if (orders.getOrdersState().equals(OrderState.ACCEPTED)) {
            synchronized (orders) {
                if (orders.getOrdersState().equals(OrderState.ACCEPTED)) {
                    orders.setOrdersState(OrderState.CANCELED);
                    dao.update(orders);
                    //todo 发布订单取消事件
                    dispatcher.getEventManager().publish(Event.OrderCancelEvent,
                            parseContext());
                }
            }
        }

        orders = null;
        dispatcher = null;
        dao = null;
    }

    /**
     * 发布事件的原因，因为代取人接单12小时之后没有作为
     * @author finderlo
     * @date 21/04/2017
     */
    EventContext parseContext(){
        EventContext context = new EventContext();
        context.put(Constant.ORDER_ID, ordersId);
        context.put(Constant.EVENT_ORDER_CANCEL_TYPE,Constant.EVENT_ORDER_CANCEL_TYPE_REPLACEMENT);
        return parseContext();
    }


}
