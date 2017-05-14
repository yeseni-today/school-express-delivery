package com.delivery.export;

import com.delivery.common.Response;
import com.delivery.common.action.Action;
import com.delivery.common.action.ActionType;
import com.delivery.common.constant.Constant;
import com.delivery.dispatch.Dispatcher;
import com.delivery.order.OrderActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

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

    @GetMapping(ORDER_CHECK_CREATE)
    public Response check_create(HttpServletRequest request){
        return execute(request,OrderActionType.check_create);
    }

    @PostMapping(ORDER_CREATE_NORMAL)
    public Response create(HttpServletRequest request){
        return execute(request,OrderActionType.create);

    }

    @GetMapping(ORDER_FIND)
    public Response find(HttpServletRequest request){
        return execute(request,OrderActionType.find);
    }

    @GetMapping(ORDER_FIND_BY_USER_UNCOMPLETE)
    public Response findUserOrder(HttpServletRequest request){
        try {
            Action action = valueOfRequestAndType(request,OrderActionType.findUserOrder);
            action.put(Constant.ORDER_ATTR_COMPLETE_STATE,0+"");
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }

    @GetMapping(ORDER_FIND_BY_USER_COMPLETE)
    public Response findUserOrde1r(HttpServletRequest request){
        try {
            Action action = valueOfRequestAndType(request,OrderActionType.findUserOrder);
            action.put(Constant.ORDER_ATTR_COMPLETE_STATE,1+"");
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }
    @PostMapping(ORDER_TIMELINE)
    public Response timeline(HttpServletRequest request){
        try {
            Action action = valueOfRequestAndType(request,OrderActionType.timeline);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }
    @PostMapping(ORDER_ACCEPT)
    public Response accept(HttpServletRequest request){
        try {
            Action action = valueOfRequestAndType(request,OrderActionType.accept);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }
    @PostMapping(ORDER_UPDATE)
    public Response update(HttpServletRequest request){
        try {
            Action action = valueOfRequestAndType(request,OrderActionType.update);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }
    @PostMapping(ORDER_AFFITM)
    public Response affirm(HttpServletRequest request){
        try {
            Action action = valueOfRequestAndType(request,OrderActionType.affirm);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }
    @PostMapping(ORDER_COMMENT)
    public Response comment(HttpServletRequest request){
        try {
            Action action = valueOfRequestAndType(request,OrderActionType.comment);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }

    @PostMapping(ORDER_CANCEL)
    public Response cancel(HttpServletRequest request){
        try {
            Action action = valueOfRequestAndType(request,OrderActionType.cancel);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }

    @PostMapping(ORDER_COMPLAIN)
    public Response complain(HttpServletRequest request){
        try {
            Action action = valueOfRequestAndType(request,OrderActionType.complain);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }
    @PostMapping(ORDER_DELIVERY)
    public Response delivery(HttpServletRequest request){
        try {
            Action action = valueOfRequestAndType(request,OrderActionType.delivery);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }
    
    @GetMapping(ORDER_FIND_ORDER_AND_USER)
    public Response findOrderAndUser(HttpServletRequest request){
        try {
            Action action = valueOfRequestAndType(request,OrderActionType.findOrderAndUser);
            return dispatcher.execute(action);
        }catch (Exception e){
            return handleException(e);
        }
    }

    @GetMapping(ORDER_FIND_ORDER_LOG)
    public Response findOrderLog(HttpServletRequest request){
        return execute(request,OrderActionType.findOrderLog);
    }

    @NotNull
    public Response execute(HttpServletRequest request,OrderActionType type){
        try {
            Action action = valueOfRequestAndType(request,type);
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
