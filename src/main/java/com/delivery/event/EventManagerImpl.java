package com.delivery.event;


import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by finderlo on 15/04/2017.
 * 事件发布系统
 *
 * @author finderlo
 */
@Component
public class EventManagerImpl implements EventManager {

    private HashMap<Event, Set<EventExecutor>> events = new HashMap<>();


    public EventManagerImpl() {
        initEvent();
    }

    private void initEvent() {
        for (Event event : Event.values()) {
            events.put(event, new LinkedHashSet<>());
        }
    }

    /**
     * 注册一个事件
     *
     * @param event    事件
     * @param executor 事件发生时的执行器
     * @author finderlo
     */
    @Override
    public void register(Event event, EventExecutor executor) {
        Set<EventExecutor> executors = events.get(event);
        executors.add(executor);
    }

    /**
     * 发布事件，通知事件的订阅者
     *
     * @param event   事件
     * @param context 事件相关的上下文信息
     * @author finderlo
     */
    @Override
    public void publish(Event event, EventContext context) {
        Set<EventExecutor> executors = events.get(event);
        for (EventExecutor executor : executors) {
            executor.execute(event,context);
        }
    }
}
