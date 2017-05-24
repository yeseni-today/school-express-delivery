package com.delivery.common.dao;

import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.ReviewEntity;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

/**
 * @author finderlo
 * @date 11/05/2017
 */
@Repository
public class ReviewDao extends AbstractDao<ReviewEntity> {
    public List<ReviewEntity> findByUserId(String userId) {
        return findBy("userId", userId);
    }

    public List<ReviewEntity> findByState(ReviewEntity.ReviewState state) {
        return findBy("state", state.ordinal() + "", false);
    }

    public List<ReviewEntity> findByState(int state) {
        return findBy("state", state + "", false);
    }


    public String newId() {
        Session session = sessionFactory.getCurrentSession();
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR) + "";
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String prefix = year + (month < 10 ? "0" + month : month) + (day < 10 ? "0" + day : day);


        StringBuilder builder = new StringBuilder();
        String sql = builder.append("SELECT MAX(review_ID) FROM review WHERE review_ID LIKE '")
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
}
