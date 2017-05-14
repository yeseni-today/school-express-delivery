package com.delivery.common.dao;

import com.delivery.common.entity.OrderLogEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author finderlo
 * @date 21/04/2017
 */
@Component
public class OrderLogDao extends AbstractDao<OrderLogEntity> {
    @Override
    public List<OrderLogEntity> findBy(Map<String, String> attr, boolean likeQuery) {
        return null;
    }

    public List<OrderLogEntity> findByOrderId(String orderId) {
        return findBy("orderId",orderId);
    }
}
