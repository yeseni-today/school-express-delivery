package com.delivery.rest.token;

import com.delivery.common.ErrorCode;
import com.delivery.common.Response;
import com.delivery.common.dao.UserDao;
import com.delivery.common.entity.UserEntity;
import com.delivery.config.annotation.Authorization;
import com.delivery.config.annotation.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author finderlo
 * @date 15/05/2017
 */
@RestController
@RequestMapping("/tokens")
public class TokenController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenManager tokenManager;

    /**
     * 登陆，缺少必选参数时，返回400HTTP状态码Status
     *
     * @author Ticknick Hou
     * @date 15/05/2017
     */
    @PostMapping
    public Response login(
            @RequestParam String phone,
            @RequestParam String password) {

        UserEntity user = userDao.findByPhone(phone);
        if (user == null ||
                !user.getPassword().equals(password)) {
            return Response.error(ErrorCode.PHONE_OR_PASSWORD_ERROR);
        }

        TokenModel tokenModel = tokenManager.createToken(user.getUid());
        return Response.ok(tokenModel);
    }

    @DeleteMapping
    @Authorization
    public Response logout(@CurrentUser UserEntity user) {
        tokenManager.deleteToken(user.getUid());
        return Response.ok();
    }


}
