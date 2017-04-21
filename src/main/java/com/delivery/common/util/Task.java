package com.delivery.common.util;

import java.util.Map;

/**
 * @author finderlo
 * @date 21/04/2017
 */
public interface Task extends Runnable {
    String getType();
    Map<String,Object> getAttr();
}
