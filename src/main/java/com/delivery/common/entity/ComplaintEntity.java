package com.delivery.common.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "complaint", schema = "sed", catalog = "")
public class ComplaintEntity {
    private String complaintId;
    private String orders_ID;
    private String userId;
    private String managerId;
    private String complaintType;
    private Timestamp complaintTime;
    private String complaintState;
    private String complaintInformation;
    private String complaintResult;

    @Id
    @Column(name = "complaint_ID")
    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    @Basic
    @Column(name = "orders_ID")
    public String getOrders_ID() {
        return orders_ID;
    }

    public void setOrders_ID(String orders_ID) {
        this.orders_ID = orders_ID;
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
    @Column(name = "complaint_type")
    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    @Basic
    @Column(name = "complaint_time")
    public Timestamp getComplaintTime() {
        return complaintTime;
    }

    public void setComplaintTime(Timestamp complaintTime) {
        this.complaintTime = complaintTime;
    }

    @Basic
    @Column(name = "complaint_state")
    public String getComplaintState() {
        return complaintState;
    }

    public void setComplaintState(String complaintState) {
        this.complaintState = complaintState;
    }

    @Basic
    @Column(name = "complaint_information")
    public String getComplaintInformation() {
        return complaintInformation;
    }

    public void setComplaintInformation(String complaintInformation) {
        this.complaintInformation = complaintInformation;
    }

    @Basic
    @Column(name = "complaint_result")
    public String getComplaintResult() {
        return complaintResult;
    }

    public void setComplaintResult(String complaintResult) {
        this.complaintResult = complaintResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplaintEntity that = (ComplaintEntity) o;

        if (complaintId != null ? !complaintId.equals(that.complaintId) : that.complaintId != null) return false;
        if (orders_ID != null ? !orders_ID.equals(that.orders_ID) : that.orders_ID != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (managerId != null ? !managerId.equals(that.managerId) : that.managerId != null) return false;
        if (complaintType != null ? !complaintType.equals(that.complaintType) : that.complaintType != null)
            return false;
        if (complaintTime != null ? !complaintTime.equals(that.complaintTime) : that.complaintTime != null)
            return false;
        if (complaintState != null ? !complaintState.equals(that.complaintState) : that.complaintState != null)
            return false;
        if (complaintInformation != null ? !complaintInformation.equals(that.complaintInformation) : that.complaintInformation != null)
            return false;
        if (complaintResult != null ? !complaintResult.equals(that.complaintResult) : that.complaintResult != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = complaintId != null ? complaintId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (orders_ID != null ? orders_ID.hashCode() : 0);
        result = 31 * result + (managerId != null ? managerId.hashCode() : 0);
        result = 31 * result + (complaintType != null ? complaintType.hashCode() : 0);
        result = 31 * result + (complaintTime != null ? complaintTime.hashCode() : 0);
        result = 31 * result + (complaintState != null ? complaintState.hashCode() : 0);
        result = 31 * result + (complaintInformation != null ? complaintInformation.hashCode() : 0);
        result = 31 * result + (complaintResult != null ? complaintResult.hashCode() : 0);
        return result;
    }
}
