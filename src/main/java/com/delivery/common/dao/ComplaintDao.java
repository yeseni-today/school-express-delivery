package com.delivery.common.dao;

import com.delivery.common.entity.ComplaintEntity;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * @author finderlo
 * @date 08/05/2017
 */
@Repository
@Controller
public class ComplaintDao extends AbstractDao<ComplaintEntity> {

    public List<ComplaintEntity> findByWaitDeal() {
        return findBy("state", ComplaintEntity.State.WAIT_DEAL.ordinal() + "");
    }

    public List<ComplaintEntity> findByOrderId(String orderId) {
        return findBy("orderId", orderId);
    }

    public String newId() {
        Session session = sessionFactory.getCurrentSession();
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR) + "";
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String prefix = year + (month < 10 ? "0" + month : month) + (day < 10 ? "0" + day : day);


        StringBuilder builder = new StringBuilder();
        String sql = builder.append("SELECT MAX(complaintId) FROM complaint WHERE complaintId LIKE '")
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
            return newid;
        }
    }

    @Autowired
    UserDao userDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    ManagerDao managerDao;

    public void casadeUpdate(ComplaintEntity complaintEntity) {
        super.update(complaintEntity);
        if (complaintEntity.getOrder() != null) {
            orderDao.update(complaintEntity.getOrder());
        }
        if (complaintEntity.getUser() != null) {
            userDao.update(complaintEntity.getUser());
        }
        if (complaintEntity.getManager() != null) {
            managerDao.update(complaintEntity.getManager());
        }
    }

    public List<ComplaintEntity> findByUserId(String uid) {
        return findBy("userId", uid, false);
    }

    public Collection<? extends ComplaintEntity> findByState(ComplaintEntity.Type state) {
        return findBy("state", state.ordinal() + "", false);
    }
}
