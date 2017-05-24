package com.delivery.rest.order.task;

import com.delivery.common.dao.OrderDao;
import com.delivery.common.entity.OrderEntity;
import com.delivery.common.util.Task;
import com.delivery.common.util.TimeUnit;
import com.delivery.common.util.Timer;

/**
 * 订单自动确认任务
 * 如果订单是待确认状态，则自动设为确认状态，并发布订单自动评价任务（12h后）
 *
 * @author finderlo
 * @date 21/04/2017
 */
public class OrderAutoComfirmTask extends Task {


    String orderId;

    OrderDao dao;

    Timer timer;


    public OrderAutoComfirmTask(String orderId,Timer timer,  OrderDao orderDao) {
        this.orderId = orderId;
        this.dao = orderDao;
        this.timer = timer;
    }

    @Override
    public void run() {
        super.run();
        OrderEntity orders = dao.findById(orderId);
        if (orders.getState().equals(OrderEntity.OrderState.TAKE_PARCEL_WAIT_DELIVERY)) {
            synchronized (orders) {
                if (orders.getState().equals(OrderEntity.OrderState.TAKE_PARCEL_WAIT_DELIVERY)) {
                    orders.setState(OrderEntity.OrderState.WAIT_COMMENT);
                    dao.update(orders);
                    timer.submit(new OrderAutoCommentTask(orderId, dao), 12, TimeUnit.HOURS);

                }
            }
        }
        orders = null;
        dao = null;
    }
}
