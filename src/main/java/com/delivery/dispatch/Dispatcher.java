package com.delivery.dispatch;

import com.delivery.common.action.Action;
import com.delivery.common.response.Response;

/**
 * Created by finderlo on 2017/4/7.
 */
public interface Dispatcher {

    Response execute(Action action);

}
