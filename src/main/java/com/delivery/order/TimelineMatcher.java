package com.delivery.order;

import com.delivery.common.entity.UserEntity;

import java.util.Map;

/**
 * 这个类对应的是OrdersEntity的查询条件，通过UsersEntity的信息来获取查询条件
 * 这里的目的是为了让特定的用户只能看到特定的订单，防止信息泄露
 * @author finderlo
 * @date 17/04/2017
 */
public interface TimelineMatcher {

    Map<String,String> timelineCondition(UserEntity users);

}
