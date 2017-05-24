package com.delivery.rest.user;

import com.delivery.common.dao.UserDao;
import com.delivery.common.entity.UserEntity;
import com.delivery.event.Event;
import com.delivery.event.EventContext;
import com.delivery.event.EventExecutor;
import com.delivery.event.EventManager;
import com.delivery.event.context.UserUpgradeEventContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author finderlo
 * @date 17/05/2017
 */
@Component
public class UserEventListener {


    @Autowired
    private UserDao userDao;

    @Autowired
    private void registerEventListener(EventManager manager) {
        manager.register(Event.UserUpgradeSuccessEvent, userUpgradeSuccessEvent());
    }

    private EventExecutor userUpgradeSuccessEvent() {
        return new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (!event.equals(Event.UserUpgradeSuccessEvent)) {
                    return;
                }
                UserUpgradeEventContext context1 = (UserUpgradeEventContext) context;
                UserEntity user = context1.getUser();
                user.setIdentity(UserEntity.UserIdentity.REPLACEMENT);
                userDao.update(user);
            }
        };
    }


}
