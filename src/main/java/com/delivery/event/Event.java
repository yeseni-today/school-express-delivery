package com.delivery.event;

/**
 * Created by finderlo on 15/04/2017.
 * 事件，系统内所有事件都需要在此枚举中
 *
 * @author finderlo
 */
public enum Event {

    ManualHandleComplainResultEvent,
    ManualHandleComplainFailEvent,
    ManualHandleUserUpgradeSuccessEvent,
    ManualHandleUserUpgradeFailEvent,

    OrderPublishedEvent,
    OrderAcceptedEvent,
    OrderCompleteSuccessEvent,
    OrderReplacementCancelEvent,
    OrderOutExpireEvent,

    UserRegisterEvent,
    UserAutoUpgradeSuccessEvent,

    FundsDepositEvent,
    FundsFetchEvent,
    FundsWithDrawEvent, OrderCommentSuccessEvent, Manual_NewUpgradeEvent, Manual_NewComplaint;

}
