package com.delivery.rest.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.delivery.common.Response;
import com.delivery.common.dao.OrderDao;
import com.delivery.common.dao.ReviewDao;
import com.delivery.common.dao.UserDao;
import com.delivery.common.entity.ComplaintEntity;
import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.ReviewEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.common.util.Assert;
import com.delivery.common.util.Util;
import com.delivery.config.annotation.AdminAuthorization;
import com.delivery.config.annotation.Authorization;
import com.delivery.config.annotation.CurrentUser;
import com.delivery.config.annotation.EnumParam;
import com.delivery.event.Event;
import com.delivery.event.EventManager;
import com.delivery.event.context.UserUpgradeEventContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.delivery.common.constant.HttpStatus.*;

/**
 * 将Reviews单独成类，作为独立的资源，主要负责对于review这个主体进行的操作
 *
 * @author finderlo
 * @date 15/05/2017
 */
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;


    @Autowired
    private EventManager eventManager;

    /**
     * 根据参数查询列表
     *
     * @author Ticknick Hou
     * @date 15/05/2017
     */
    @GetMapping
    @AdminAuthorization
    public Response find(
            @RequestParam(defaultValue = "") String uid,
            @RequestParam(defaultValue = "-1") int state
    ) {
        Set<ReviewEntity> reviews = new HashSet<>();
        if (!uid.equals("")) {
            reviews.addAll(reviewDao.findByUserId(uid));
        }
        if (state != -1 && state < ComplaintEntity.ComplaintState.values().length) {
            reviews.addAll(reviewDao.findByState(state));
        }
        return Response.ok(reviews);
    }

    @GetMapping("/token")
    @Authorization
    public Response findOwn(
            @CurrentUser UserEntity user) {
        return Response.ok(reviewDao.findByUserId(user.getUid()));
    }

    /**
     * 管理员处理审核单
     *
     * @author Ticknick Hou
     * @date 15/05/2017
     */
    @PutMapping("/{review_id}")
    @AdminAuthorization
    @Transactional(rollbackFor = Exception.class)
    public Response modify(
            @PathVariable String review_id,
            @EnumParam ReviewEntity.ReviewState result,
            @RequestParam String remark,
            @CurrentUser UserEntity admin
    ) {
        Assert.isTrue(!result.equals(ReviewEntity.ReviewState.WAIT_HANDLE), WRONG_AUGUMENT, "result must be success(1) or fail(2)");

        ReviewEntity review = reviewDao.findById(review_id);
        review.setState(result);
        review.setTime(Util.now());
        review.setRemark(remark);
        review.setManagerId(admin.getUid());

        reviewDao.update(review);

        if (review.getType().equals(ReviewEntity.ReviewType.UPGRADE)) {
            UserUpgradeEventContext context = new UserUpgradeEventContext(review.getUser(), review);
            if (result.equals(ReviewEntity.ReviewState.SUCCESS)) {
                eventManager.publish(Event.UserUpgradeSuccessEvent, context);
            } else eventManager.publish(Event.UserUpgradeFailEvent, context);
        }

        return Response.ok(review);
    }

    /**
     * 用户，提交审核单, 参数是审核单信息
     *
     * @author Ticknick Hou
     * @date 15/05/2017
     */
    @PostMapping
    @Authorization
    @Transactional(rollbackFor = Exception.class)
    public Response newReview(
            @CurrentUser UserEntity user,
            @EnumParam ReviewEntity.ReviewType type
    ) {
        Assert.notNull(type, WRONG_AUGUMENT, "review type can not be empty");
        if (type.equals(ReviewEntity.ReviewType.DEGRADE)) {
            return userDegrade(user);
        }

        Assert.isTrue(Util.isHaveEnoughInfoToUpgrade(user), FORBBID, "user only upgrade with enough profile");

        ReviewEntity review = new ReviewEntity();
        review.setUser(user);
        review.setUserId(user.getUid());
        review.setType(type);
        review.setState(ReviewEntity.ReviewState.WAIT_HANDLE);
        review.setTime(Util.now());

        String id = reviewDao.newId();
        review.setId(id);
        reviewDao.save(review);
        return Response.ok(review);
    }

    private Response userDegrade(UserEntity user) {
        Assert.isTrue(user.getIdentity().equals(UserEntity.UserIdentity.REPLACEMENT), "user already is recipient");
        Assert.isTrue(orderDao.findByReplacementId(user.getUid()).size() == 0, 403, "user have uncompleted order");
        return Response.ok();
    }
}
