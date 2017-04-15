package com.delivery.common.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "message", schema = "sed", catalog = "")
public class MessageEntity {
    private int messageId;
    private String messageSendId;
    private String messageReceiveId;
    private String messageType;
    private Timestamp messagEDate;
    private String messageTitle;
    private String messageInformation;
    private String messageState;

    @Id
    @Column(name = "message_ID")
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Basic
    @Column(name = "message_send_ID")
    public String getMessageSendId() {
        return messageSendId;
    }

    public void setMessageSendId(String messageSendId) {
        this.messageSendId = messageSendId;
    }

    @Basic
    @Column(name = "message_receive_ID")
    public String getMessageReceiveId() {
        return messageReceiveId;
    }

    public void setMessageReceiveId(String messageReceiveId) {
        this.messageReceiveId = messageReceiveId;
    }

    @Basic
    @Column(name = "message_type")
    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Basic
    @Column(name = "messag\r\ne_date")
    public Timestamp getMessagEDate() {
        return messagEDate;
    }

    public void setMessagEDate(Timestamp messagEDate) {
        this.messagEDate = messagEDate;
    }

    @Basic
    @Column(name = "message_title")
    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    @Basic
    @Column(name = "message_information")
    public String getMessageInformation() {
        return messageInformation;
    }

    public void setMessageInformation(String messageInformation) {
        this.messageInformation = messageInformation;
    }

    @Basic
    @Column(name = "message_state")
    public String getMessageState() {
        return messageState;
    }

    public void setMessageState(String messageState) {
        this.messageState = messageState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntity that = (MessageEntity) o;

        if (messageId != that.messageId) return false;
        if (messageSendId != null ? !messageSendId.equals(that.messageSendId) : that.messageSendId != null)
            return false;
        if (messageReceiveId != null ? !messageReceiveId.equals(that.messageReceiveId) : that.messageReceiveId != null)
            return false;
        if (messageType != null ? !messageType.equals(that.messageType) : that.messageType != null) return false;
        if (messagEDate != null ? !messagEDate.equals(that.messagEDate) : that.messagEDate != null) return false;
        if (messageTitle != null ? !messageTitle.equals(that.messageTitle) : that.messageTitle != null) return false;
        if (messageInformation != null ? !messageInformation.equals(that.messageInformation) : that.messageInformation != null)
            return false;
        if (messageState != null ? !messageState.equals(that.messageState) : that.messageState != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = messageId;
        result = 31 * result + (messageSendId != null ? messageSendId.hashCode() : 0);
        result = 31 * result + (messageReceiveId != null ? messageReceiveId.hashCode() : 0);
        result = 31 * result + (messageType != null ? messageType.hashCode() : 0);
        result = 31 * result + (messagEDate != null ? messagEDate.hashCode() : 0);
        result = 31 * result + (messageTitle != null ? messageTitle.hashCode() : 0);
        result = 31 * result + (messageInformation != null ? messageInformation.hashCode() : 0);
        result = 31 * result + (messageState != null ? messageState.hashCode() : 0);
        return result;
    }
}
