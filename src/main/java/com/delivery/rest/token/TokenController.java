package com.delivery.rest.token;

import com.delivery.common.ErrorCode;
import com.delivery.common.Response;
import com.delivery.common.dao.UserDao;
import com.delivery.common.entity.UserEntity;
import com.delivery.common.util.Assert;
import com.delivery.config.annotation.Authorization;
import com.delivery.config.annotation.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.delivery.common.constant.HttpStatus.*;

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
            @RequestParam(defaultValue = "") String uid,
            @RequestParam(defaultValue = "") String phone,
            @RequestParam String password,
            @RequestParam(defaultValue = "0") int type) {
        if (uid.equals("") && phone.equals("")) {
            return Response.error(WRONG_AUGUMENT, "uid or phone parameter not exist");
        }

        UserEntity user;
        TokenModel tokenModel;

        if (!uid.equals("")) {
            user = userDao.findById(uid);
        } else {
            user = userDao.findByPhone(phone);
        }

        if (user == null ||
                !user.getPassword().equals(password)) {
            return Response.error(ErrorCode.PHONE_OR_PASSWORD_ERROR);
        }

        tokenModel = tokenManager.createToken(user.getUid());
        return Response.ok(tokenModel);

    }

    @DeleteMapping
    @Authorization
    public Response logout(@CurrentUser UserEntity user) {
        tokenManager.deleteToken(user.getUid());
        return Response.ok();
    }


}
