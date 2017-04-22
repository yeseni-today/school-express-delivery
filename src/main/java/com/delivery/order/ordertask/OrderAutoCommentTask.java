package com.delivery.order.ordertask;

import com.delivery.common.constant.Constant;
import com.delivery.common.dao.OrdersDao;
import com.delivery.common.entity.OrdersEntity;
import com.delivery.common.util.Task;
import com.delivery.order.OrderState;

/**
 * 订单自动评价任务，用来检测订单是否为待评价状态，是的话则自动评价
 * @author finderlo
 * @date 21/04/2017
 */
public class OrderAutoCommentTask extends Task {


    String ordersId;

    OrdersDao dao;

    public OrderAutoCommentTask(String orderId, OrdersDao ordersDao) {
        this.ordersId = orderId;
        this.dao = ordersDao;
    }

    @Override
    public void run() {
        super.run();
        OrdersEntity orders = dao.findById(ordersId);
        if (orders.getOrdersState().equals(OrderState.WAIT_COMMENT)) {
            synchronized (orders) {
                if (orders.getOrdersState().equals(OrderState.WAIT_COMMENT)) {
                    orders.setOrdersState(OrderState.COMPLETED);
                    orders.setOrdersGrade(Constant.getDefaultOrdersEntity().getOrdersGrade());
                    dao.update(orders);
                }
            }
        }
        orders = null;
        dao = null;
    }

}
