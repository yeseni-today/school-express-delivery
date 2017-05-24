package com.delivery.event.context;

import com.delivery.common.entity.UserEntity;
import com.delivery.event.EventContext;

/**
 * @author finderlo
 * @date 16/05/2017
 */
public class UserEventContext extends EventContext {

    private final UserEntity user;

    public UserEventContext(UserEntity user) {
        this.user = user;
    }


    public UserEntity getUser() {
        return user;
    }
}
