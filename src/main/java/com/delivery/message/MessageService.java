package com.delivery.message;

import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.action.ActionType;
import com.delivery.common.Response;
import com.delivery.common.constant.Constant;
import com.delivery.common.dao.MessageDao;
import com.delivery.common.entity.ComplaintEntity;
import com.delivery.common.entity.MessageEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.dispatch.Dispatcher;
import com.delivery.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

import static com.delivery.common.util.UserUtil.checkAdmin;
import static com.delivery.common.util.Util.*;
import static com.delivery.message.MessageUtil.*;
import static com.delivery.common.ErrorCode.*;

/**
 * Created by finderlo on 17/04/2017.
 */
@Component
public class MessageService implements ActionHandler, EventPublisher {

    @Autowired
    MessageDao messageDao;

    /**
     * 能否执行Action
     *
     * @author finderlo
     */
    @Override
    public boolean canHandleAction(Action action) {
        return action.getType() == ActionType.MESSAGE;
    }

    Dispatcher dispatcher;

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * 初始化信用体系相关的事件接收器
     *
     * @author finderlo
     * @date 02/05/2017
     */
    public void initEventListener() {
        EventManager manager = dispatcher.getEventManager();
        manager.register(Event.ManualHandleComplainResultEvent, manualHandleComplainResultEventEventExecutor());
    }

    public enum MessageSubActionType {
        sendSysMsg, send, receiveMsg, read, notshow
    }

    /**
     * 执行Action
     *
     * @author finderlo
     */
    @Override
    public Response execute(Action action) {
        MessageSubActionType tyoe = getActionSubType(action, MessageSubActionType.class);
        switch (tyoe) {
            case send:
                return send(action);
            case notshow:
                return notShow(action);
            case sendSysMsg:
                return sendSysMsg(action);
            case receiveMsg:
                return receiveMsg(action);
            case read:
                return read(action);
            default:
                return Response.error(MESSAGE_UNKNOWN_ACTION_TYPE);
        }
    }

    /**
     * 管理员发送信息
     *
     * @author Ticknick Hou
     * @date 08/05/2017
     */
    public Response sendSysMsg(Action action) {
        checkAdmin(action);
        UserEntity admin = getUser(action);
        action.put(MESSAGE_SENDID, admin.getId());
        return send(action);
    }

    /**
     * 发送一条消息
     *
     * @param action 发送者ID（系统或管理员）
     *               接受者ID（所有用户或者一类用户）
     *               消息内容结构体
     * @return success ； error 权限不足等造成失败
     * @author finderlo
     */
    public Response send(Action action) {
        MessageEntity message = getMessageEntity(action);
        messageDao.save(message);
        return Response.success();
    }

    /**
     * 获取用户收到的消息
     *
     * @param action 用户ID及用户身份（用于获取群发的消息）
     *               消息类型（可选）
     * @author finderlo
     */
    public Response receiveMsg(Action action) {
        UserEntity user = getUser(action);
        String userId = user.getId();
        List<MessageEntity> msgs = messageDao.findByReceiveUserId(userId);
        return Response.success(msgs);
    }

    /**
     * 将消息设为已读状态
     *
     * @param action 消息ID
     * @author finderlo
     */
    public Response read(Action action) {
        return setState(getMsgId(action), MessageEntity.MessageState.READ);
    }

    /**
     * 将消息设为不再显示状态
     *
     * @param action 消息ID
     * @author finderlo
     */
    public Response notShow(Action action) {
        return setState(getMsgId(action), MessageEntity.MessageState.NOT_SHOW);
    }


    public Response setState(String msgId, MessageEntity.MessageState type) {
        MessageEntity msg = messageDao.findById(msgId);
        msg.setState(type);
        messageDao.update(msg);
        return Response.success();
    }

    public void saveMessgae(MessageEntity messageEntity) {
        messageDao.save(messageEntity);
    }

    /**
     * 申诉处理结果，事件执行器，主要执行对用户双方发送信息
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    public EventExecutor manualHandleComplainResultEventEventExecutor() {
        EventExecutor executor = new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.ManualHandleComplainResultEvent) return;
                ComplaintEntity complaint = (ComplaintEntity) context.get(Constant.EVENT_COMPLAINT_ENTITY);
                StringBuilder builder = new StringBuilder();
                builder.append("你的订单(").append(complaint.getOrderId()).append("(")
                        .append("申诉已经有了结果，").append("请进入订单详情查看结果。");
                MessageEntity msg = new MessageBuilder().sender("0")
                        .receiver(complaint.getOrder().getRecipientId()).title("申诉结果通知").content(builder.toString())
                        .type(MessageEntity.MessageType.SYSTEM).state(MessageEntity.MessageState.UNREAD).build();
                MessageEntity msg1 = new MessageBuilder().sender("0")
                        .receiver(complaint.getOrder().getReplacementId()).title("申诉结果通知").content(builder.toString())
                        .type(MessageEntity.MessageType.SYSTEM).state(MessageEntity.MessageState.UNREAD).build();
                saveMessgae(msg);
                saveMessgae(msg1);
            }
        };
        return executor;
    }

    public static class MessageBuilder {

        private MessageEntity message;

        public MessageBuilder sender(String senderId) {
            message.setMessageSendId(senderId);
            return this;
        }

        public MessageBuilder receiver(String senderId) {
            message.setReceiverId(senderId);
            return this;
        }

        public MessageBuilder type(MessageEntity.MessageType type) {
            message.setType(type);
            return this;
        }

        public MessageBuilder title(String title) {
            message.setTitle(title);
            return this;
        }

        public MessageBuilder content(String content) {
            message.setContent(content);
            return this;
        }

        public MessageBuilder state(MessageEntity.MessageState state) {
            message.setState(state);
            return this;
        }

        public MessageEntity build() {
            message.setCreateTime(new Timestamp(System.currentTimeMillis()));
            if (message.getState() == null) {
                message.setState(MessageEntity.MessageState.UNREAD);
            }
            return message;
        }
    }

}
