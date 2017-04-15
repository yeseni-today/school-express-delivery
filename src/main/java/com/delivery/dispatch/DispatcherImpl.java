package com.delivery.dispatch;

import com.delivery.common.response.ErrorCode;
import com.delivery.common.action.Action;
import com.delivery.common.response.Response;
import com.delivery.common.action.ActionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by finderlo on 2017/4/7.
 */
public class DispatcherImpl implements Dispatcher {

    private final List<ActionHandler> handlers = new ArrayList<>();

    @Override
    public Response execute(Action action) {
        for (ActionHandler handler : handlers) {
            if (handler.canHandleAction(action)){
                return handler.execute(action);
            }
        }
        return Response.error(ErrorCode.CANNOT_HANDLE_ACTION);
    }

}
