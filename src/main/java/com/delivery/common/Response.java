package com.delivery.common;

import com.google.gson.Gson;

/**
 * Created by finderlo on 2017/4/7.
 */
public class Response {

    /**
     * 状态码
     */
    private int status;

    /**
     * 描述
     */
    private String message;

    /**
     * 返回信息
     */
    private Object data;

    /**
     * @author finderlo
     * @date 17/04/2017
     */
    public Response() {
        this(200, null);
    }

    public Response(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


    private Response(int status, Object data) {
        this.status = status;
        this.data = data;
    }

    /**
     * @author finderlo
     * @date 17/04/2017
     */
    private Response(ErrorCode errorCode) {
        this(errorCode, null);
    }


    private Response(ErrorCode errorCode, Object data) {
        this.status = errorCode.getCode();
        this.message = errorCode.getDescription();
        this.data = data;
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
    public static Response ok() {
        return new Response(ErrorCode.DEFAULT_SUCCESS);
    }

    public static Response ok(final Object object) {
        return new Response(ErrorCode.DEFAULT_SUCCESS, object);
    }


    /**
     * @return 返回一个默认错误的response.
     **/
    public static Response error(int status,String message) {
        return new Response(status,message,null);
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
     * @return status
     */
    public int getStatus() {
        return status;
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
    public Object getData() {
        return data;
    }

    public void setStatus(int code) {
        this.status = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
