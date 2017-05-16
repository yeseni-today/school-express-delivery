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
    private Integer value;
    private String remark;
    private int id;

    private UserEntity user;

    @ManyToOne(cascade = CascadeType.MERGE,targetEntity = UserEntity.class)
    @JoinColumn(name = "user_ID",insertable = false,updatable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

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
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Basic
    @Column(name = "event_information")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (event != null ? event.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }
}
