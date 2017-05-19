package com.delivery.rest.order;

import com.delivery.common.Response;
import com.delivery.common.dao.*;
import com.delivery.common.entity.ComplaintEntity;
import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.common.util.Assert;
import com.delivery.common.util.Util;
import com.delivery.config.annotation.AdminAuthorization;
import com.delivery.config.annotation.Authorization;
import com.delivery.config.annotation.CurrentUser;
import com.delivery.config.annotation.EnumParam;
import com.delivery.event.Event;
import com.delivery.event.EventManager;
import com.delivery.event.context.ComplaintResultEventContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.delivery.common.constant.HttpStatus.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author finderlo
 * @date 15/05/2017
 */
@RestController
@RequestMapping("/complaints")
public class ComplaintController {


    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLogDao orderLogDao;

    @Autowired
    private EventManager eventManager;

    @Autowired
    private ComplaintDao complaintDao;

    /**
     * 根据参数查询列表,管理员
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
        Set<ComplaintEntity> complaints = new HashSet<>();
        if (!uid.equals("")) {
            complaints.addAll(complaintDao.findByUserId(uid));
        }
        if (state != -1 && state < ComplaintEntity.ComplaintType.values().length) {
            complaints.addAll(complaintDao.findByState(state));
        }
        return Response.ok(complaints);
    }

    /**
     * 获取TOKEN对应用户的所有申诉单
     *
     * @author Ticknick Hou
     * @date 16/05/2017
     */
    @GetMapping("/token")
    public Response findOwn(
            @CurrentUser UserEntity user) {
        return Response.ok(complaintDao.findByUserId(user.getUid()));
    }

    /**
     * 用户，提交申诉, 参数是申诉单信息
     *
     * @author Ticknick Hou
     * @date 15/05/2017
     */
    @PostMapping
    @Authorization
    @Transactional(rollbackFor = Exception.class)
    public Response newNompaint(
            @CurrentUser UserEntity user,
            @RequestParam String order_id,
            @EnumParam ComplaintEntity.ComplaintType type,
            @RequestParam(defaultValue = "default description") String description
    ) {
        Assert.isTrue(description.length() < 100, "description length should less than 100");
        Assert.notNull(type, WRONG_AUGUMENT, "review type can not be empty");
        Assert.notNull(orderDao.findById(order_id), WRONG_AUGUMENT, "wrong order id");
        Assert.isTrue(!orderDao.findById(order_id)
                        .getState().equals(OrderEntity.OrderState.COMPLAINING),
                "the order is in complaint state");

        ComplaintEntity complaint = new ComplaintEntity();
        complaint.setUser(user);
        complaint.setType(type);
        complaint.setOrderId(order_id);
        complaint.setUserId(user.getUid());
        complaint.setCreateTime(Util.now());
        complaint.setState(ComplaintEntity.ComplaintState.WAIT_DEAL);
        complaint.setDescription(description);

        String id = complaintDao.newId();
        complaint.setId(id);
        complaintDao.save(complaint);
        return Response.ok(complaint);
    }


    /**
     * 管理员处理申诉单
     *
     * @author Ticknick Hou
     * @date 15/05/2017
     */
    @PutMapping("/{id}")
    @AdminAuthorization
    @Transactional(rollbackFor = Exception.class)
    public Response modify(
            @PathVariable String id,
            @EnumParam OrderEntity.OrderState order_state,
            @RequestParam int credit_value,
            @RequestParam String result,
            @CurrentUser UserEntity admin
    ) {
        Assert.isTrue(result.length() < 10, "result length should less than 10");


        ComplaintEntity complaint = complaintDao.findById(id);

        Assert.notNull(complaint, WRONG_AUGUMENT, "wrong complaint id ");
        Assert.isTrue(!complaint.getState().equals(ComplaintEntity.ComplaintState.COMPLETE), WRONG_AUGUMENT, "complaint already complete");

        complaint.setState(ComplaintEntity.ComplaintState.COMPLETE);
        complaint.setResult(result);
        complaint.setManagerId(admin.getUid());

        OrderEntity order = complaint.getOrder();
        order.setState(order_state);
        complaintDao.update(complaint);
        orderDao.update(order);
        //发布审核单处理结果事件，此处信用机制和消息机制进行监听此事件,信用机制根据传过来的值进行修改
        ComplaintResultEventContext context = new ComplaintResultEventContext(complaint, credit_value, order_state);
        eventManager.publish(Event.ComplaintResultEvent, context);
        return Response.ok(complaint);
    }


}
