package com.delivery.common.util;

import com.delivery.common.NullObject;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author finderlo
 * @date 17/04/2017
 */
public class Context {

    private Map<String, Object> attributes = new HashMap<>();


    public Object put(String attr, Object value) {
        return attributes.put(attr, value);
    }

    public void putAll(Map<String, Object> attrs) {
        attributes.putAll(attrs);
    }

    @NotNull
    public Object get(String attr) {
        return attributes.getOrDefault(attr, NullObject.NULL_OBJECT);
    }


    public Object getOrDefault(String key, Object defaultValue) {
        return attributes.getOrDefault(key, defaultValue);
    }

}
