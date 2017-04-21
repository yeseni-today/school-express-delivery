package com.delivery.common.util;

/**
 * @author finderlo
 * @date 21/04/2017
 */
public enum TimeUnit {
    SECOND(1),
    MINUTES(60),
    HOURS(60 * 60),
    DAYS(60 * 60 * 24);

    int weight;

    TimeUnit(int weight) {
        this.weight = weight;
    }

    int getWeight() {
        return weight;
    }
}