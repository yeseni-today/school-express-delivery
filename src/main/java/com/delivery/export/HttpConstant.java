package com.delivery.export;

/**
 * @author finderlo
 * @date 17/04/2017
 */
public class HttpConstant {



    public static final String USER_REGISTER = "/user/register";

    public static final String USER_FIND = "/user/findByPhone";

    public static final String USER_LOGIN = "/user/login";

    public static final String USER_UPGRADE = "/user/upgrade";

    public static final String USER_DEGRADE = "/user/degrade";


    public static final String ORDER_CREATE_DEPOSIT = "/order/create/deposit";

    public static final String ORDER_CHECK_CREATE ="/order/check_create";

    public static final String ORDER_CREATE_NORMAL = "/order/create";

    public static final String ORDER_FIND = "/order/findByPhone";

    public static final String ORDER_FIND_ORDER_LOG = "/order/find_order_log.json";

    public static final String ORDER_FIND_BY_USER_COMPLETE = "/order/find_by_user_complete";

  public static final String  ORDER_FIND_BY_USER_UNCOMPLETE =
          "/order/find_by_user_uncomplete";

    public static final String ORDER_TIMELINE = "/order/timeline";

    public static final String ORDER_ACCEPT = "/order/accept";

    public static final String ORDER_DELIVERY = "/order/delivery";

    public static final String ORDER_UPDATE = "/order/update";

    public static final String ORDER_AFFITM = "/order/confirm";

    public static final String ORDER_COMMENT = "/order/comment";

    public static final String ORDER_COMPLAIN = "/order/complain";

    public static final String ORDER_CANCEL = "/order/cancel";
    public static final String ORDER_FIND_ORDER_AND_USER = "/order/find_order_user";

    public static final String FUNDS_PAY = "/funds/pay ";

    public static final String FUNDS_WITHDRAW = "/funds/withdraw";


    public static final String MESSAGE_TIMELINE = "/message/timeline";

    public static final String MANUAL_HANDLE_USER_UPGRADE = "/manage/handle_user_upgrade";

    public static final String MANUAL_GET_REVIEW_LIST = "/manage/get_review_list";
    public static final String MANUAL_GET_REVIEW_BY_USER = "/manage/get_review_by_userid";
}
