package com.delivery.user;

import com.delivery.common.entity.UsersEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by finderlo on 15/04/2017.
 */
@Component
public class TokenHandler {

    private Map<String,UsersEntity> tokens = new HashMap<>();

    /**
     * 判断Token是否正确，可以判断这个Token是否已经登陆
     */
    public boolean isRight(String token){
        return tokens.containsKey(token);
    }

    /**
     * 根据Token，来获得用户信息
     */
    public UsersEntity getUser(String token){
        return tokens.get(token);
    }

    /**
     * 根据用户生成Token，并将Token保存起来（判断是否登陆）
     */
    public String getTokenAndLogin(UsersEntity usersEntity) {
        StringBuilder builder = new StringBuilder();
        builder.append(usersEntity.getUserId()).append("%%").append(usersEntity.getUserName());
        tokens.put(builder.toString(),usersEntity);
        return builder.toString();
    }


}
