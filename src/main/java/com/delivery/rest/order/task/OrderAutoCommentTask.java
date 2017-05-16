package com.delivery.rest.order.task;

import com.delivery.common.constant.Constant;
import com.delivery.common.dao.OrderDao;
import com.delivery.common.entity.OrderEntity;
import com.delivery.common.util.Task;

/**
 * 订单自动评价任务，用来检测订单是否为待评价状态，是的话则自动评价
 * @author finderlo
 * @date 21/04/2017
 */
public class OrderAutoCommentTask extends Task {


    private String orderId;

    private OrderDao orderDao;

    public OrderAutoCommentTask(String orderId, OrderDao orderDao) {
        this.orderId = orderId;
        this.orderDao = orderDao;
    }

    @Override
    public void run() {
        super.run();
        OrderEntity order = orderDao.findById(orderId);
        if (order.getState().equals(OrderEntity.OrderState.WAIT_COMMENT)) {
            synchronized (order) {
                if (order.getState().equals(OrderEntity.OrderState.WAIT_COMMENT)) {
                    order.setState(OrderEntity.OrderState.COMPLETED);
                    order.setGrade("5");
                    orderDao.update(order);
                }
            }
        }
        order = null;
        orderDao = null;
    }

}
