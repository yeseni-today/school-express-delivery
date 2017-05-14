package com.delivery.common.entity;

import javax.persistence.*;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "credit_record", schema = "sed", catalog = "")
public class CreditRecordEntity {
    private String userId;
    private String event;
    private Integer creditChange;
    private String eventInformation;
    private int id;

    @Basic
    @Column(name = "user_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "user_event")
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Basic
    @Column(name = "credit_change")
    public Integer getCreditChange() {
        return creditChange;
    }

    public void setCreditChange(Integer creditChange) {
        this.creditChange = creditChange;
    }

    @Basic
    @Column(name = "event_information")
    public String getEventInformation() {
        return eventInformation;
    }

    public void setEventInformation(String eventInformation) {
        this.eventInformation = eventInformation;
    }

    @Id
    @Column(name = "credit_ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreditRecordEntity that = (CreditRecordEntity) o;

        if (id != that.id) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (event != null ? !event.equals(that.event) : that.event != null) return false;
        if (creditChange != null ? !creditChange.equals(that.creditChange) : that.creditChange != null) return false;
        if (eventInformation != null ? !eventInformation.equals(that.eventInformation) : that.eventInformation != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (event != null ? event.hashCode() : 0);
        result = 31 * result + (creditChange != null ? creditChange.hashCode() : 0);
        result = 31 * result + (eventInformation != null ? eventInformation.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }
}
