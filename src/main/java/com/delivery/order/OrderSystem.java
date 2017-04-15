package com.delivery.order;

import com.delivery.common.response.Response;
import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;

/**
 * Created by finderlo on 2017/4/7.
 */
public interface OrderSystem extends ActionHandler {

    /**
     * 创建一个订单
     */
    Response create(Action action);


    Response accept(Action action);

    Response setSate(Action action);

    Response comment(Action action);
}
