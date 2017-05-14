package com.delivery.common.dao;

import com.delivery.common.entity.MessageEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author finderlo
 * @date 08/05/2017
 */
@Repository
public class MessageDao extends AbstractDao<MessageEntity> {
    public List<MessageEntity> findByReceiveUserId(String userId) {
        return findBy("receiverId",userId,false);
    }
}
