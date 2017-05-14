package com.delivery.common.entity;

import com.google.gson.Gson;

import javax.persistence.*;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "users", schema = "sed", catalog = "")
public class UserEntity {
    private String id;
    private String name;
    private String phone;
    private String password;
    private UserType identity;
    private String schoolCard;
    private String idCard;
    private String photo;
    private String aliPay;
    private String sex;
    private String schoolName;
    private String schoolAddress;

    public enum UserType{
        SYSTEM,RECIPIENT,REPLACEMENT,ADMINSTARTE
    }

    @Id
    @Column(name = "user_ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "user_phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "user_password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "user_identity")
    public UserType getIdentity() {
        return identity;
    }

    public void setIdentity(UserType identity) {
        this.identity = identity;
    }

    @Basic
    @Column(name = "user_schoolcard")
    public String getSchoolCard() {
        return schoolCard;
    }

    public void setSchoolCard(String schoolCard) {
        this.schoolCard = schoolCard;
    }

    @Basic
    @Column(name = "user_idcard")
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Basic
    @Column(name = "user_photo")
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Basic
    @Column(name = "user_alipay")
    public String getAliPay() {
        return aliPay;
    }

    public void setAliPay(String aliPay) {
        this.aliPay = aliPay;
    }

    @Basic
    @Column(name = "user_sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "user_schoolname")
    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Basic
    @Column(name = "user_schooladdress")
    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (identity != null ? !identity.equals(that.identity) : that.identity != null) return false;
        if (schoolCard != null ? !schoolCard.equals(that.schoolCard) : that.schoolCard != null)
            return false;
        if (idCard != null ? !idCard.equals(that.idCard) : that.idCard != null) return false;
        if (photo != null ? !photo.equals(that.photo) : that.photo != null) return false;
        if (aliPay != null ? !aliPay.equals(that.aliPay) : that.aliPay != null) return false;
        if (sex != null ? !sex.equals(that.sex) : that.sex != null) return false;
        if (schoolName != null ? !schoolName.equals(that.schoolName) : that.schoolName != null)
            return false;
        if (schoolAddress != null ? !schoolAddress.equals(that.schoolAddress) : that.schoolAddress != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (identity != null ? identity.hashCode() : 0);
        result = 31 * result + (schoolCard != null ? schoolCard.hashCode() : 0);
        result = 31 * result + (idCard != null ? idCard.hashCode() : 0);
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + (aliPay != null ? aliPay.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (schoolName != null ? schoolName.hashCode() : 0);
        result = 31 * result + (schoolAddress != null ? schoolAddress.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
