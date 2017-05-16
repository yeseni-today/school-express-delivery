package com.delivery.rest.token;

/**
 * @author finderlo
 * @date 15/05/2017
 */
public interface TokenManager {
    TokenModel createToken(String userId);

     boolean checkToken(TokenModel tokenModel);

    TokenModel getToken(String authentication);

    void deleteToken(String userId);

}
