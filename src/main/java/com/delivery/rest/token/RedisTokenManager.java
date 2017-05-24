package com.delivery.rest.token;

import com.delivery.common.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author finderlo
 * @date 15/05/2017
 */
@Component
public class RedisTokenManager implements TokenManager {

    private Logger logger = LoggerFactory.getLogger(RedisTokenManager.class);

    private StringRedisTemplate redis;

    @Autowired
    public void setRedis(StringRedisTemplate redis) {
        this.redis = redis;
        redis.setKeySerializer(new JdkSerializationRedisSerializer());
    }

    @Override
    public TokenModel createToken(String uid) {
        String token = UUID.randomUUID().toString().replace("_", "");
        token = uid + "_" + token;
        logger.info("create token. uid:" + uid + "  token:" + token);
        TokenModel tokenModel = new TokenModel(uid, token);
        //set expire time
        redis.boundValueOps(uid).set(token, Constant.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return tokenModel;
    }

    @Override
    public boolean checkToken(TokenModel model) {
        if (model == null) {
            logger.warn("model is null");
            return false;
        }
        String token = redis.boundValueOps(model.getUid()).get();

        if (token == null || !token.equals(model.getToken())) {
            return false;
        }

        redis.boundValueOps(model.getUid()).expire(Constant.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        logger.info("check model is right, update the token expires time. uid :" + model.getUid());
        return true;
    }

    @Override
    public TokenModel getToken(String authentication) {
        if (authentication == null || authentication.length() == 0) {
            return null;
        }
        String[] param = authentication.split("_");
        if (param.length != 2) {
            logger.error("authentication is not format[uid_uuid]");
            return null;
        }

        String uid = param[0];
        return new TokenModel(uid, authentication);
    }


    @Override
    public void deleteToken(String userId) {
        redis.delete(userId);
    }
}
