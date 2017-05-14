package com.delivery.message;

import com.delivery.common.SedException;
import com.delivery.common.action.Action;
import com.delivery.common.entity.MessageEntity;

import java.sql.Timestamp;

import static com.delivery.common.util.Util.*;
import static com.delivery.common.ErrorCode.*;

/**
 * @author finderlo
 * @date 08/05/2017
 */
public class MessageUtil {


    static String MESSAGE_ID = "msg_id";
    static String MESSAGE_RECEIVEID = "msg_receiver";
    static String MESSAGE_SENDID = "msg_sender";
    static String MESSAGE_TYPE = "msg_type";
    static String MESSAGE_TILE = "msg_title";
    static String MESSAGE_CONTENT  = "msg_content";
    static String MESSAGE_MESSAGE_STATE = "msg_state";

    public static String getMsgId(Action action) {
        String msgId = (String) action.getOrDefault(MESSAGE_ID,"");
        checkNotEqual(msgId,"",MESSAGE_ID_NULL);
        return msgId;
    }

    public static MessageEntity getMessageEntity(Action action) {
        String sendId = (String) action.getOrDefault(MESSAGE_SENDID, "");
        String rece = (String) action.getOrDefault(MESSAGE_RECEIVEID, "");
        MessageEntity.MessageType type = getMessageType(action);
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        String title = (String) action.getOrDefault(MESSAGE_TILE,"");
        String content = (String) action.getOrDefault(MESSAGE_CONTENT,"");
        MessageEntity.MessageState state = getMessageState(action);

        MessageEntity message = new MessageEntity();
        message.setMessageSendId(sendId);
        message.setReceiverId(rece);
        message.setType(type);
        message.setCreateTime(createTime);
        message.setTitle(title);
        message.setContent(content);
        message.setState(state);

        checkMessage(message);
        return message;
    }

    private static void checkMessage(MessageEntity message) {
        checkNotEqual(message.getMessageSendId(),"",MESSAGE_SENDID_NULL);
        checkNotEqual(message.getReceiverId(),"",MESSAGE_RECEIVER_NULL);
    }

    private static MessageEntity.MessageState getMessageState(Action action) {
        int ori = Integer.valueOf((String) action.getOrDefault(MESSAGE_MESSAGE_STATE, "0"));
        for (MessageEntity.MessageState type : MessageEntity.MessageState.values()) {
            if (type.ordinal() == ori) {
                return type;
            }
        }
        throw new SedException(MESSAGE_UNKOWN_MESSAGE_STATE);
    }


    private static MessageEntity.MessageType getMessageType(Action action) {
        int ori = Integer.valueOf((String) action.getOrDefault(MESSAGE_TYPE, "-1"));
        for (MessageEntity.MessageType type : MessageEntity.MessageType.values()) {
            if (type.ordinal() == ori) {
                return type;
            }
        }
        throw new SedException(MESSAGE_UNKOWN_MESSAGE_TYPE);
    }
}
