package com.delivery.common.dao;

import com.delivery.common.entity.OrderOperationLogEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author finderlo
 * @date 21/04/2017
 */
//todo
@Component
public class OrderLogDao extends AbstractDao<OrderOperationLogEntity> {
    @Override
    public List<OrderOperationLogEntity> findBy(Map<String, String> attr, boolean likeQuery) {
        return null;
    }
}
