package com.delivery.rest.order;

import com.delivery.common.constant.Constant;
import com.delivery.common.dao.CreditRecordDao;
import com.delivery.common.dao.OrderDao;
import com.delivery.common.entity.CreditRecordEntity;
import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author finderlo
 * @date 16/05/2017
 */
@Component
public class OrderUtil {

    @Autowired
    CreditRecordDao creditRecordDao;

    @Autowired
    OrderDao orderDao;

    public boolean canCreate(UserEntity user) {
        if (getCreditValue(user) < Constant.USER_CAN_CREATE_ORDER_CREDIT_LIMIT) {
            return false;
        }
        int runingOrderCount = orderDao.findByReplacementId(user.getUid()).size();
        if (runingOrderCount > Constant.USER_CREATE_ORDER_MAX_COUNT) {
            return false;
        }

        int complaintCount = orderDao.findByIdAndState(user.getUid(), OrderEntity.OrderState.COMPLAINING).size();
        if (complaintCount != 0) {
            return false;
        }

        return true;
    }

    public int getCreditValue(UserEntity user) {
        List<CreditRecordEntity> records = creditRecordDao.findByUserId(user.getUid());
        int value = 0;
        for (CreditRecordEntity record : records) {
            value += record.getValue();
        }
        return value;
    }


    public boolean isParticipation(UserEntity user, OrderEntity order) {
        return user.getUid().equals(order.getRecipientId()) || user.getUid().equals(order.getReplacementId());
    }
}
