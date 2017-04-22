package com.delivery.common.constant;

import com.delivery.common.entity.OrdersEntity;

/**
 * @author finderlo
 * @date 21/04/2017
 */
public class Constant {

    private static final OrdersEntity defaultOrdersEntity = new OrdersEntity();

    public static final OrdersEntity getDefaultOrdersEntity() {
        defaultOrdersEntity.setOrdersGrade("5");
        return defaultOrdersEntity;
    }

    public static final String EVENT_ORDER_CANCEL_TYPE = "event_order_cancel_type";
    public static final String EVENT_ORDER_CANCEL_TYPE_RECEIVER = "event_order_cancel_type_receiver";
    public static final String EVENT_ORDER_CANCEL_TYPE_REPLACEMENT = "event_order_cancel_type_replacement";


    public static final String USER_ID = "user_id";

    public static final String USER_TYPE = "user_type";


    public static final String USER_ENTITY = "user_entity_object";

    public static final String ORDER_ID = "order_id";
    public static final String ORDER_CANCEL_TYPE = "order_cancel_type";
    public static final String ORDER_CANCEL_TYPE_RECEIVER = "order_cancel_type_rec";
    public static final String ORDER_CANCEL_TYPE_REPLACEMENT = "order_cancel_type_rep";
    public static final String ORDER_CANCEL_REASON = "order_cancel_reason";
    public static final String ORDER_ATTR_GRADLE = "order_attr_gradle";
    public static final String ORDER_ATTR_STATE = "order_attr_state";
    public static final String ORDER_ATTR_COMPLETE_STATE = "order_attr__complete_state";

    public static final String ACTION_SUB_TYPE = "action_sub_type";

    public static final String MANUAL_UPGRADE = "action_sub_type_upgrade";
    public static final String MANUAL_DEGRADE = "action_sub_type_degrade";
    public static final String MANUAL_COMPLAIN = "action_sub_type_complain";
}
