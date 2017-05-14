package com.delivery.export;

import com.delivery.common.Response;
import com.delivery.common.action.Action;
import com.delivery.common.action.ActionType;
import com.delivery.common.constant.Constant;
import com.delivery.dispatch.Dispatcher;
import com.delivery.manual.ManualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import static com.delivery.common.util.Util.handleException;

/**
 * @author finderlo
 * @date 11/05/2017
 */
@RestController
public class ManualController {

    @Autowired
    Dispatcher dispatcher;


     @RequestMapping(HttpConstant.MANUAL_GET_REVIEW_BY_USER)
    public Response MANUAL_GET_USER_UPGRADE_LIST(HttpServletRequest request){
        return execute(request, ManualService.ManualActionType.showReviewToManager);
    }

    @RequestMapping(HttpConstant.MANUAL_GET_REVIEW_LIST)
    public Response MANUAL_GET_REVIEW_LIST(HttpServletRequest request){
        return execute(request, ManualService.ManualActionType.showWaitHandleReviewsToManager);
    }


    @RequestMapping(HttpConstant.MANUAL_HANDLE_USER_UPGRADE)
    public Response MANUAL_HANDLE_USER_UPGRADE(HttpServletRequest request){
        return execute(request, ManualService.ManualActionType.handleUpgrade);
    }


    @NotNull
    public Response execute(HttpServletRequest request,ManualService.ManualActionType type){
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
    private Action valueOfRequestAndType(HttpServletRequest request, ManualService.ManualActionType actionType) {
        Action action = Action.valueOf(request);
        action.setType(ActionType.MANUAL);
        action.put(Constant.ACTION_SUB_TYPE, actionType);
        return action;
    }

}
