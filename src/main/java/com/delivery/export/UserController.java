package com.delivery.export;

import com.delivery.common.action.Action;
import com.delivery.common.action.ActionType;
import com.delivery.common.constant.Constant;
import com.delivery.common.Response;
import com.delivery.dispatch.Dispatcher;
import com.delivery.user.UserActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Map;

import static com.delivery.export.HttpConstant.*;
import static com.delivery.common.util.Util.*;
/**
 * @author finderlo
 * @date 17/04/2017
 */
@Controller
public class UserController {

    @Autowired
    Dispatcher dispatcher;

    @RequestMapping(value = "/")
    public String hello(HttpServletRequest request) {
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            System.out.println(entry.getKey() + " " + Arrays.toString(entry.getValue()));
        }
        //todo
        //todo 在Service中取出实体类，将传入参数转换为UserEntity，并进行检验。
        //TODO Service中保留一个默认用户的实体类，用来将optinal参数使用默认值
        return "hello";
    }



    /**
     * 用户注册
     *
     * @author finderlo
     * @date 17/04/2017
     * @see com.delivery.user.UserService#register(Action)
     */
    @RequestMapping(value = USER_REGISTER, method = RequestMethod.POST)
    @ResponseBody
    public Response register(HttpServletRequest request) {
        try {
            Action action = valueOfRequestAndType(request, UserActionType.REGISTER);
            //todo 在Service中取出实体类，将传入参数转换为UserEntity，并进行检验。
            //Service中保留一个默认用户的实体类，用来将optinal参数使用默认值
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }

    /**
     * @author finderlo
     * @date 17/04/2017
     * @see com.delivery.user.UserService#login(Action)
     */
    @RequestMapping(value = USER_LOGIN)
    @ResponseBody
    public Response login(HttpServletRequest request) {
        try {
            Action action = valueOfRequestAndType(request, UserActionType.LOGIN);
            Response response = dispatcher.execute(action);
            return response;
        }catch (Exception e){
            return handleException(e);
        }
    }

    /**
     * @author finderlo
     * @date 17/04/2017
     * @see com.delivery.user.UserService#find(Action)
     */
    @RequestMapping(value = USER_FIND, method = RequestMethod.GET)
    @ResponseBody
    public Response find(HttpServletRequest request) {
        Action action = valueOfRequestAndType(request, UserActionType.FIND);
        return dispatcher.execute(action);
    }

    /**
     * @author finderlo
     * @date 17/04/2017
     * @see com.delivery.user.UserService#upgrade(Action)
     */
    @RequestMapping(value = USER_UPGRADE, method = RequestMethod.POST)
    @ResponseBody
    public Response upgrade(HttpServletRequest request) {
        Action action = valueOfRequestAndType(request, UserActionType.UPGRADE);
        return dispatcher.execute(action);
    }

    /**
     * @author finderlo
     * @date 17/04/2017
     * @see com.delivery.user.UserService#degrade(Action)
     */
    @RequestMapping(value = USER_DEGRADE, method = RequestMethod.POST)
    @ResponseBody
    public Response degrade(HttpServletRequest request) {
        Action action = valueOfRequestAndType(request, UserActionType.DEGRADE);
        return dispatcher.execute(action);
    }

    /**
     * 默认生成带有Action.USER类型的Action
     *
     * @author finderlo
     * @date 17/04/2017
     */
    private Action valueOfRequestAndType(HttpServletRequest request, UserActionType actionType) {
        Action action = Action.valueOf(request);
        action.setType(ActionType.USER);
        action.put(Constant.ACTION_SUB_TYPE, actionType);
        return action;
    }


}
