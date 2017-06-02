package com.delivery.common.entity;


import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "orders_operation_log", schema = "sed", catalog = "")
public class OrderLogEntity {
    private String orderId;
    private OrderEntity.OrderState orderState;
    private Timestamp changeTime;
    private int id;

    private OrderEntity order;


    @OneToOne(cascade = CascadeType.DETACH,targetEntity = OrderEntity.class)
    @JoinColumn(name = "orders_ID",insertable = false,updatable = false)
    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    @Basic
    @Column(name = "orders_ID")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "state_type")
    public OrderEntity.OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderEntity.OrderState orderState) {
        this.orderState = orderState;
    }

    @Basic
    @Column(name = "state_change_time")
    public Timestamp getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    @Id
    @Column(name = "orders_log_ID")
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

        OrderLogEntity that = (OrderLogEntity) o;

        if (id != that.id) return false;
        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (orderState != null ? !orderState.equals(that.orderState) : that.orderState != null) return false;
        if (changeTime != null ? !changeTime.equals(that.changeTime) : that.changeTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (orderState != null ? orderState.hashCode() : 0);
        result = 31 * result + (changeTime != null ? changeTime.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }
}
