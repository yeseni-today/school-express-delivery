package com.delivery.manual;

import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.response.Response;
import com.delivery.event.EventPublisher;

/**
 * @author finderlo
 * @date 17/04/2017
 */
public class ManualService implements ActionHandler, EventPublisher {
    @Override
    public boolean canHandleAction(Action action) {
        //todo
        return false;
    }

    @Override
    public Response execute(Action action) {
        //todo
        return null;
    }

    /**
     * 用户申请升级
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public Response applyUpgrade(Action action) {
        //todo
        return null;
    }


    /**
     * 用户申请降级
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public Response applyDegrade(Action action) {
        //todo
        return null;
    }

    /**
     * 人工处理升级
     *
     * @param action 收件人
     *               结果
     *               客服
     * @author finderlo
     * @date 17/04/2017
     */
    public Response handleUpgrade(Action action) {
        //todo
        return null;
    }


    /**
     * 人工处理降级
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public Response handleDegrade(Action action) {
        //todo
        return null;
    }

    /**
     * 用户申诉一个订单
     *
     * @param action 订单号
     *               申诉申请人ID
     * @author finderlo
     * @date 17/04/2017
     */
    public Response orderComplain(Action action) {
        //todo
        return null;
    }

    /**
     * 人工处理一个订单申诉，成功或者失败，发布相对应事件
     *
     * @param action 订单号
     *               客服
     * @author finderlo
     * @date 17/04/2017
     */
    public Response orderHandleComplain(Action action) {
        //todo
        return null;
    }
}
