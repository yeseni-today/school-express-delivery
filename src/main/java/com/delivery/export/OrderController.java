package com.delivery.export;

import com.delivery.common.Response;
import com.delivery.common.action.Action;
import com.delivery.common.action.ActionType;
import com.delivery.common.constant.Constant;
import com.delivery.dispatch.Dispatcher;
import com.delivery.order.OrderActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.delivery.common.util.Util.*;
import static com.delivery.export.HttpConstant.*;
/**
 * @author finderlo
 * @date 22/04/2017
 */
@RestController
public class OrderController {

    private final Dispatcher dispatcher;

    @Autowired
    public OrderController(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @PostMapping(ORDER_CHECK_CREATE)
    public Response check_create(HttpServletRequest httpRequest){
        try {
            Action action = valueOfRequestAndType(httpRequest,OrderActionType.check_create);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }

    @PostMapping(ORDER_CREATE_NORMAL)
    public Response create(HttpServletRequest httpRequest){
        try {
            Action action = valueOfRequestAndType(httpRequest,OrderActionType.create);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }

    @PostMapping(ORDER_FIND)
    public Response find(HttpServletRequest httpRequest){
        try {
            Action action = valueOfRequestAndType(httpRequest,OrderActionType.find);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }

    @PostMapping(ORDER_FIND_BY_USER)
    public Response findUserOrder(HttpServletRequest httpRequest){
        try {
            Action action = valueOfRequestAndType(httpRequest,OrderActionType.findUserOrder);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }

    @PostMapping(ORDER_TIMELINE)
    public Response timeline(HttpServletRequest httpRequest){
        try {
            Action action = valueOfRequestAndType(httpRequest,OrderActionType.timeline);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }
    @PostMapping(ORDER_ACCEPT)
    public Response accept(HttpServletRequest httpRequest){
        try {
            Action action = valueOfRequestAndType(httpRequest,OrderActionType.accept);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }
    @PostMapping(ORDER_UPDATE)
    public Response update(HttpServletRequest httpRequest){
        try {
            Action action = valueOfRequestAndType(httpRequest,OrderActionType.update);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }
    @PostMapping(ORDER_AFFITM)
    public Response affirm(HttpServletRequest httpRequest){
        try {
            Action action = valueOfRequestAndType(httpRequest,OrderActionType.affirm);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }
    @PostMapping(ORDER_COMMENT)
    public Response comment(HttpServletRequest httpRequest){
        try {
            Action action = valueOfRequestAndType(httpRequest,OrderActionType.comment);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }

    @PostMapping(ORDER_CANCEL)
    public Response cancel(HttpServletRequest httpRequest){
        try {
            Action action = valueOfRequestAndType(httpRequest,OrderActionType.cancel);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }

    @PostMapping(ORDER_COMPLAIN)
    public Response complain(HttpServletRequest httpRequest){
        try {
            Action action = valueOfRequestAndType(httpRequest,OrderActionType.complain);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }
    @PostMapping(ORDER_DELIVERY)
    public Response delivery(HttpServletRequest httpRequest){
        try {
            Action action = valueOfRequestAndType(httpRequest,OrderActionType.delivery);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }



    /**
     * 默认生成带有Action.USER类型的Action
     *
     * @author finderlo
     * @date 17/04/2017
     */
    private Action valueOfRequestAndType(HttpServletRequest request, OrderActionType actionType) {
        Action action = Action.valueOf(request);
        action.setType(ActionType.ORDER);
        action.put(Constant.ACTION_SUB_TYPE, actionType);
        return action;
    }

}
