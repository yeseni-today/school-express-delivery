package com.delivery.common.dao;

import com.delivery.common.entity.UserEntity;
import com.delivery.common.util.Util;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by finderlo on 15/04/2017.
 */
@Component
@Repository
public class UserDao extends AbstractDao<UserEntity> {

    public UserEntity findByPhone(String value) {
        List<UserEntity> usersEntities =  super.findBy("phone", value);
        if (usersEntities.size()==0){
            return null;
        }
        return usersEntities.get(0);
    }

    public String newUsersId(){
        Session session = sessionFactory.getCurrentSession();
        SQLQuery l = session.createSQLQuery("SELECT MAX(user_ID) FROM users");
        String id = (String) l.list().get(0);
        Integer idd = Integer.valueOf(id);
        String newid = String.valueOf(idd + 1);
        return newid;
    }
}
