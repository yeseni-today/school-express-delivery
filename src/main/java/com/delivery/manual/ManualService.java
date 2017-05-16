//package com.delivery.manual;
//
//import com.delivery.common.ErrorCode;
//import com.delivery.common.SedException;
//import com.delivery.common.Response;
//import com.delivery.common.constant.Constant;
//import com.delivery.common.dao.ComplaintDao;
//import com.delivery.common.dao.ReviewDao;
//import com.delivery.common.entity.ComplaintEntity;
//import com.delivery.common.entity.ReviewEntity;
//import com.delivery.common.entity.UserEntity;
//import com.delivery.common.util.Util;
//import com.delivery.event.Event;
//import com.delivery.event.EventContext;
//import com.delivery.event.EventPublisher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.sql.Timestamp;
//import java.util.List;
//
//import static com.delivery.common.ErrorCode.MANUAL_COMPLAINT_HANDLE_PARAM_INTEGER_FORMAR_WRONG;
//import static com.delivery.common.constant.Constant.*;
//import static com.delivery.common.util.ManualUtil.getComplaintId;
//import static com.delivery.common.util.ManualUtil.getReviewId;
//import static com.delivery.common.util.UserUtil.checkAdmin;
//import static com.delivery.common.util.Util.*;
//
///**
// * @author finderlo
// * @date 17/04/2017
// */
//@Component
//public class ManualService implements ActionHandler, EventPublisher {
//
//
//    @Autowired
//    ReviewDao reviewDao;
//
//    @Autowired
//    ComplaintDao complaintDao;
//
//    Dispatcher dispatcher;
//
//    public void setDispatcher(Dispatcher dispatcher) {
//        this.dispatcher = dispatcher;
//    }
//
//
//    @Override
//    public boolean canHandleAction(Action action) {
//        return action.getType() == ActionType.MANUAL;
//    }
//
//    public enum ManualActionType {
//        applyUpgrade, applyDegrade, handleUpgrade, handleDegrade, showReviewToUser, showReviewToManager,
//        complaintFromUser, showComplainToManager, orderHandleComplain,showWaitHandleReviewsToManager
//    }
//
//    @Override
//    public Response execute(Action action) {
//        ManualActionType type = Util.getActionSubType(action, ManualActionType.class);
//        preExecute(action);
//        switch (type) {
//            case applyDegrade:
//                return applyDegrade(action);
//            case applyUpgrade:
//                return applyUpgrade(action);
//            case handleDegrade:
//                return handleDegrade(action);
//            case handleUpgrade:
//                return handleUpgrade(action);
//            case complaintFromUser:
//                return complaintFromUser(action);
//            case showReviewToUser:
//                return showReviewToUser(action);
//            case orderHandleComplain:
//                return handleOrderComplaint(action);
//            case showReviewToManager:
//                return showReviewToManager(action);
//            case showComplainToManager:
//                return showComplaintToManager(action);
//            case showWaitHandleReviewsToManager:
//                return showWaitHandleReviewsToManager(action);
//            default:
//                return Response.error(ErrorCode.MANUAL_WRONG_ACTION_TYPE);
//        }
//    }
//
//    private void preExecute(Action action) {
//        //审核单号，审核对象
//        String reviewId = getReviewId(action);
//        if (!reviewId.equals("")) {
//            ReviewEntity review = reviewDao.findById(reviewId);
//            action.put(REVIEW_ENTITY, review);
//        }
//
//        //申诉单号，审核对象
//        String complaintId = getComplaintId(action);
//        if (!reviewId.equals("")) {
//            ComplaintEntity review = complaintDao.findById(complaintId);
//            action.put(COMPLAINT_ENTITY, review);
//        }
//    }
//
//
//    /**
//     * 用户申请升级
//     *
//     * @param action 用户ID
//     *               0.检测用户是否已经存在审核类型为升级且审核状态为待审核状态
//     *               1.保存申请单数据 设置审核类型为1[升级] 设置申请状态为待审核
//     *               2.将用户提交的个人数据保存至Users表
//     * @author finderlo
//     * @date 17/04/2017
//     */
//    public Response applyUpgrade(Action action) {
//        try {
//            ReviewEntity review = ManualUtil.getReviewFromParamAndSave(action, reviewDao);
//            //event
//            EventContext context = new EventContext();
//            context.put(Constant.EVENT_REVIEW_ENTITY, review);
//            publish(Event.Manual_NewUpgradeEvent, context);
//            return Response.ok(review);
//        } catch (SedException e) {
//            return Response.error(e.getErrorCode());
//        }
//    }
//
//
//    /**
//     * 用户申请降级
//     *
//     * @param action 用户ID
//     *               1.保存申请单数据 设置审核类型为2[降级]设置申请状态为待审核
//     * @author finderlo
//     * @date 17/04/2017
//     */
//    public Response applyDegrade(Action action) {
//        //todo
//        return null;
//    }
//
//    /**
//     * 人工处理升级
//     *
//     * @param action 审核单ID
//     *               管理员ID
//     *               1.保存信息并修改审核状态通过/不通过
//     *               2.修改Users表的 身份 若通过设为代取人 不通过则不变
//     * @author finderlo
//     * @date 17/04/2017
//     */
//    public Response handleUpgrade(Action action) {
//        checkAdmin(action);
//        UserEntity admin = getUser(action);
//
//        String pass = (String) action.getOrDefault("pass", "false");
//        String info = (String) action.getOrDefault("review_info", " ");
//        ReviewEntity review = ManualUtil.getReview(action);
//
//        review.setManagerId(admin.getUid());
//        review.setRemark(info);
//        review.setTime(new Timestamp(System.currentTimeMillis()));
//
//        if (pass.toLowerCase().equals("true")) {
//            review.setState(ReviewEntity.ReviewState.SUCCESS);
//            review.getUser().setIdentity(UserEntity.Identity.REPLACEMENT);
//            reviewDao.update(review);
//
//            EventContext context = new EventContext();
//            context.put(Constant.EVENT_REVIEW_ENTITY, review);
//            publish(Event.UserUpgradeSuccessEvent, context);
//        } else {
//            review.setState(ReviewEntity.ReviewState.FAIL);
//            reviewDao.update(review);
//
//            EventContext context = new EventContext();
//            context.put(Constant.EVENT_REVIEW_ENTITY, review);
//            publish(Event.UserUpgradeFailEvent, context);
//        }
//
//        return Response.ok();
//    }
//
//
//    /**
//     * 人工处理降级
//     *
//     * @param action 审核单ID
//     *               管理员ID
//     *               1.保存信息并修改审核状态通过/不通过
//     *               2.修改Users表的 身份 若通过设为代取人 不通过则不变
//     * @author finderlo
//     * @date 17/04/2017
//     */
//    public Response handleDegrade(Action action) {
//        //todo
//        return null;
//    }
//
//    /**
//     * 向用户展示申请的具体情况和实时状态
//     *
//     * @param action 用户ID
//     *               1.根据用户ID查出所有的审核单
//     *               2.返回一个包含这些审核单的list
//     * @author lx
//     * @date 2017/4/22
//     */
//    public Response showReviewToUser(Action action) {
//        UserEntity user = getUser(action);
//        List<ReviewEntity> reviews = reviewDao.findByUserId(user.getUid());
//        return Response.ok(reviews);
//    }
//
//    /**
//     * 向管理员展示申请的具体内容
//     *
//     * @param action 用户ID
//     *               1.根据用户ID查出待审核的审核单
//     *               2.根据用户ID查出用户的详细信息（Users表）
//     *               3.返回审核单及用户详细信息
//     * @author lx
//     * @date 2017/4/22
//     */
//    public Response showReviewsToManager(Action action) {
//        checkAdmin(action);
//        String userId = ManualUtil.getReviewUserId(action);
//        List<ReviewEntity> reviews = reviewDao.findByUserId(userId);
//        //todo 返回信息含有信用值
//        return Response.ok(reviews);
//    }
//
//    /**
//     * 向管理员展示申请的具体内容
//     *
//     * @param action 用户ID
//     *               1.根据用户ID查出待审核的审核单
//     *               2.根据用户ID查出用户的详细信息（Users表）
//     *               3.返回审核单及用户详细信息
//     * @author lx
//     * @date 2017/4/22
//     */
//    public Response showReviewToManager(Action action) {
//        checkAdmin(action);
//        String userId = ManualUtil.getReviewUserId(action);
//        List<ReviewEntity> reviews = reviewDao.findByUserId(userId);
//        //todo 返回信息含有信用值
//        return Response.ok(reviews);
//    }
//
//    public Response showWaitHandleReviewsToManager(Action action) {
//        checkAdmin(action);
//        List<ReviewEntity> reviews = reviewDao.findByState(ReviewEntity.ReviewState.WAIT_HANDLE);
//        return Response.ok(reviews);
//    }
//
//    /**
//     * 用户申诉一个订单
//     *
//     * @param action 订单号
//     *               申诉申请人ID（complaint表中的user_ID）
//     *               0.检测订单是否已经在申诉中 如果在则创建失败
//     *               1.创建一个申诉单
//     *               2.修改订单状态为申诉中
//     * @author finderlo
//     * @date 17/04/2017
//     */
//    public Response complaintFromUser(Action action) {
//        try {
//            String orderId = (String) action.getOrDefault("order_id", null);
//            checkNull(orderId, ErrorCode.MANUAL_COMPLAINT_CREATE_ORDERID_NULL);
//            if (!complaintDao.findByOrderId(orderId).isEmpty()) {
//                return Response.error(ErrorCode.MANUAL_COMPLAINT_CREATE_ORDER_ALEADY_COMPLAIN);
//            }
//
//            ComplaintEntity complaint = ManualUtil.getComplaintFromParamAndSave(action, complaintDao);
//            //event
//            EventContext context = new EventContext();
//            context.put(Constant.EVENT_COMPLAINT_ENTITY, complaint);
//            publish(Event.Manual_NewComplaint, context);
//            return Response.ok(complaint);
//        } catch (SedException e) {
//            return Response.error(e.getErrorCode());
//        }
//    }
//
//
//    /**
//     * 向管理员展示申诉单详情
//     * <p>
//     * 1.查询所有待处理的申诉单信息并返回
//     *
//     * @author lx
//     * @date 2017/4/22
//     */
//    public Response showComplaintToManager(Action action) {
//        List<ComplaintEntity> waitHandle = complaintDao.findByWaitDeal();
//        return Response.ok(waitHandle);
//    }
//
//
//    /**
//     * 人工处理一个订单申诉，成功或者失败，发布相对应事件
//     *
//     * @param action 申诉单号
//     *               管理员号
//     *               申诉处理结果（用于修改订单状态）（具体见状态转换图的申诉）
//     *               1.保存申诉单的修改信息
//     *               2.修改订单状态
//     *               3.发布对应事件通知双方（发送个消息）
//     * @author finderlo
//     * @date 17/04/2017
//     */
//    public Response handleOrderComplaint(Action action) {
//        //todo
//        checkAdmin(action);
//        UserEntity admin = getUser(action);
//
//        String creditStr = (String) action.get("complaint_result_credit");
//        String stateStr = (String) action.get("complaint_result_order_state");
//
//        int creditInt = 0;
//        int stateInt = 0;
//        try {
//            creditInt = Integer.parseInt(creditStr);
//            stateInt = Integer.parseInt(stateStr);
//            if (stateInt > OrderState.values().length - 1){
//                return Response.error(MANUAL_COMPLAINT_HANDLE_PARAM_INTEGER_FORMAR_WRONG);
//            }
//        } catch (Exception e) {
//            return Response.error(MANUAL_COMPLAINT_HANDLE_PARAM_INTEGER_FORMAR_WRONG);
//        }
//
//
//        ComplaintEntity complaint = ManualUtil.getComplaint(action);
//        complaint.setState(ComplaintEntity.OrderState.COMPLETE);
//        String result = ManualUtil.getResult(action);
//        complaint.setResult(result);
//        complaint.setManagerId(admin.getUid());
//        complaintDao.update(complaint);
//
//        EventContext context = new EventContext();
//        context.put(EVENT_COMPLAINT_ENTITY, complaint);
//        context.put(EVENT_COMPLAINT_RESULT_CREDIT_VALUE_INT, creditInt);
//        context.put(EVENT_COMPLAINT_RESULT_ORDER_STATE_INT, stateInt);
//        publish(Event.ComplaintResultEvent, context);
//        return Response.ok();
//    }
//
//    /**
//     * 发布事件
//     *
//     * @author Ticknick Hou
//     * @date 17/04/2017
//     */
//    private void publish(Event event, EventContext context) {
//        dispatcher.getEventManager().publish(event, context);
//    }
//}
