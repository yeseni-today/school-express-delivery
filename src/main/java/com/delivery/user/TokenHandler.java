package com.delivery.user;

import com.delivery.common.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by finderlo on 15/04/2017.
 */
@Component
public class TokenHandler {

    private Map<String, UserEntity> tokens = new HashMap<>();

    /**
     * 判断Token是否正确，可以判断这个Token是否已经登陆
     *
     * @author finderlo
     */
    public boolean isRight(String token) {
        return tokens.containsKey(token);
    }

    /**
     * 根据Token，来获得用户信息
     *
     * @author finderlo
     */
    public UserEntity getUser(String token) {
        return tokens.get(token);
    }

    /**
     * 根据用户生成Token，并将Token保存起来（判断是否登陆）
     *
     * @author finderlo
     */
    public String getTokenAndLogin(UserEntity userEntity) {
        StringBuilder builder = new StringBuilder();
        builder.append(userEntity.getUserId()).append("%%").append(userEntity.getUserName());
        tokens.put(builder.toString(), userEntity);
        return builder.toString();
    }


}
