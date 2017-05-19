package com.delivery.common.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "orders", schema = "sed", catalog = "")
public class OrderEntity {
    private String id;
    private Timestamp createTime;
    private Timestamp finishTime;
    private String grade;
    private Double price;
    private String remark;

    private OrderState state;

    private String recipientId;
    private String replacementId;


    private UserEntity recipient;

    private UserEntity replacement;

    private String expressName;
    private String expressCode;
    private Timestamp pickupTime;
    private String pickupAddress;
    private String pickupCode;
    private Timestamp deliveryTime;
    private String deliveryAddress;

    /**
     * 等待接单、已接单、已收件、已确认、待评价、已完成、已取消、申诉中、已赔偿
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public enum OrderState {


        // -1 代表完成订单

        WAIT_PAY(),

        //等待接单
        WAIT_ACCEPT(),

        //已接单
        ACCEPTED,

        //已收件，待送达
        TAKE_PARCEL_WAIT_DELIVERY,

        //已确认
        AFFIRMATIVE,

        //待评价 -1
        WAIT_COMMENT,

        //已完成 -1
        COMPLETED,

        //以取消
        CANCELED,

        //申诉中
        COMPLAINING,

        //已赔偿 -1
        COMPENSATION;

    }

    @OneToOne(cascade = CascadeType.MERGE, targetEntity = UserEntity.class)
    @JoinColumn(name = "recipient_ID", insertable = false, updatable = false)
    public UserEntity getRecipient() {
        return recipient;
    }

    public void setRecipient(UserEntity recipient) {
        this.recipient = recipient;
    }

    @OneToOne(cascade = CascadeType.MERGE, targetEntity = UserEntity.class)
    @JoinColumn(name = "replacement_ID", insertable = false, updatable = false)
    public UserEntity getReplacement() {
        return replacement;
    }

    public void setReplacement(UserEntity replacement) {
        this.replacement = replacement;
    }

    @Id
    @Column(name = "orders_ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "orders_createtime")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "orders_finishtime")
    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    @Basic
    @Column(name = "orders_grade")
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Basic
    @Column(name = "orders_cost")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "orders_remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "orders_state")
    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
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

        OrderEntity that = (OrderEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null)
            return false;
        if (finishTime != null ? !finishTime.equals(that.finishTime) : that.finishTime != null)
            return false;
        if (grade != null ? !grade.equals(that.grade) : that.grade != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
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
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (finishTime != null ? finishTime.hashCode() : 0);
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
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
