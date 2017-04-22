package com.delivery.common.dao;

import com.delivery.common.entity.UsersEntity;
import com.delivery.common.util.Util;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.engine.query.spi.sql.NativeSQLQueryReturn;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by finderlo on 15/04/2017.
 */
@Component
@Repository
public class UsersDao extends AbstractDao<UsersEntity> {

    public UsersEntity findByUserPhone(String value) {
        List<UsersEntity> usersEntities =  super.findBy("userPhone", value);
        Util.checkNull(usersEntities);
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
