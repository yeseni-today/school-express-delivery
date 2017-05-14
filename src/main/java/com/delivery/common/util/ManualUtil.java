package com.delivery.common.util;

import com.delivery.common.action.Action;

import static com.delivery.common.constant.Constant.COMPLAINT_ID;
import static com.delivery.common.constant.Constant.REVIEW_ID;

/**
 * @author finderlo
 * @date 14/05/2017
 */
public class ManualUtil {

    public static String getReviewId(Action action) {
        return (String) action.getOrDefault(REVIEW_ID, "");
    }

    public static String getComplaintId(Action action) {
        return (String) action.getOrDefault(COMPLAINT_ID, "");
    }

}
