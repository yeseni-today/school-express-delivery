package com.delivery.event.context;

import com.delivery.common.entity.OrderEntity;
import com.delivery.event.EventContext;

/**
 * @author finderlo
 * @date 16/05/2017
 */
public class OrderEventContext extends EventContext {
    OrderEntity order;

    public OrderEventContext(OrderEntity order){
        this.order = order;
    }


    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }
}
