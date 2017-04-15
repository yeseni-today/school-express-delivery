package com.delivery.user;

import com.delivery.common.response.Response;
import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;

/**
 * Created by finderlo on 2017/4/7.
 */
public interface UserSystem extends ActionHandler {

    /**
     * 用户注册
     * @param action 注册时的信息
     */
    Response register(Action action);

    /**
     * 用户登陆，登陆成功会返回一个标识符
     */
    Response login(Action action);

    /**
     * 校验用户的正确性，判断是否登陆
     */
    Response checkLogin(Action action);

}
