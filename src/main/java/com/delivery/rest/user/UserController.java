package com.delivery.rest.user;

import com.delivery.common.ErrorCode;
import com.delivery.common.Response;
import com.delivery.common.dao.ReviewDao;
import com.delivery.common.dao.UserDao;
import com.delivery.common.entity.UserEntity;
import com.delivery.config.annotation.AdminAuthorization;
import com.delivery.config.annotation.Authorization;
import com.delivery.config.annotation.CurrentUser;
import com.delivery.event.Event;
import com.delivery.event.EventManager;
import com.delivery.event.context.UserRegisterEventContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;


/**
 * @author finderlo
 * @date 15/05/2017
 */
@RestController()
@RequestMapping("/users")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserDao userDao;


    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private EventManager eventManager;


    @PostMapping
    public Response register(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String sex,
            @RequestParam String password,
            @RequestParam String school_card,
            @RequestParam String school_name,
            @RequestParam String school_address
    ) {
        logger.info("/users  POST");
        UserEntity user = new UserEntity();
        //用户填写的参数:name\phone\password\schoolcard\sex\schoolname\schooladdress
        if (userDao.findByPhone(phone) != null) {
            return Response.error(ErrorCode.USER_REGISTER_DEP_PHONE);
        }
        user.setName(name);
        user.setPhone(phone);
        user.setSex(sex);
        user.setPassword(password);
        user.setSchoolCard(school_card);
        user.setSchoolName(school_name);
        user.setSchoolAddress(school_address);
        //默认参数
        user.setIdentity(UserEntity.Identity.RECIPIENT);
        user.setUid(userDao.newUsersId());
        userDao.save(user);
        logger.info(user.toString());

        //发布事件
        UserRegisterEventContext context = new UserRegisterEventContext(user);
        eventManager.publish(Event.UserRegisterEvent, context);
        return Response.ok(user);
    }

    @GetMapping
    @Authorization
    public Response find(
            @RequestParam String phone) {
        Assert.notNull(phone, "phone can not be empty");
        return Response.ok(userDao.findByPhone(phone));
    }

    @GetMapping("/{uid}")
    @AdminAuthorization
    public Response findOne(
            @PathVariable String uid) {
        return Response.ok(userDao.findById(uid));
    }


    @PutMapping("/token")
    @Authorization
    public Response modify(
            @CurrentUser UserEntity user,
            @RequestParam String school_card,
            @RequestParam String id_card,
            @RequestParam(required = false) String photo,
            @RequestParam String alipay
    ) {
        Assert.notNull(school_card, "school_card can not be empty");
        Assert.notNull(id_card, "id_card can not be empty");
        Assert.notNull(alipay, "alipay can not be empty");


        user.setSchoolCard(school_card);
        user.setIdCard(id_card);
        user.setPhoto(photo);
        user.setAliPay(alipay);
        userDao.update(user);
        return Response.ok(user);
    }

    @GetMapping("/token")
    public Response own(
            @CurrentUser UserEntity user) {
        return Response.ok(user);
    }
}
