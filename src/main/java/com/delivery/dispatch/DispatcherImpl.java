package com.delivery.dispatch;

import com.delivery.common.SedException;
import com.delivery.common.entity.UsersEntity;
import com.delivery.common.response.ErrorCode;
import com.delivery.common.action.Action;
import com.delivery.common.response.Response;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.util.Task;
import com.delivery.common.util.Timer;
import com.delivery.event.EventManager;
import com.delivery.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.delivery.common.util.Util.*;
import static com.delivery.common.constant.Constant.*;

/**
 * Created by finderlo on 2017/4/7.
 *
 * @author finderlo
 */
@Component
public class DispatcherImpl implements Dispatcher {

    private final List<ActionHandler> handlers = new ArrayList<>();

    @Autowired
    private EventManager eventManager;

    @Autowired
    UserService userService;

    @Autowired
    private Timer timer;


    @Override
    public Response execute(Action action) {
        preExecute(action);
        for (ActionHandler handler : handlers) {
            if (handler.canHandleAction(action)) {
                return handler.execute(action);
            }
        }
        return Response.error(ErrorCode.DISPATCHER_UNKNOWN_ACTION_TYPE);
    }

    private void preExecute(Action action) {
        //如果存在token，则添加用户名和类型
        String token = getToken(action);
        if (!token.equals("")) {
            Response response = userService.checkLogin(action);
            if (isSuccess(response)) {
                UsersEntity user = (UsersEntity) response.getContent();
                user.setUserPassword("");
                action.put(USER_ENTITY, user);
                action.put(USER_ID, user.getUserId());
                action.put(USER_TYPE, user.getUserIdentity());
            } else {
                throw new SedException(ErrorCode.USER_TOKEN_NOEXIST);
            }
        }
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public Timer getTimer() {
        return timer;
    }

    @Override
    public void handTimerException(Throwable throwable, Task task) {

    }


}
