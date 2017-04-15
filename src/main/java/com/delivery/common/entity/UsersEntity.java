package com.delivery.common.entity;

import javax.persistence.*;

/**
 * Created by finderlo on 2017/4/7.
 */
@Entity
@Table(name = "users", schema = "sed", catalog = "")
public class UsersEntity {
    private String userId;
    private String userName;
    private String userPhone;
    private String userPassword;
    private String userIdentity;
    private String userSchoolcard;
    private String userIdcard;
    private String userPhoto;
    private String userAlipay;
    private String userSex;
    private String userSchoolname;
    private String userSchooladdress;

    @Id
    @Column(name = "user_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "user_phone")
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Basic
    @Column(name = "user_password")
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Basic
    @Column(name = "user_identity")
    public String getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
    }

    @Basic
    @Column(name = "user_schoolcard")
    public String getUserSchoolcard() {
        return userSchoolcard;
    }

    public void setUserSchoolcard(String userSchoolcard) {
        this.userSchoolcard = userSchoolcard;
    }

    @Basic
    @Column(name = "user_idcard")
    public String getUserIdcard() {
        return userIdcard;
    }

    public void setUserIdcard(String userIdcard) {
        this.userIdcard = userIdcard;
    }

    @Basic
    @Column(name = "user_photo")
    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @Basic
    @Column(name = "user_alipay")
    public String getUserAlipay() {
        return userAlipay;
    }

    public void setUserAlipay(String userAlipay) {
        this.userAlipay = userAlipay;
    }

    @Basic
    @Column(name = "user_sex")
    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    @Basic
    @Column(name = "user_schoolname")
    public String getUserSchoolname() {
        return userSchoolname;
    }

    public void setUserSchoolname(String userSchoolname) {
        this.userSchoolname = userSchoolname;
    }

    @Basic
    @Column(name = "user_schooladdress")
    public String getUserSchooladdress() {
        return userSchooladdress;
    }

    public void setUserSchooladdress(String userSchooladdress) {
        this.userSchooladdress = userSchooladdress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersEntity that = (UsersEntity) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (userPhone != null ? !userPhone.equals(that.userPhone) : that.userPhone != null) return false;
        if (userPassword != null ? !userPassword.equals(that.userPassword) : that.userPassword != null) return false;
        if (userIdentity != null ? !userIdentity.equals(that.userIdentity) : that.userIdentity != null) return false;
        if (userSchoolcard != null ? !userSchoolcard.equals(that.userSchoolcard) : that.userSchoolcard != null)
            return false;
        if (userIdcard != null ? !userIdcard.equals(that.userIdcard) : that.userIdcard != null) return false;
        if (userPhoto != null ? !userPhoto.equals(that.userPhoto) : that.userPhoto != null) return false;
        if (userAlipay != null ? !userAlipay.equals(that.userAlipay) : that.userAlipay != null) return false;
        if (userSex != null ? !userSex.equals(that.userSex) : that.userSex != null) return false;
        if (userSchoolname != null ? !userSchoolname.equals(that.userSchoolname) : that.userSchoolname != null)
            return false;
        if (userSchooladdress != null ? !userSchooladdress.equals(that.userSchooladdress) : that.userSchooladdress != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userPhone != null ? userPhone.hashCode() : 0);
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        result = 31 * result + (userIdentity != null ? userIdentity.hashCode() : 0);
        result = 31 * result + (userSchoolcard != null ? userSchoolcard.hashCode() : 0);
        result = 31 * result + (userIdcard != null ? userIdcard.hashCode() : 0);
        result = 31 * result + (userPhoto != null ? userPhoto.hashCode() : 0);
        result = 31 * result + (userAlipay != null ? userAlipay.hashCode() : 0);
        result = 31 * result + (userSex != null ? userSex.hashCode() : 0);
        result = 31 * result + (userSchoolname != null ? userSchoolname.hashCode() : 0);
        result = 31 * result + (userSchooladdress != null ? userSchooladdress.hashCode() : 0);
        return result;
    }
}
