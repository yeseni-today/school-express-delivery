package com.delivery.persistent;

import com.delivery.common.response.Response;
import com.delivery.common.action.ActionHandler;

import java.util.Map;

/**
 * Created by finderlo on 2017/4/7.
 */
public interface PersistentSystem extends ActionHandler {

    /**
     * 删除一个对象
     */
    Response delete(Object o);

    /**
     * 查找对象
     *
     * @param isLike 是否模糊查询
     */
    Response find(Map<String, String> attr, boolean isLike);

    /**
     * 更新一个对象
     */
    Response update(Object o);

    /**
     * 持久化一个对象
     */
    Response save(Object o);

}
