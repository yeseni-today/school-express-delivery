package com.delivery.common.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "orders", schema = "sed", catalog = "")
public class OrdersEntity {
    private String ordersId;
    private Timestamp ordersCreatetime;
    private Timestamp ordersFinishtime;
    private String ordersGrade;
    private Double ordersCost;
    private String ordersRemark;
    private String ordersState;
    private String recipientId;
    private String replacementId;
    private String expressName;
    private String expressCode;
    private Timestamp pickupTime;
    private String pickupAddress;
    private String pickupCode;
    private Timestamp deliveryTime;
    private String deliveryAddress;

    @Id
    @Column(name = "orders_ID")
    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    @Basic
    @Column(name = "orders_createtime")
    public Timestamp getOrdersCreatetime() {
        return ordersCreatetime;
    }

    public void setOrdersCreatetime(Timestamp ordersCreatetime) {
        this.ordersCreatetime = ordersCreatetime;
    }

    @Basic
    @Column(name = "orders_finishtime")
    public Timestamp getOrdersFinishtime() {
        return ordersFinishtime;
    }

    public void setOrdersFinishtime(Timestamp ordersFinishtime) {
        this.ordersFinishtime = ordersFinishtime;
    }

    @Basic
    @Column(name = "orders_grade")
    public String getOrdersGrade() {
        return ordersGrade;
    }

    public void setOrdersGrade(String ordersGrade) {
        this.ordersGrade = ordersGrade;
    }

    @Basic
    @Column(name = "orders_cost")
    public Double getOrdersCost() {
        return ordersCost;
    }

    public void setOrdersCost(Double ordersCost) {
        this.ordersCost = ordersCost;
    }

    @Basic
    @Column(name = "orders_remark")
    public String getOrdersRemark() {
        return ordersRemark;
    }

    public void setOrdersRemark(String ordersRemark) {
        this.ordersRemark = ordersRemark;
    }

    @Basic
    @Column(name = "orders_state")
    public String getOrdersState() {
        return ordersState;
    }

    public void setOrdersState(String ordersState) {
        this.ordersState = ordersState;
    }

    @Basic
    @Column(name = "recipient_ID")
    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    @Basic
    @Column(name = "replacement_ID")
    public String getReplacementId() {
        return replacementId;
    }

    public void setReplacementId(String replacementId) {
        this.replacementId = replacementId;
    }

    @Basic
    @Column(name = "express_name")
    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    @Basic
    @Column(name = "express_code")
    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    @Basic
    @Column(name = "pickup_time")
    public Timestamp getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Timestamp pickupTime) {
        this.pickupTime = pickupTime;
    }

    @Basic
    @Column(name = "pickup_address")
    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    @Basic
    @Column(name = "pickup_code")
    public String getPickupCode() {
        return pickupCode;
    }

    public void setPickupCode(String pickupCode) {
        this.pickupCode = pickupCode;
    }

    @Basic
    @Column(name = "delivery_time")
    public Timestamp getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Timestamp deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @Basic
    @Column(name = "delivery_address")
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdersEntity that = (OrdersEntity) o;

        if (ordersId != null ? !ordersId.equals(that.ordersId) : that.ordersId != null) return false;
        if (ordersCreatetime != null ? !ordersCreatetime.equals(that.ordersCreatetime) : that.ordersCreatetime != null)
            return false;
        if (ordersFinishtime != null ? !ordersFinishtime.equals(that.ordersFinishtime) : that.ordersFinishtime != null)
            return false;
        if (ordersGrade != null ? !ordersGrade.equals(that.ordersGrade) : that.ordersGrade != null) return false;
        if (ordersCost != null ? !ordersCost.equals(that.ordersCost) : that.ordersCost != null) return false;
        if (ordersRemark != null ? !ordersRemark.equals(that.ordersRemark) : that.ordersRemark != null) return false;
        if (ordersState != null ? !ordersState.equals(that.ordersState) : that.ordersState != null) return false;
        if (recipientId != null ? !recipientId.equals(that.recipientId) : that.recipientId != null) return false;
        if (replacementId != null ? !replacementId.equals(that.replacementId) : that.replacementId != null)
            return false;
        if (expressName != null ? !expressName.equals(that.expressName) : that.expressName != null) return false;
        if (expressCode != null ? !expressCode.equals(that.expressCode) : that.expressCode != null) return false;
        if (pickupTime != null ? !pickupTime.equals(that.pickupTime) : that.pickupTime != null) return false;
        if (pickupAddress != null ? !pickupAddress.equals(that.pickupAddress) : that.pickupAddress != null)
            return false;
        if (pickupCode != null ? !pickupCode.equals(that.pickupCode) : that.pickupCode != null) return false;
        if (deliveryTime != null ? !deliveryTime.equals(that.deliveryTime) : that.deliveryTime != null) return false;
        if (deliveryAddress != null ? !deliveryAddress.equals(that.deliveryAddress) : that.deliveryAddress != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ordersId != null ? ordersId.hashCode() : 0;
        result = 31 * result + (ordersCreatetime != null ? ordersCreatetime.hashCode() : 0);
        result = 31 * result + (ordersFinishtime != null ? ordersFinishtime.hashCode() : 0);
        result = 31 * result + (ordersGrade != null ? ordersGrade.hashCode() : 0);
        result = 31 * result + (ordersCost != null ? ordersCost.hashCode() : 0);
        result = 31 * result + (ordersRemark != null ? ordersRemark.hashCode() : 0);
        result = 31 * result + (ordersState != null ? ordersState.hashCode() : 0);
        result = 31 * result + (recipientId != null ? recipientId.hashCode() : 0);
        result = 31 * result + (replacementId != null ? replacementId.hashCode() : 0);
        result = 31 * result + (expressName != null ? expressName.hashCode() : 0);
        result = 31 * result + (expressCode != null ? expressCode.hashCode() : 0);
        result = 31 * result + (pickupTime != null ? pickupTime.hashCode() : 0);
        result = 31 * result + (pickupAddress != null ? pickupAddress.hashCode() : 0);
        result = 31 * result + (pickupCode != null ? pickupCode.hashCode() : 0);
        result = 31 * result + (deliveryTime != null ? deliveryTime.hashCode() : 0);
        result = 31 * result + (deliveryAddress != null ? deliveryAddress.hashCode() : 0);
        return result;
    }
}
