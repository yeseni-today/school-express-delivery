package com.delivery.common.response;

/**
 * Created by finderlo on 2017/4/7.
 */
public class Response {

    /**
     * 错误码
     */
    private int error_code;

    /**
     * 描述
     */
    private String message;

    /**
     * 返回信息
     */
    private Object content;

    /**
     * @author finderlo
     * @date 17/04/2017
     */
    private Response(ErrorCode errorCode) {
        this(errorCode, null);
    }

    private Response(ErrorCode errorCode, Object content) {
        this.error_code = errorCode.getCode();
        this.message = errorCode.getDescription();
        this.content = content;
    }


    /**
     * @param object
     * @return 返回一个承载对象的response.
     **/
    public static Response withObject(final Object object) {
        return new Response(ErrorCode.DEFAULT_SUCCESS, object);
    }

    /**
     * @return 返回一个默认成功的response.
     **/
    public static Response success() {
        return new Response(ErrorCode.DEFAULT_SUCCESS);
    }

    public static Response success(final Object object) {
        return new Response(ErrorCode.DEFAULT_SUCCESS, object);
    }

    /**
     * @return 返回一个默认错误的response.
     **/
    public static Response error() {
        return new Response(ErrorCode.DEFAULT_ERROR);
    }

    public static Response error(ErrorCode errorCode) {
        return new Response(errorCode);
    }


    /**
     * @return error_code
     */
    public int getError_code() {
        return error_code;
    }


    /**
     * @return message
     */
    public String getMessage() {
        return message;
    }


    /**
     * @return
     */
    public Object getContent() {
        return content;
    }


}
