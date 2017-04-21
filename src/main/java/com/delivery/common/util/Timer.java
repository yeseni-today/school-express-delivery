package com.delivery.common.util;


/**
 * @author finderlo
 * @date 21/04/2017
 */
public interface Timer {


    void submit(Task task, int delay, TimeUnit unit);

}

