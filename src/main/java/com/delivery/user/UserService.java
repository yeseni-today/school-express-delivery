package com.delivery.user;

import com.delivery.common.action.ActionHandler;
import com.delivery.common.constant.Constant;
import com.delivery.common.dao.UsersDao;
import com.delivery.common.entity.UsersEntity;
import com.delivery.common.Response;
import com.delivery.common.action.Action;
import com.delivery.common.action.ActionType;
import com.delivery.dispatch.Dispatcher;
import com.delivery.event.Event;
import com.delivery.event.EventContext;
import com.delivery.event.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.delivery.common.ErrorCode.*;
import static com.delivery.user.UserUtil.*;
import static com.delivery.common.Response.*;
import static com.delivery.user.UserConstant.*;
import static com.delivery.common.util.Util.*;

/**
 * Created by finderlo on 2017/4/7.
 */
@Component
public class UserService implements ActionHandler, EventPublisher {

    /**
     * 调度器
     *
     * @author finderlo
     */
    Dispatcher dispatcher;

    @Autowired
    UsersDao usersDao;

    @Autowired
    TokenHandler tokenHandler;

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * 能否执行Action
     *
     * @author finderlo
     */
    @Override
    public boolean canHandleAction(Action action) {
        return action.getType().equals(ActionType.USER);
    }

    /**
     * 根据Action类型执行这个操作
     *
     * @author finderlo
     */
    @Override
    public Response execute(Action action) {
        UserActionType type = getType(action);
        switch (type) {
            case LOGIN:
                return login(action);
            case REGISTER:
                return register(action);
            case CHECK_LOGIN:
                return checkLogin(action);
            case FIND:
                return find(action);
            case DEGRADE:
                return degrade(action);
            case UPGRADE:
                return upgrade(action);
            default:
                return error(USER_UNKNOWN_TYPE);
        }
    }


    /**
     * 用户注册，Action中应该含有全面的用户信息,前端参数正确
     *
     * @param action
     * @return error 不完整的信息或者用户名ID重复；success 用户信息
     * @author finderlo
     * @see UserConstant#USER_ID
     * @see UserConstant#USER_PASSWORD
     */
    public Response register(Action action) {
        UsersEntity user = getUserAndSave(action,usersDao);
        publishRegisterSuccess(user);
        return success(user);
    }

    /**
     * 查找用户，返回一个或多个用户
     *
     * @param action 查询条件
     *               手机号
     *               返回多个对象List
     * @author finderlo
     * @date 17/04/2017
     * @see UserConstant#USER_ID
     */
    public Response find(Action action) {
        String phone = getUserPhone(action);
        if (phone.equals("")) return error(USER_NOT_EXIST_FIND_ARRT);

        UsersEntity users = usersDao.findByUserPhone(phone);
        Map<String, Object> res = new HashMap<>();
        res.put(USER_RES_USERS, users);
        return success(res);
    }

    /**
     * 发布注册成功事件
     *
     * @author finderlo
     * @date 17/04/2017
     */
    private void publishRegisterSuccess(UsersEntity user) {
        publish(Event.UserRegisterEvent, parseEventfromUsersEntity(user));
    }

    /**
     * 登陆用户，返回TOKEN值，或者是对应的错误
     *
     * @param action 用户名
     *               用户密码
     * @return response token、user
     * @author finderlo
     * @see UserConstant#USER_ID
     * @see UserConstant#USER_PASSWORD
     */
    public Response login(Action action) {
        //获取用户名密码
        String phone = getUserPhone(action);
        String userpsd = getUserPsd(action);
        //空检查
        if ("".equals(phone)) return error(USER_ID_ISNULL);
        //用户名密码检查
        UsersEntity user = usersDao.findByUserPhone(phone);
        if (user == null) return error(USER_NO_EXIST_PHONE);

        if (!validUser(phone, userpsd, user)) return error(USER_INCORRECT_ID_OR_PSD);
        //返回信息
        String token = tokenHandler.getTokenAndLogin(user);
        HashMap<String, Object> returnContent = new HashMap<>();
        user.setUserPassword(null);
        returnContent.put(USER_RES_USERS, user);
        returnContent.put(USER_TOKEN, token);
        return success(returnContent);
    }


    /**
     * 判断是否登陆，通过Action的UserConstant#USER_TOKEN判断
     *
     * @param action Token值
     * @author finderlo
     * @see UserConstant#USER_TOKEN
     */
    public Response checkLogin(Action action) {
        //判断Token是否存在
        String token = getToken(action);
        if ("".equals(token)) return Response.error(USER_TOKEN_NOEXIST);
        //判断Token是否正确
        if (!tokenHandler.isRight(token)) return Response.error(USER_TOKEN_EXPIRY);
        //正确则返回Response
        return Response.success(tokenHandler.getUser(token));
    }

    /**
     * 升级一个用户，转交至人工服务处理
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public Response upgrade(Action action) {
        action.setType(ActionType.MANUAL);
        action.put(Constant.ACTION_SUB_TYPE, Constant.MANUAL_UPGRADE);
        return dispatcher.execute(action);
    }

    /**
     * 降级一个用户，转交至人工服务处理
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public Response degrade(Action action) {
        action.setType(ActionType.MANUAL);
        action.put(Constant.ACTION_SUB_TYPE, Constant.MANUAL_DEGRADE);
        return dispatcher.execute(action);
    }


    /**
     * 发布事件
     *
     * @author finderlo
     * @date 17/04/2017
     */
    private void publish(Event event, EventContext context) {
        dispatcher.getEventManager().publish(event, context);
    }
}
