package com.delivery.event;

import java.util.List;

/**
 * @author finderlo
 * @date 17/04/2017
 */
public interface EventManager extends EventPublisher, EventRegister {
    void publish(Event event, EventContext context);
}
