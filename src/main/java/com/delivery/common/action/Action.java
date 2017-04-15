package com.delivery.common.action;

import java.util.Map;

/**
 * Created by finderlo on 2017/4/7.
 */
public interface Action {


    ActionType getType();

    Map<String,Object> getAttributes();



}
