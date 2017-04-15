package com.delivery.persistent.dao;

import org.hibernate.Session;

import java.util.List;
import java.util.Map;

/**
 * Created by finderlo on 2017/4/7.
 */
public interface Dao {

    <T> List<T> findAll(Class<T> classT);

    <T> List<T> findBy(Class<T> classT,String key,String value,boolean isLikeQuery);

    <T> List<T> findBy(Class<T> classT, Map<String, String> keyValues, boolean isLikeQuery);

    void save(Object t);

    void saveOrUpdate(Object t);

    void delete(Object t);

    void update(Object t);
}
