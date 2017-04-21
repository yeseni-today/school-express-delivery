package com.delivery.common.dao;

import com.delivery.common.dao.AbstractDao;
import com.delivery.common.entity.OrdersEntity;
import com.delivery.common.entity.UsersEntity;
import com.delivery.order.OrderState;
import org.hibernate.internal.CriteriaImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author finderlo
 * @date 17/04/2017
 */
@Component
@Repository
public class OrdersDao extends AbstractDao<OrdersEntity> {

    public List<OrdersEntity> findByIdAndState(String id, OrderState state) {
        String[] key = new String[]{"ordersId","ordersState"};
        return super.findBy(key,new String[]{id,state.toString()},false);
    }

}
