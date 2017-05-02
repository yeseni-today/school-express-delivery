package com.delivery.order;

import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.dispatch.Dispatcher;

/**
 * 订单接收判别器
 * 判断代取人能否接收该订单
 * @author finderlo
 * @date 17/04/2017
 */
public interface OrderAccepter {
    boolean canAcceptOrder(UserEntity user, OrderEntity order, Dispatcher dispatcher);
}
