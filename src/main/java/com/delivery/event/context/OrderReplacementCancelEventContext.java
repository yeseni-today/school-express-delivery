package com.delivery.event.context;

import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.event.EventContext;

/**
 * @author finderlo
 * @date 16/05/2017
 */
public class OrderReplacementCancelEventContext extends OrderEventContext {

    private final UserEntity replacement;

    public OrderReplacementCancelEventContext(OrderEntity order, UserEntity replacement) {
        super(order);
        this.replacement = replacement;
    }

    public UserEntity getReplacement() {
        return replacement;
    }
}
