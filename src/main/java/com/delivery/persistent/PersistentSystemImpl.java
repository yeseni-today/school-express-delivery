package com.delivery.persistent;

import com.delivery.common.response.Response;
import com.delivery.common.action.Action;
import com.delivery.common.action.ActionType;

import java.util.Map;

/**
 * Created by finderlo on 2017/4/7.
 * 持久层系统，用于和数据库连接的中间层，使用Hibernate框架
 */
public class PersistentSystemImpl implements PersistentSystem {

    @Override
    public boolean canHandleAction(Action action) {
        return action.getType().equals(ActionType.PERSISTENT);
    }

    @Override
    public Response execute(Action action) {
        return null;
        //todo
    }

    @Override
    public Response delete(Object o) {
        return null;        //todo

    }

    @Override
    public Response find(Map<String, String> attr, boolean isLike) {
        return null;        //todo

    }

    @Override
    public Response update(Object o) {
        return null;        //todo

    }

    @Override
    public Response save(Object o) {
        return null;        //todo

    }
}
