package com.delivery.common.dao;

import com.delivery.common.dao.AbstractDao;
import com.delivery.common.entity.OrdersEntity;
import com.delivery.common.entity.UsersEntity;
import com.delivery.order.OrderState;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.internal.CriteriaImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
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
        String[] key = new String[]{"ordersId", "ordersState"};
        return super.findBy(key, new String[]{id, state.toString()}, false);
    }

    public List<OrdersEntity> findByReplacementId(String id) {
        String[] key = new String[]{"replacementId"};
        return findBy(key, new String[]{id}, false);
    }

    public List<OrdersEntity> findByRecipientId(String id) {
        String[] key = new String[]{"recipientId"};
        return findBy(key, new String[]{id}, false);
    }

    public String newOrderId(IdType idType) {
        Session session = sessionFactory.getCurrentSession();
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR) + "";
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String prefix = year + (month < 10 ? "0" + month : month) + (day < 10 ? "0" + day : day) + idType.getValue();


        StringBuilder builder = new StringBuilder();
        String sql = builder.append("SELECT MAX(orders_ID) FROM orders WHERE orders_ID LIKE '")
                .append(prefix)
                .append("%' ")
                .toString();
        SQLQuery l = session.createSQLQuery(sql);
        List list = l.list();
        if (list == null || list.size() == 0) {
            //当天没有，生成新的订单号
            String id = prefix + "00000";
            return id;
        } else {
            String id = (String) l.list().get(0);
            Integer idd = Integer.valueOf(id);
            String newid = String.valueOf(idd + 1);
            return newid;
        }
    }

    public enum IdType {
        ORDER(1),
        CHECK(2),
        APPLY(3);
        int value;

        IdType(int y) {
            value = y;
        }

        public int getValue() {
            return value;
        }
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.MONTH));
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
    }


    //

}
