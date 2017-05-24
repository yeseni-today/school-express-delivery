package com.delivery.rest.message;

import com.delivery.common.Response;
import com.delivery.common.dao.MessageDao;
import com.delivery.common.entity.MessageEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.common.util.Assert;
import com.delivery.common.util.Util;
import com.delivery.config.annotation.AdminAuthorization;
import com.delivery.config.annotation.Authorization;
import com.delivery.config.annotation.CurrentUser;
import com.delivery.config.annotation.EnumParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.delivery.common.constant.HttpStatus.*;

/**
 * @author finderlo
 * @date 16/05/2017
 */
@RestController
@RequestMapping("/messages")
public class MessageController {


    @Autowired
    private
    MessageDao messageDao;

    @PostMapping
    @AdminAuthorization
    public Response newMessage(
            @CurrentUser UserEntity user,
            @RequestParam String receiver,
            @EnumParam MessageEntity.MessageType type,
            @RequestParam String title,
            @RequestParam String content) {
        MessageEntity message = new MessageEntity();
        message.setState(MessageEntity.State.UNREAD);
        message.setSenderId(user.getUid());
        message.setReceiverId(receiver);
        message.setTitle(title);
        message.setContent(content);
        message.setCreateTime(Util.now());
        message.setType(type);

        messageDao.save(message);
        return Response.ok(message);
    }

    /**
     * 获取用户消息
     *
     * @author Ticknick Hou
     * @date 16/05/2017
     */
    @GetMapping
    @Authorization
    public Response find(
            @CurrentUser UserEntity user) {
        return Response.ok(messageDao.findByReceiveUserId(user.getUid()));
    }

    @PutMapping("/{message_id}")
    @Authorization
    public Response modify(
            @CurrentUser UserEntity user,
            @PathVariable String message_id,
            @EnumParam MessageEntity.State state) {

        MessageEntity message = messageDao.findById(message_id);
        Assert.notNull(message, NOT_FOUND, "message is not existed");
        Assert.isTrue(user.getUid().equals(message.getReceiverId()), "you only modify your received message");
        message.setState(state);
        messageDao.update(message);
        return Response.ok(message);
    }

}
