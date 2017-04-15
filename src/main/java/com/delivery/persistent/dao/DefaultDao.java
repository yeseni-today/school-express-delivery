package com.delivery.persistent.dao;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 默认Dao层，负责对和数据库的底层操作
 * Dao对传入的参数不会检查
 * 传入参数的检查交由DefaultDaoDelegate判断
 */

//@Component
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class DefaultDao implements Dao {


    @Autowired
    private SessionFactory sessionFactory;


    public List sort(List t) {
        return t;
    }

    public <T> List<T> findAll(Class<T> classT) {
        Session session = sessionFactory.getCurrentSession();
        List tList = session.createQuery("from " + classT.getName()).list();

        if (tList == null) {
            return new ArrayList<T>();
        }
        return sort(tList);
    }

    @Override
    public <T> List<T> findBy(Class<T> classT, String key, String value, boolean isLikeQuery) {
        HashMap<String, String> temp = new HashMap<>();
        temp.put(key, value);
        return findBy(classT, temp, isLikeQuery);
    }


    @Override
    public <T> List<T> findBy(Class<T> classT, Map<String, String> keyValue, boolean isLikeQuery) {

        String hql = jointLikeQuery(classT.getName(), keyValue, isLikeQuery);

        if (hql == null) {
            return new ArrayList<T>();
        }

        Session session = sessionFactory.getCurrentSession();
        List result = session.createQuery(hql).list();
        return sort(result);
    }


    protected String jointLikeQuery(String className, Map<String, String> keyValue, boolean isLikeQuery) {
//        String hql = " from UsersEntity e where e.usersName like 'xiao%' and e.usersPassword like 'psd%'";
        String head = "from " + className;

        int enableCount = 0;

        StringBuilder hqlbuilder = new StringBuilder(head);

        boolean isFirst = true;
        Set<Map.Entry<String, String>> entrySet = keyValue.entrySet();

        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key == null || key.trim().equals("") || value == null || value.trim().equals("")) {
                continue;
            }
            if (!isFirst) {
                hqlbuilder.append(" and ");
            } else {
                hqlbuilder.append(" e where ");
                isFirst = false;
            }
            hqlbuilder.append("e.");
            hqlbuilder.append(entry.getKey());


            if (isLikeQuery) {
                hqlbuilder.append(" like ");
                value = "%" + value + "%";
            } else {
                hqlbuilder.append(" =");
            }
            hqlbuilder.append("'").append(value).append("'");
            enableCount++;
        }

        if (enableCount == 0) {
            return null;
        }
        System.out.println(hqlbuilder.toString());
        return hqlbuilder.toString();
    }


    public void save(Object t) {
        Session session = sessionFactory.getCurrentSession();
        session.save(t);
    }

    public void saveOrUpdate(Object t) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(t);
    }

    public void delete(Object t) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(t);
    }

    public void update(Object t) {
        if (t == null) {
            return;
        }
        Session session = sessionFactory.getCurrentSession();
        session.update(t);
    }

}

