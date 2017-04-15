package com.delivery.event;

/**
 * Created by finderlo on 15/04/2017.
 * 事件发布器
 */
public interface EventPublisher {
     void publish(Event event,EventContext context);
}
