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
    USER_UNKNOWN_TYPE(90006, "模块不支持这个操作");

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
