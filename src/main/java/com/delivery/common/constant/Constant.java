package com.delivery.common.constant;

import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.UserEntity;

/**
 * @author finderlo
 * @date 21/04/2017
 */
public class Constant {

    public static final String AUTHORIZATION = "token";
    public static final String CURRENT_USER_ID = "current_user_id";
    public static final int ORDER_ACCEPT_UNCOMPLETED_COUNT_LIMIT = 10;
    public static final int USER_CAN_CREATE_ORDER_CREDIT_LIMIT = 60;
    public static final int USER_CREATE_ORDER_MAX_COUNT = 10;
    public static final String SYSTEM_MESSAGE_SENDER = "0";
    private static OrderEntity defaultOrderEntity;

    public static int TOKEN_EXPIRES_HOUR = 10;
    private static UserEntity defaultUserEntity;


}
