package com.delivery.event;

/**
 * Created by finderlo on 15/04/2017.
 * 事件注册器
 */
public interface EventRegister {
    void register(Event event,EventExecutor executor);
}
