package com.delivery.common.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "review", schema = "sed", catalog = "")
public class ReviewEntity {
    private String id;
    private String userId;
    private String managerId;
    private Type type;
    private Timestamp time;
    private ReviewState state;
    private String remark;

    private UserEntity user;


    @OneToOne(cascade = CascadeType.MERGE,targetEntity = UserEntity.class)
    @JoinColumn(name = "user_ID",insertable = false,updatable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
        this.userId = user.getUid();
    }

    public enum Type {
        UPGRADE,DEGRADE,UPDATE_DATA
    }

    public enum ReviewState{
        WAIT_HANDLE,SUCCESS,FAIL
    }

    @Id
    @Column(name = "review_ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "manager_ID")
    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    @Basic
    @Column(name = "review_type")
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Basic
    @Column(name = "review_time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "review_state")
    public ReviewState getState() {
        return state;
    }

    public void setState(ReviewState state) {
        this.state = state;
    }

    @Basic
    @Column(name = "review_information")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReviewEntity that = (ReviewEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (managerId != null ? !managerId.equals(that.managerId) : that.managerId != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (managerId != null ? managerId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
}
