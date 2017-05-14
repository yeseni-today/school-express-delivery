package com.delivery.common.constant;

import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.UserEntity;

/**
 * @author finderlo
 * @date 21/04/2017
 */
public class Constant {

    private static OrderEntity defaultOrderEntity;

    public static String ACTION_NEED_LOGIN = "action_need_login";

    public static OrderEntity getDefaultOrderEntity() {
        if (defaultOrderEntity == null) {
            defaultOrderEntity = new OrderEntity();
            defaultOrderEntity.setGrade("5");
        }
        return defaultOrderEntity;
    }

    private static UserEntity defaultUserEntity;

    public static UserEntity getDefaultUserEntity() {
        if (defaultUserEntity == null) {
            defaultUserEntity = new UserEntity();
            defaultUserEntity.setIdentity(UserEntity.UserType.RECIPIENT);
            defaultUserEntity.setIdCard(null);

        }
        return defaultUserEntity;
    }


    public static final String USER_ID = "user_id";
    public static final String USER_TYPE = "user_type";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_ENTITY = "user_entity_object";

    public static final String ORDER_ENTITY = "order_entity_object";
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

    public static final String REVIEW_ID = "review_id";
    public static final String REVIEW_ENTITY = "review_entity_object";
    public static final String REVIEW_USER_ID = "review_user_id";

    public static final String COMPLAINT_ID = "complaint_id";
    public static final String COMPLAINT_ENTITY = "complaint_entity_object";
    public static final String COMPLAINT_RESULT = "complaint_result";
    public static final String COMPLAINT_RESULT_CREDIT = "complaint_result_credit";


    public static final String EVENT_ORDER_CANCEL_TYPE = "event_order_cancel_type";
    public static final String EVENT_ORDER_CANCEL_TYPE_RECEIVER = "event_order_cancel_type_receiver";
    public static final String EVENT_ORDER_CANCEL_TYPE_REPLACEMENT = "event_order_cancel_type_replacement";
    public static final String EVENT_USER_REGISTER_USERENTITY = "event_user_register_user_entity";
    public static final String EVENT_ORDER_ENTITY = ORDER_ENTITY;
    public static final String EVENT_REVIEW_ENTITY = "event_review";
    public static final String EVENT_COMPLAINT_ENTITY = "event_complaint";
    public static final String EVENT_COMPLAINT_RESULT_CREDIT_VALUE_INT = "event_complaint_result_credit";
    public static final String EVENT_COMPLAINT_RESULT_ORDER_STATE_INT = "event_complaint_result_order_state";
    public static final String CREDIT_CHANGE_VALUE = "credit_change_value";
    public static final String CREDIT_REMARK = "credit_remark";

}
