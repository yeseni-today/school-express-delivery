package com.delivery.dispatch;

import com.delivery.common.response.ErrorCode;
import com.delivery.common.action.Action;
import com.delivery.common.response.Response;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.util.Task;
import com.delivery.common.util.Timer;
import com.delivery.event.EventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by finderlo on 2017/4/7.
 * @author finderlo
 */
@Component
public class DispatcherImpl implements Dispatcher {

    private final List<ActionHandler> handlers = new ArrayList<>();

    @Autowired
    private EventManager eventManager;

    @Autowired
    private Timer timer;


    @Override
    public Response execute(Action action) {
        for (ActionHandler handler : handlers) {
            if (handler.canHandleAction(action)){
                return handler.execute(action);
            }
        }
        return Response.error(ErrorCode.DISPATCHER_UNKNOWN_ACTION_TYPE);
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public Timer getTimer() {
        return timer;
    }

    @Override
    public void handTimerException(Task task) {

    }

}
