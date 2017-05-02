package com.delivery.common;

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
    DISPATCHER_AUTO_LOGIN_CHECK_ERROR(10002, "token不存在"),

    USER_TOKEN_NOEXIST(90001, "Token不存在"),
    USER_TOKEN_EXPIRY(90002, "Token已过期"),
    USER_ID_ISNULL(90003, "没有用户账号"),
    USER_INCORRECT_ID_OR_PSD(90004, "用户名或者密码不正确"),
    USER_REGISTER_INCORRECT_INFO(90005, "注册信息不完全"),
    USER_UNKNOWN_TYPE(90006, "模块不支持这个操作"),
    USER_NOT_EXIST_FIND_ARRT(90007, "查询条件为空"),
    USER_NO_EXIST_PHONE(90008, "手机号没有输入"),
    USER_REGISTER_DEP_PHONE(90009, "手机号已经存在"),

    ORDER_CREATE_FAILED_MAX_ORDER_COUNT(20002, "达到最大未完成订单数量"),
    ORDER_CREATE_EXIST_COMPLAINING_ORDER(20003, "用户存在正在申诉的订单"),
    ORDER_ALEADY_ACCEPTED(20004, "订单已被接单了"),
    ORDER_UNKNOWN_CANCEL_TYPE(20005, "未知订单取消类型"),
    ORDER_CANCEL_CANNOT(20006, "此时无法取消订单"),
    ORDER_UNKNOWN_ACTION_TYPE(20007, "Order_module:未知action类型"),
    ORDER_CREATE_FAILED_UNKNOW_ERROR(20008, "订单创建未知错误"),
    ORDER_CREATE_DELIVERY_TIME_NOEXIST(20009, "订单创建送件时间不存在"),
    ORDER_CREATE_DELIVERY_ADDR_NOEXIST(20010, "送件地址不存在"),
    ORDER_CREATE_PICKTIME_NO_EXIST(20011, "取件时间不存在"),
    ORDER_CREATE_PICKUP_ADDR_NOEXIST(20012, "取件地址不存在"),
    ORDER_CREATE_EXPRESSNAME_NOEXIST(20013, "快递名称不存在"),
    ORDER_OPERATION_LOG_PUBLISH_ERROR(20014, "订单操作表创建异常", false),
    ORDER_UNABLE_DELIVERY(20015, "订单无法设置递送状态，前置状态不是已接收"),

    SYSTEM_LOWCREDIT(1, "信用值不足"),
    SYSTEM_PERSISTENT_INCORRECT_KEY(2, "持久化层查询键错误/编程错误"),
    SYSTEM_NULL_OBJECT(3, "为空的对象"),
    SYSTEM_UNKNOWN_ERROR(4, "未知错误"),
    SYSTEM_USER_NO_EXIST(5, "用户不存在"),
    SYSTEM_NO_ENOUGH_PERMISSION(6, "用户权限不足"),
    SYSTEM_TIME_FORMAT_ERROR(7, "传入时间参数格式不正确"),
    SYSTEM_ORDER_NO_EXIST(8, "订单不存在"),

    CREDIT_SAVE_RECORD_ERROR(80001, "信用记录保存失败"),
    CREDIT_CHANGE_VALUE_ERROR_TYPE(80002, "传入信用值类型不对，为数字或者数字的字符串"), CREDIT_UNKNOWN_ACTION_TYPE(80003, "未知Action类型");


    String description;
    int code;
    boolean returnError = true;

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    ErrorCode(int code, String description, boolean returnError) {
        this.code = code;
        this.description = description;
    }


    public boolean isReturnError() {
        return returnError;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
