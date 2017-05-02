package com.delivery.common.dao;

import com.delivery.common.entity.CreditRecordEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Ticknick Hou
 * @date 02/05/2017
 */
@Component
@Repository
public class CreditRecordDao extends AbstractDao<CreditRecordEntity> {


    public int getCreditValueByUserId(String userId) {
        List<CreditRecordEntity> records = findBy("userId", userId, false);
        int recordval = 0;
        for (CreditRecordEntity record : records) {
            recordval += record.getCreditChange();
        }
        return recordval;
    }

    public List<CreditRecordEntity> findByUserId(String userId){
        return findBy("userId",userId,false);
    }

}
