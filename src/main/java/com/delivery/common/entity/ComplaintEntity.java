package com.delivery.common.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "complaint", schema = "sed", catalog = "")
public class ComplaintEntity {
    private String id;
    private String orderId;
    private String userId;
    private String managerId;
    private ComplaintType type;
    private Timestamp createTime;
    private ComplaintState state;
    private String description;
    private String result;

    private OrderEntity order;
    private UserEntity user;
//    private ManagerEntity manager;


    public enum ComplaintState {
        WAIT_DEAL,
        COMPLETE
    }

    public enum ComplaintType {
        ReplacementDeliveryOnTime,
        ReplacementNotSendDelivery,
        ReceiverInfoError,
        BigOrSmallDelivery,
        Other
    }


    @OneToOne(cascade = CascadeType.DETACH, targetEntity = OrderEntity.class)
    @JoinColumn(name = "orders_ID", insertable = false, updatable = false)
    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    @OneToOne(cascade = CascadeType.DETACH, targetEntity = UserEntity.class)
    @JoinColumn(name = "user_ID", insertable = false, updatable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

//    @OneToOne(cascade = CascadeType.DETACH, targetEntity = ManagerEntity.class)
//    @JoinColumn(name = "manager_ID", insertable = false, updatable = false)
//    public ManagerEntity getManager() {
//        return manager;
//    }
//
//    public void setManager(ManagerEntity manager) {
//        this.manager = manager;
//    }

    @Id
    @Column(name = "complaint_ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "orders_ID")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orders_ID) {
        this.orderId = orders_ID;
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
    public ComplaintType getType() {
        return type;
    }

    public void setType(ComplaintType type) {
        this.type = type;
    }

    @Basic
    @Column(name = "complaint_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "complaint_state")
    public ComplaintState getState() {
        return state;
    }

    public void setState(ComplaintState state) {
        this.state = state;
    }

    @Basic
    @Column(name = "complaint_information")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "complaint_result")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplaintEntity that = (ComplaintEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (managerId != null ? !managerId.equals(that.managerId) : that.managerId != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null)
            return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null)
            return false;
        if (state != null ? !state.equals(that.state) : that.state != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (result != null ? !result.equals(that.result) : that.result != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        result = 31 * result + (managerId != null ? managerId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (this.result != null ? this.result.hashCode() : 0);
        return result;
    }
}
