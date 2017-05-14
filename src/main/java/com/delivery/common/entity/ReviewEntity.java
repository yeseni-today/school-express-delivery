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
    private ReviewType reviewType;
    private Timestamp reviewTime;
    private ReviewState state;
    private String information;

    private UserEntity user;


    @OneToOne(cascade = CascadeType.MERGE,targetEntity = UserEntity.class)
    @JoinColumn(name = "user_ID",insertable = false,updatable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public enum ReviewType{
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
    public ReviewType getReviewType() {
        return reviewType;
    }

    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }

    @Basic
    @Column(name = "review_time")
    public Timestamp getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Timestamp reviewTime) {
        this.reviewTime = reviewTime;
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
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReviewEntity that = (ReviewEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (managerId != null ? !managerId.equals(that.managerId) : that.managerId != null) return false;
        if (reviewType != null ? !reviewType.equals(that.reviewType) : that.reviewType != null) return false;
        if (reviewTime != null ? !reviewTime.equals(that.reviewTime) : that.reviewTime != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (information != null ? !information.equals(that.information) : that.information != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (managerId != null ? managerId.hashCode() : 0);
        result = 31 * result + (reviewType != null ? reviewType.hashCode() : 0);
        result = 31 * result + (reviewTime != null ? reviewTime.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (information != null ? information.hashCode() : 0);
        return result;
    }
}
