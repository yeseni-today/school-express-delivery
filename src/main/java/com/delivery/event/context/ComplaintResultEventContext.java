package com.delivery.event.context;

import com.delivery.common.entity.ComplaintEntity;
import com.delivery.common.entity.OrderEntity;
import com.delivery.event.EventContext;

/**
 * @author finderlo
 * @date 16/05/2017
 */
public class ComplaintResultEventContext extends EventContext {

    private final OrderEntity.OrderState orderState;
    private final int creditChangeValue;
    private final ComplaintEntity complaint;

    public ComplaintResultEventContext(ComplaintEntity complaint, int creditChangeValue, OrderEntity.OrderState state) {
        this.orderState = state;
        this.creditChangeValue = creditChangeValue;
        this.complaint = complaint;
    }

    public OrderEntity.OrderState getOrderState() {
        return orderState;
    }


    public int getCreditChangeValue() {
        return creditChangeValue;
    }


    public ComplaintEntity getComplaint() {
        return complaint;
    }

}
