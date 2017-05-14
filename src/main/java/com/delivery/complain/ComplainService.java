package com.delivery.complain;

import com.delivery.common.Response;
import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.action.ActionType;
import com.delivery.event.EventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author finderlo
 * @date 11/05/2017
 */
@Component
public class ComplainService implements ActionHandler, EventPublisher {

    @Override
    public boolean canHandleAction(Action action) {
        return action.getType() == ActionType.COMPLAIN;
    }

    @Override
    public Response execute(Action action) {
        return null;
    }


}
