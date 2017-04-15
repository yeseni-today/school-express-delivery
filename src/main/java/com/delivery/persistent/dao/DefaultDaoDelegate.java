package com.delivery.persistent.dao;

import com.delivery.common.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by finderlo on 2017/4/7.
 */
@Component
public class DefaultDaoDelegate implements Dao {

    @Autowired
    private Dao defaultDao;

    private List<Class> entitys = new ArrayList<>();

    private Map<Class, List<String>> keys = new HashMap<>();

    public DefaultDaoDelegate(Dao dao) {
        this.defaultDao = dao;
        initialEntities();
        initialEntityKeys();
    }

    private void initialEntities() {
        Class[] entis = {
                ComplaintEntity.class,
                CreditRecordEntity.class,
                ManagerEntity.class,
                MessageEntity.class,
                OrdersEntity.class,
                OrdersOperationLogEntity.class,
                PaymentInfomationEntity.class,
                ReviewEntity.class,
                UsersEntity.class
        };
        entitys.addAll(Arrays.asList(entis));
    }

    private void initialEntityKeys() {
        for (Class entity : entitys) {
            List<String> results = new ArrayList<>();
            for (Field field : entity.getDeclaredFields()) {
                results.add(field.getName());
            }
            keys.put(entity, results);
        }
    }


    public <T> List<T> findAll(Class<T> classT) {
        check(classT);
        return defaultDao.findAll(classT);
    }

    @Override
    public <T> List<T> findBy(Class<T> classT, String key, String value, boolean isLikeQuery) {
        check(classT,key);
        return defaultDao.findBy(classT, key, value, isLikeQuery);
    }

    public <T> List<T> findBy(Class<T> classT, Map<String, String> keyValues, boolean isLikeQuery) {
        check(classT,keyValues);
        return defaultDao.findBy(classT, keyValues, isLikeQuery);
    }


    public void save(Object t) {
        check(t);
        defaultDao.save(t);
    }

    public void saveOrUpdate(Object t) {
        check(t);
        defaultDao.saveOrUpdate(t);
    }

    public void delete(Object t) {
        check(t);
        defaultDao.delete(t);
    }

    public void update(Object t) {
        check(t);
        defaultDao.update(t);
    }

    private void check(Object o){
        if (!entitys.contains(o.getClass())) {
            throw new IllegalArgumentException("非实体类参数");
        }
    }

    private void check(Class classT) {
        if (!entitys.contains(classT)) {
            throw new IllegalArgumentException("非实体类参数");
        }
    }

    private void check(Class classT, Map<String, String> keyValues) {
        check(classT);
        List<String> classkeys = keys.get(classT);
        Set<String> key = keyValues.keySet();
        if (!classkeys.containsAll(key)) {
                throw new IllegalArgumentException("非实体类参数|关键字不是类中属性");
        }
    }

    private void check(Class classT, String key) {
        check(classT);
        List<String> classkeys = keys.get(classT);
        if (!classkeys.contains(key)) {
            throw new IllegalArgumentException("非实体类参数|关键字不是类中属性");
        }
    }

}
