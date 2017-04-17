package com.delivery.event;

/**
 * Created by finderlo on 15/04/2017.
 * 事件注册器
 *
 * @author finderlo
 */
public interface EventRegister {
    void register(Event event, EventExecutor executor);
}
