package com.delivery.common.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "review", schema = "sed", catalog = "")
public class ReviewEntity {
    private String reviewId;
    private String userId;
    private String managerId;
    private String reviewType;
    private Timestamp reviewTime;
    private String reviewState;
    private String revieWInformation;

    @Id
    @Column(name = "review_ID")
    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
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
    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
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
    public String getReviewState() {
        return reviewState;
    }

    public void setReviewState(String reviewState) {
        this.reviewState = reviewState;
    }

    @Basic
    @Column(name = "revie\r\nw_information")
    public String getRevieWInformation() {
        return revieWInformation;
    }

    public void setRevieWInformation(String revieWInformation) {
        this.revieWInformation = revieWInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReviewEntity that = (ReviewEntity) o;

        if (reviewId != null ? !reviewId.equals(that.reviewId) : that.reviewId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (managerId != null ? !managerId.equals(that.managerId) : that.managerId != null) return false;
        if (reviewType != null ? !reviewType.equals(that.reviewType) : that.reviewType != null) return false;
        if (reviewTime != null ? !reviewTime.equals(that.reviewTime) : that.reviewTime != null) return false;
        if (reviewState != null ? !reviewState.equals(that.reviewState) : that.reviewState != null) return false;
        if (revieWInformation != null ? !revieWInformation.equals(that.revieWInformation) : that.revieWInformation != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = reviewId != null ? reviewId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (managerId != null ? managerId.hashCode() : 0);
        result = 31 * result + (reviewType != null ? reviewType.hashCode() : 0);
        result = 31 * result + (reviewTime != null ? reviewTime.hashCode() : 0);
        result = 31 * result + (reviewState != null ? reviewState.hashCode() : 0);
        result = 31 * result + (revieWInformation != null ? revieWInformation.hashCode() : 0);
        return result;
    }
}
