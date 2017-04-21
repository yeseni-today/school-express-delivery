package com.delivery.common.response;

/**
 * Created by finderlo on 2017/4/7.
 */
public enum ErrorCode {

    /**
     * 默认的成功信息.
     */
    DEFAULT_SUCCESS(200, "success"),
    /**
     * 默认的失败信息.
     */
    DEFAULT_ERROR(1, "error"),


    DISPATCHER_UNKNOWN_ACTION_TYPE(10001, "未知的Action类型"),


    USER_TOKEN_NOEXIST(90001, "Token不存在"),
    USER_TOKEN_EXPIRY(90002, "Token已过期"),
    USER_ID_ISNULL(90003, "没有用户账号"),
    USER_INCORRECT_ID_OR_PSD(90004, "用户名或者密码不正确"),
    USER_REGISTER_INCORRECT_INFO(90005, "注册信息不完全"),
    USER_UNKNOWN_TYPE(90006, "模块不支持这个操作"),
    USER_NOT_EXIST_FIND_ARRT(90007, "查询条件为空"),


    ORDER_CREATE_FAILED_MAX_ORDER_COUNT(20002, "达到最大未完成订单数量"),

    SYSTEM_LOWCREDIT(00002, "信用值不足"),
    SYSTEM_PERSISTENT_INCORRECT_KEY(00001, "持久化层查询键错误/编程错误"), ORDER_CREATE_EXIST_COMPLAINING_ORDER(20003, "用户存在正在申诉的订单"), ORDER_ALEADY_ACCEPTED(20004, "订单已被接单了");

    String description;
    int code;

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
