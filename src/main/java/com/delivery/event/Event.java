package com.delivery.event;

import com.delivery.event.context.*;

/**
 * Created by finderlo on 15/04/2017.
 * 事件，系统内所有事件都需要在此枚举中
 *
 * @author finderlo
 */
public enum Event {

    ComplaintResultEvent(ComplaintResultEventContext.class),

    UserUpgradeSuccessEvent(UserEventContext.class),
    UserUpgradeFailEvent(UserEventContext.class),

    OrderPublishedEvent(OrderEventContext.class),
    OrderAcceptedEvent(OrderEventContext.class),
    OrderCompleteSuccessEvent(OrderEventContext.class),
    OrderReplacementCancelEvent(OrderReplacementCancelEventContext.class),
    OrderOutExpireEvent,
    OrderCommentSuccessEvent(OrderEventContext.class),

    UserRegisterEvent(UserRegisterEventContext.class),
    UserAutoUpgradeSuccessEvent,

    FundsDepositEvent,
    FundsFetchEvent,
    FundsWithDrawEvent, Manual_NewUpgradeEvent, Manual_NewComplaint;

    Class contextType;

    Event() {
    }

    Event(Class context) {
        this.contextType = context;
    }

    public Class getContextType() {
        return contextType;
    }

}
