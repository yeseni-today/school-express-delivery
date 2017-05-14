package com.delivery.order.ordertask;

import com.delivery.common.constant.Constant;
import com.delivery.common.dao.OrderDao;
import com.delivery.common.entity.OrderEntity;
import com.delivery.common.util.Task;
import com.delivery.order.OrderState;

/**
 * 订单自动评价任务，用来检测订单是否为待评价状态，是的话则自动评价
 * @author finderlo
 * @date 21/04/2017
 */
public class OrderAutoCommentTask extends Task {


    String ordersId;

    OrderDao dao;

    public OrderAutoCommentTask(String orderId, OrderDao orderDao) {
        this.ordersId = orderId;
        this.dao = orderDao;
    }

    @Override
    public void run() {
        super.run();
        OrderEntity orders = dao.findById(ordersId);
        if (orders.getState().equals(OrderState.WAIT_COMMENT)) {
            synchronized (orders) {
                if (orders.getState().equals(OrderState.WAIT_COMMENT)) {
                    orders.setState(OrderState.COMPLETED);
                    orders.setGrade(Constant.getDefaultOrderEntity().getGrade());
                    dao.update(orders);
                }
            }
        }
        orders = null;
        dao = null;
    }

}
