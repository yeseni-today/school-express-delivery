package com.delivery.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by finderlo on 15/04/2017.
 * 事件相关的上下文属性等等
 */
public class EventContext {

    private  String type;

    private Map<String,Object> attributes = new HashMap<>();


    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public String getType() {
        return type;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void setType(String type) {
        this.type = type;
    }
}
