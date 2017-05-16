package com.delivery.event.context;

import com.delivery.common.entity.ReviewEntity;
import com.delivery.common.entity.UserEntity;

/**
 * @author finderlo
 * @date 16/05/2017
 */
public class UserUpgradeEventContext extends UserEventContext {

    private final ReviewEntity review;

    public UserUpgradeEventContext(UserEntity user, ReviewEntity review) {
        super(user);
        this.review = review;
    }

    public ReviewEntity getReview() {
        return review;
    }
}
