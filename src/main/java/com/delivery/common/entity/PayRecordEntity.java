package com.delivery.common.entity;

import javax.persistence.*;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "payment_infomation", schema = "sed", catalog = "")
public class PayRecordEntity {
    private String id;
    private String orderId;
    private String type;
    private Double cost;
    private String state;
    private String channel;
    private String information;

    @Id
    @Column(name = "payment_ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "orders_id")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "payment_type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "payment_cost")
    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Basic
    @Column(name = "payment_state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Basic
    @Column(name = "payment_ channels")
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Basic
    @Column(name = "payment_information")
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

        PayRecordEntity that = (PayRecordEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (cost != null ? !cost.equals(that.cost) : that.cost != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (channel != null ? !channel.equals(that.channel) : that.channel != null)
            return false;
        if (information != null ? !information.equals(that.information) : that.information != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (channel != null ? channel.hashCode() : 0);
        result = 31 * result + (information != null ? information.hashCode() : 0);
        return result;
    }
}
