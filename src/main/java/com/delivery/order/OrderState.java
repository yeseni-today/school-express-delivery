package com.delivery.order;

/**
 * 等待接单、已接单、已收件、已确认、待评价、已完成、已取消、申诉中、已赔偿
 *
 * @author finderlo
 * @date 17/04/2017
 */
public enum OrderState {

    // -1 代表完成订单

    //等待接单
    WAIT_ACCEPT,

    //已接单
    ACCEPTED,

    //已收件，待送达
    TAKE_PARCEL_WAIT_DELIVERY,

    //已确认
    AFFIRMATIVE,

    //待评价 -1
    WAIT_COMMENT,

    //已完成 -1
    COMPLETED,

    //以取消
    CANCELED,

    //申诉中
    COMPLAINING,

    //已赔偿 -1
    COMPENSATION
}
