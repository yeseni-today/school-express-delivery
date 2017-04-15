package com.delivery.common.action;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by finderlo on 2017/4/7.
 * 将应用层传来的请求，封装为Action
 */
public class DefaultAction implements Action {

    private ActionType type;

    private Map<String,Object> attributes = new HashMap<>();


    @Override
    public ActionType getType() {
        return type;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    public void setType(ActionType type) {
        this.type = type;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
