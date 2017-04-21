package com.delivery.credit;

import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.response.Response;
import com.delivery.event.EventPublisher;

/**
 * @author finderlo
 * @date 20/04/2017
 */
public class CreditSystem implements EventPublisher,ActionHandler{


    @Override
    public boolean canHandleAction(Action action) {
        return false;
        //todo
    }

    @Override
    public Response execute(Action action) {
        return null;
        //todo
    }
}
