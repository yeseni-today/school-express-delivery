package com.delivery.funds;

import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.Response;
import com.delivery.event.EventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author finderlo
 * @date 17/04/2017
 */
@Component
public class FundsService implements ActionHandler,EventPublisher {
    /**
     * @author finderlo
     * @date 17/04/2017
     */
    public boolean canHandleAction(Action action) {
        //todo
        return false;
    }

    /**
     * @author finderlo
     * @date 17/04/2017
     */
    public Response execute(Action action) {
        //todo
        return null;
    }

    /**
     * 支付一个订单
     *
     * @param action 订单号
     * @author finderlo
     * @date 17/04/2017
     */
    public Response payOrder(Action action) {
        //todo

        return null;
    }

    /**
     * 申请提现一个订单
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public Response withdraw(Action action) {
        //todo

        return null;
    }
}
