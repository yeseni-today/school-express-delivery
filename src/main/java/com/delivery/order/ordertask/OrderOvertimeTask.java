package com.delivery.order.ordertask;

import com.delivery.common.dao.OrdersDao;
import com.delivery.common.entity.OrdersEntity;
import com.delivery.common.util.Task;
import com.delivery.order.OrderState;

/**
 * 订单超时任务，用来检测订单是否为接单状态，不然设为取消状态
 * @author finderlo
 * @date 21/04/2017
 */
public class OrderOvertimeTask extends Task {

    String ordersId;

    OrdersDao dao;

    public OrderOvertimeTask(String orderId, OrdersDao ordersDao){
        this.ordersId = orderId;
        this.dao = ordersDao;
    }

    @Override
    public void run() {
        super.run();
        OrdersEntity orders = dao.findById(ordersId);
        if (orders.getOrdersState().equals(OrderState.WAIT_ACCEPT)){
            synchronized (orders){
                if (orders.getOrdersState().equals(OrderState.WAIT_ACCEPT)){
                    orders.setOrdersState(OrderState.CANCELED);
                    dao.update(orders);
                }
            }
        }
        orders = null;
    }
}
