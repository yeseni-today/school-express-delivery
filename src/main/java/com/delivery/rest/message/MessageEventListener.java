package com.delivery.rest.message;

import com.delivery.common.constant.Constant;
import com.delivery.common.dao.MessageDao;
import com.delivery.common.entity.*;
import com.delivery.event.*;
import com.delivery.event.context.ComplaintResultEventContext;
import com.delivery.event.context.OrderEventContext;
import com.delivery.event.context.UserUpgradeEventContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by finderlo on 17/04/2017.
 */
@Component
public class MessageEventListener {

    @Autowired
    private MessageDao messageDao;

    /**
     * 初始化消息相关的事件接收器
     *
     * @author finderlo
     * @date 02/05/2017
     */
    @Autowired
    public void registerEventListener(EventManager manager) {
        manager.register(Event.ComplaintResultEvent, handleComplaintResultEvent());
        manager.register(Event.UserUpgradeSuccessEvent, userUpgradeSuccessEventExecutor());
        manager.register(Event.UserUpgradeFailEvent, userUpgradeFailEventExecutor());
        manager.register(Event.OrderAcceptedEvent, orderAcceptedEventExecutor());
    }


    private void saveMessage(MessageEntity messageEntity) {
        messageDao.save(messageEntity);
    }

    /**
     * 申诉处理结果，事件执行器，主要执行对用户双方发送信息
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    private EventExecutor handleComplaintResultEvent() {
        return new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.ComplaintResultEvent) return;
                ComplaintEntity complaint = ((ComplaintResultEventContext) context).getComplaint();
                StringBuilder builder = new StringBuilder();
                builder.append("你的订单(").append(complaint.getOrderId()).append("(")
                        .append("申诉已经有了结果，").append("请进入订单详情查看结果。");
                MessageEntity msg = new MessageBuilder().sender("0")
                        .receiver(complaint.getOrder().getRecipientId()).title("申诉结果通知").content(builder.toString())
                        .type(MessageEntity.MessageType.SYSTEM).state(MessageEntity.State.UNREAD).build();
                MessageEntity msg1 = new MessageBuilder().sender("0")
                        .receiver(complaint.getOrder().getReplacementId()).title("申诉结果通知").content(builder.toString())
                        .type(MessageEntity.MessageType.SYSTEM).state(MessageEntity.State.UNREAD).build();
                saveMessage(msg);
                saveMessage(msg1);
            }
        };
    }

    /**
     * 用户升级成功，事件执行器，主要执行对用户发送信息
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    private EventExecutor userUpgradeFailEventExecutor() {
        return new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.UserUpgradeFailEvent) return;

                UserUpgradeEventContext context1 = (UserUpgradeEventContext) context;
                StringBuilder builder = new StringBuilder();
                builder.append("很抱歉，您升级失败了。请保持你的良好信用，并且过一段时间再试。申请单备注：" + context1.getReview().getRemark());
                MessageEntity msg = new MessageBuilder().sender("0")
                        .receiver(context1.getUser().getUid()).title("申请升级结果通知").content(builder.toString())
                        .type(MessageEntity.MessageType.SYSTEM).state(MessageEntity.State.UNREAD).build();
                saveMessage(msg);
            }
        };
    }


    /**
     * 用户升级成功，事件执行器，主要执行对用户发送信息
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    private EventExecutor userUpgradeSuccessEventExecutor() {
        return new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.UserUpgradeSuccessEvent) return;
                UserEntity user = ((UserUpgradeEventContext) context).getUser();
                MessageEntity msg = new MessageBuilder().sender("0")
                        .receiver(user.getUid()).title("申请升级结果通知").content("您升级成功了!!!")
                        .type(MessageEntity.MessageType.SYSTEM).state(MessageEntity.State.UNREAD).build();
                saveMessage(msg);
            }
        };
    }

    /**
     * @author Ticknick Hou
     * @date 16/05/2017
     */
    private EventExecutor orderAcceptedEventExecutor() {
        return new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.OrderAcceptedEvent) return;
                OrderEntity order = ((OrderEventContext) context).getOrder();
                MessageEntity msg = new MessageBuilder().sender(Constant.SYSTEM_MESSAGE_SENDER)
                        .receiver(order.getRecipientId()).title("您的订单被接单了").content("您的订单：" + order.getId() + ", 被用户 " + order.getReplacement().getName() + "接单了").build();
                saveMessage(msg);
            }
        };
    }


    public static class MessageBuilder {

        private MessageEntity message;

        public MessageBuilder sender(String senderId) {
            message.setSenderId(senderId);
            return this;
        }

        public MessageBuilder receiver(String senderId) {
            message.setReceiverId(senderId);
            return this;
        }

        public MessageBuilder type(MessageEntity.MessageType messageType) {
            message.setType(messageType);
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

        public MessageBuilder state(MessageEntity.State state) {
            message.setState(state);
            return this;
        }

        public MessageEntity build() {
            message.setCreateTime(new Timestamp(System.currentTimeMillis()));
            if (message.getState() == null) {
                message.setState(MessageEntity.State.UNREAD);
            }
            return message;
        }
    }

}
