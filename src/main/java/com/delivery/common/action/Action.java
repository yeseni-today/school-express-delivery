package com.delivery.common.action;

import com.delivery.common.NullObject;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by finderlo on 2017/4/7.
 *
 * @author finderlo
 */
public class Action {

    private ActionType type;

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

    public void setType(ActionType type) {
        this.type = type;
    }

    public ActionType getType() {
        return type;
    }


    public Object getOrDefault(String key, Object defaultValue) {
        return attributes.getOrDefault(key, defaultValue);
    }

    /**
     *
     * 静态工厂方法，由HttpServletRequest中参数来封装Action
     * @author finderlo
     * @date 17/04/2017
     */
    public static Action valueOf(HttpServletRequest request) {
        Action action = new Action();
        request.getParameterMap().forEach((key, value) -> action.put(key, value.length == 0 ? null : value[0]));
        return action;
    }

    public String toString(){
        return new Gson().toJson(this);
    }
}
