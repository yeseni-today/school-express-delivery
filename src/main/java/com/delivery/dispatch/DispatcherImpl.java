package com.delivery.dispatch;

import com.delivery.common.SedException;
import com.delivery.common.constant.Constant;
import com.delivery.common.entity.UserEntity;
import com.delivery.common.ErrorCode;
import com.delivery.common.action.Action;
import com.delivery.common.Response;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.util.Task;
import com.delivery.common.util.Timer;
import com.delivery.event.EventManager;
import com.delivery.order.OrderService;
import com.delivery.user.UserService;
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

    private EventManager eventManager;

    private UserService userService;

    private OrderService orderService;

    private Timer timer;


    public DispatcherImpl() {
    }


    @Override
    public Response execute(Action action) {
        try {
            preExecute(action);
            for (ActionHandler handler : handlers) {
                if (handler.canHandleAction(action)) {
                    return handler.execute(action);
                }
            }
        } catch (SedException e) {
            return Response.error(e.getErrorCode());
        } catch (Exception e) {
            return Response.error(ErrorCode.DEFAULT_ERROR);
        }
        return Response.error(ErrorCode.DISPATCHER_UNKNOWN_ACTION_TYPE);
    }

    private void preExecute(Action action) {
        //如果不需要登陆，设置不需要登陆状态m
        String bool = (String) action.getOrDefault(Constant.ACTION_NEED_LOGIN, "true");
        boolean needLogin = !bool.equals("false");
        //如果存在token，则添加用户名和类型
        if (!needLogin) {
            return;
        }
        String token = getToken(action);
        if (!token.equals("")) {
            Response response = userService.checkLogin(action);
            if (isSuccess(response)) {
                UserEntity user = (UserEntity) response.getContent();
                user.setPassword("");
                action.put(USER_ENTITY, user);
                action.put(USER_ID, user.getId());
                action.put(USER_TYPE, user.getIdentity());
            } else {
                throw new SedException(ErrorCode.DISPATCHER_AUTO_LOGIN_CHECK_ERROR);
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


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void setHandlers(List<ActionHandler> handlers) {
        this.handlers.addAll(handlers);
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
