package com.delivery.dispatch;

import com.delivery.common.action.Action;
import com.delivery.common.response.Response;
import com.delivery.event.EventManager;
import org.springframework.stereotype.Component;

/**
 * Created by finderlo on 2017/4/7.
 * @author finderlo
 */

public interface Dispatcher {

    /**
     * @author finderlo
     */
    Response execute(Action action);

    EventManager getEventManager();

}
