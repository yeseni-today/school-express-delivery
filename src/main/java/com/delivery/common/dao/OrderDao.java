package com.delivery.common.dao;

import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.order.OrderState;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author finderlo
 * @date 17/04/2017
 */
@Component
@Repository
public class OrderDao extends AbstractDao<OrderEntity> {

    public List<OrderEntity> findByIdAndState(String id, OrderState state) {
        String[] key = new String[]{"ordersId", "ordersState"};
        return super.findBy(key, new String[]{id, state.toString()}, false);
    }

    public List<OrderEntity> findByReplacementId(String id) {
        String[] key = new String[]{"replacementId"};
        return findBy(key, new String[]{id}, false);
    }

    public List<OrderEntity> findByRecipientId(String id) {
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
        String id = (String) list.get(0);
        if (id == null || "null".equals(id)) {
            //当天没有，生成新的订单号
            id = prefix + "00000";
            return id;
        } else {
            long idd = Long.valueOf(id);
            String newid = String.valueOf(idd + 1);
            System.out.println(newid);
            return newid;
        }
    }

    //todo 在lx下测试
    public List<OrderEntity> findByUserMatch(UserEntity user) {
        List<OrderEntity> res = new ArrayList<>();
        Session session = sessionFactory.getCurrentSession();
        int state = OrderState.WAIT_ACCEPT.ordinal();
        SQLQuery query = session.createSQLQuery("select *  from orders where orders_state='" + state + "' and recipient_ID in(SELECT user_ID FROM users WHERE user_sex=(select user_sex from users where user_ID='" + user.getUserId() + "'))");
        res.addAll(query.addEntity(OrderEntity.class).list());

        System.out.println(res.size());
        return res;
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
