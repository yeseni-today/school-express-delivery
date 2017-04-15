package com.delivery.common.action;

import com.delivery.common.response.Response;

/**
 * Created by finderlo on 2017/4/7.
 */
public interface ActionHandler {

    boolean canHandleAction(Action action);

    Response execute(Action action);

}
