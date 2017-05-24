package com.delivery.common.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.*;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "message", schema = "sed", catalog = "")
public class MessageEntity {
    private int id;
    private String senderId;
    private String receiverId;

    private MessageType type;
    private Timestamp createTime;
    private String title;
    private String content;
    private State state;


    public enum State {
        UNREAD, READ, NOT_SHOW
    }

    public enum MessageType {
        SYSTEM, APPLY, COMPLAIN_RESULT, ORDER_MSG, OTHER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "message_send_ID")
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    @Basic
    @Column(name = "message_receive_ID")
    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    @Basic
    @Column(name = "message_type")
    public MessageType getType() {
        return type;
    }

    public void setType(MessageType messageType) {
        this.type = messageType;
    }

    @Basic
    @Column(name = "message_date")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "message_title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "message_information")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "message_state")
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntity that = (MessageEntity) o;

        if (id != that.id) return false;
        if (senderId != null ? !senderId.equals(that.senderId) : that.senderId != null)
            return false;
        if (receiverId != null ? !receiverId.equals(that.receiverId) : that.receiverId != null)
            return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null)
            return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (senderId != null ? senderId.hashCode() : 0);
        result = 31 * result + (receiverId != null ? receiverId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
