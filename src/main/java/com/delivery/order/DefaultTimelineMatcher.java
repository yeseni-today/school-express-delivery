package com.delivery.order;

import com.delivery.common.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author finderlo
 * @date 20/04/2017
 */
@Component
public class DefaultTimelineMatcher implements TimelineMatcher {


    /**
     * 订单匹配
     * @author finderlo
     * @date 21/04/2017
     */
    @Override
    public Map<String, String> timelineCondition(UserEntity users) {
        String school = users.getUserSchoolname();
        String sex = users.getUserSex();

        Map<String,String> attr = new HashMap<>();

        attr.put("userSchoolname",school);
        attr.put("userSex",sex);
        return attr;
    }

}
