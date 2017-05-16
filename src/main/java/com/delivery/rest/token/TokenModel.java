package com.delivery.rest.token;

/**
 * @author finderlo
 * @date 15/05/2017
 */
public class TokenModel {

    private String uid;

    private String token;

    public TokenModel(String uid,String token){
        this.uid = uid;
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
