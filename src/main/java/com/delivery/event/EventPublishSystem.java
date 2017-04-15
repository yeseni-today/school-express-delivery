package com.delivery.event;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by finderlo on 15/04/2017.
 * 事件发布系统
 */
public class EventPublishSystem implements EventRegister, EventPublisher {

    private HashMap<Event, List<EventExecutor>> events = new HashMap<>();


    public EventPublishSystem() {
        initEvent();
    }

    private void initEvent() {
        for (Event event : Event.values()) {
            events.put(event, new LinkedList<>());
        }
    }

    /**
     * 注册一个事件
     *
     * @param event    事件
     * @param executor 事件发生时的执行器
     */
    @Override
    public void register(Event event, EventExecutor executor) {
        List<EventExecutor> executors = events.get(event);
        executors.add(executor);
    }

    /**
     * 发布事件，通知事件的订阅者
     *
     * @param event   事件
     * @param context 事件相关的上下文信息
     */
    @Override
    public void publish(Event event, EventContext context) {
        List<EventExecutor> executors = events.get(event);
        executors.stream().forEach(e -> e.execute(event, context));
    }
}
