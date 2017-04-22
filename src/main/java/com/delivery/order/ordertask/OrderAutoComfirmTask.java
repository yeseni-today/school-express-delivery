package com.delivery.order.ordertask;

import com.delivery.common.dao.OrdersDao;
import com.delivery.common.entity.OrdersEntity;
import com.delivery.common.util.Task;
import com.delivery.common.util.TimeUnit;
import com.delivery.dispatch.Dispatcher;
import com.delivery.order.OrderState;

/**
 * 订单自动确认任务
 * 如果订单是待确认状态，则自动设为确认状态，并发布订单自动评价任务（12h后）
 *
 * @author finderlo
 * @date 21/04/2017
 */
public class OrderAutoComfirmTask extends Task {


    String orderId;

    OrdersDao dao;

    Dispatcher dispatcher;

    public OrderAutoComfirmTask(String orderId, Dispatcher dispatcher, OrdersDao ordersDao) {
        this.orderId = orderId;
        this.dao = ordersDao;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        super.run();
        OrdersEntity orders = dao.findById(orderId);
        if (orders.getOrdersState().equals(OrderState.TAKE_PARCEL_WAIT_DELIVERY)) {
            synchronized (orders) {
                if (orders.getOrdersState().equals(OrderState.TAKE_PARCEL_WAIT_DELIVERY)) {
                    orders.setOrdersState(OrderState.WAIT_COMMENT);
                    dao.update(orders);
                    dispatcher.getTimer().submit(new OrderAutoCommentTask(orderId, dao), 12, TimeUnit.HOURS);

                }
            }
        }
        orders = null;
        dao = null;
        dispatcher = null;
    }
}
