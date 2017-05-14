package com.delivery.common.entity;

import com.delivery.order.OrderState;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "orders_operation_log", schema = "sed", catalog = "")
public class OrderLogEntity {
    private String orderId;
    private OrderState state;
    private Timestamp changeTime;
    private int id;

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
    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
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
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (changeTime != null ? !changeTime.equals(that.changeTime) : that.changeTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (changeTime != null ? changeTime.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }
}
