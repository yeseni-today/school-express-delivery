package com.delivery.manual;

import com.delivery.common.ErrorCode;
import com.delivery.common.SedException;
import com.delivery.common.action.Action;
import com.delivery.common.constant.Constant;
import com.delivery.common.dao.ComplainDao;
import com.delivery.common.dao.ReviewDao;
import com.delivery.common.entity.ComplaintEntity;
import com.delivery.common.entity.ReviewEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.common.util.Util;
import com.delivery.dispatch.Dispatcher;

import java.sql.Timestamp;

import static com.delivery.common.ErrorCode.*;
import static com.delivery.common.constant.Constant.COMPLAINT_ENTITY;
import static com.delivery.common.constant.Constant.REVIEW_ENTITY;
import static com.delivery.common.constant.Constant.REVIEW_USER_ID;

/**
 * @author finderlo
 * @date 11/05/2017
 */
public class ManualUtil {

    /**
     * 从参数中获取信息生成ReviewEntity并且保存
     *
     * @author finderlo
     * @date 22/04/2017
     */
    public static synchronized ReviewEntity getReviewFromParamAndSave(Action action, ReviewDao reviewDao) {
        ReviewEntity review = null;

        try {
            review = new ReviewEntity();

            ReviewEntity.ReviewType reviewType = Util.getEnumParamByIntger(action, "review_type", ReviewEntity.ReviewType.class);
            //提交时间
            Timestamp review_time = new Timestamp(System.currentTimeMillis());
            ReviewEntity.ReviewState reviewState = ReviewEntity.ReviewState.WAIT_HANDLE;

            //通过TOKEN自动获取
            UserEntity user = Util.getUser(action);

            String compus_id = (String) action.getOrDefault("school_card", null);
            String idcard = (String) action.getOrDefault("id_card", null);
            String photo = "  ";

            String alipay = (String) action.getOrDefault("alipay", null);

            Util.checkNull(compus_id, ErrorCode.MANUAL_COMPLAINT_CREATE_COMPUSID_NULL);
            Util.checkNull(idcard, ErrorCode.MANUAL_REVIEW_CREATE_IDCARD_NULL);
            Util.checkNull(alipay, ErrorCode.MANUAL_REVIEW_CREATE_ALIPAY_NULL);

            user.setSchoolCard(compus_id);
            user.setIdCard(idcard);
            user.setPhoto(photo);
            user.setAliPay(alipay);

            review.setReviewTime(review_time);
            review.setUser(user);
            review.setUserId(user.getId());
            review.setReviewType(reviewType);
            review.setState(reviewState);


            String reviewId = reviewDao.newOrderId();
            review.setId(reviewId);
            reviewDao.save(review);
            return review;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new SedException(SYSTEM_TIME_FORMAT_ERROR);
        } catch (SedException e1) {
            e1.printStackTrace();
            throw e1;
        } catch (Exception e) {
            e.printStackTrace();
            if (review == null) {
                throw new SedException(MANUAL_REVIEW_CREATE_FAILED_UNKNOW_ERROR);
            } else if (review.getId() == null) {
                e.printStackTrace();
                throw new SedException(ErrorCode.MANUAL_REVIEW_CREATE_FAILED_UNKNOW_ERROR);
            } else {
                String id = review.getId();
                ReviewEntity e1 = reviewDao.findById(id);
                reviewDao.delete(e1);
                e.printStackTrace();
                throw new SedException(ErrorCode.MANUAL_REVIEW_CREATE_FAILED_UNKNOW_ERROR);
            }
        }
    }

    /**
     * 从参数中获取信息生成ComplaintEntity并且保存
     *
     * @author finderlo
     * @date 22/04/2017
     */
    public static synchronized ComplaintEntity getComplaintFromParamAndSave(Action action, ComplainDao complainDao) {
        ComplaintEntity complaint = null;

        try {
            complaint = new ComplaintEntity();


            String order_id = (String) action.getOrDefault("order_id", null);
            ComplaintEntity.ComplainType complainType =
                    Util.getEnumParamByIntger(
                            action, "complaint_type", ComplaintEntity.ComplainType.class);
            Timestamp complaintTime = new Timestamp(System.currentTimeMillis());
            ComplaintEntity.ComplainState complainState = ComplaintEntity.ComplainState.WAIT_DEAL;

            String description = (String) action.getOrDefault("complaint_description", "");
            String result = "Wait the service handle it";

            //通过TOKEN自动获取
            UserEntity user = Util.getUser(action);

            Util.checkNull(order_id, ErrorCode.MANUAL_COMPLAINT_CREATE_COMPUSID_NULL);
            Util.checkNull(complainType, ErrorCode.MANUAL_COMPLAINT_CREATE_IDCARD_NULL);

            complaint.setOrderId(order_id);
            complaint.setUserId(user.getId());
            complaint.setType(complainType);
            complaint.setCreateTime(complaintTime);
            complaint.setDescription(description);
            complaint.setResult(result);


            String id = complainDao.newComplaintId();
            complaint.setId(id);
            complainDao.save(complaint);
            return complaint;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new SedException(SYSTEM_TIME_FORMAT_ERROR);
        } catch (SedException e1) {
            e1.printStackTrace();
            throw e1;
        } catch (Exception e) {
            e.printStackTrace();
            if (complaint == null) {
                throw new SedException(MANUAL_COMPLAINT_CREATE_FAILED_UNKNOW_ERROR);
            } else if (complaint.getId() == null) {
                e.printStackTrace();
                throw new SedException(ErrorCode.MANUAL_REVIEW_CREATE_FAILED_UNKNOW_ERROR);
            } else {
                String id = complaint.getId();
                ComplaintEntity e1 = complainDao.findById(id);
                complainDao.delete(e1);
                e.printStackTrace();
                throw new SedException(ErrorCode.MANUAL_REVIEW_CREATE_FAILED_UNKNOW_ERROR);
            }
        }
    }


    public static void updateUserInfo(UserEntity user) {
        //todo
    }

    public static void updateUser(UserEntity user, Dispatcher dispatcher) {
        //todo
    }

    public static ReviewEntity getReview(Action action) {
        return (ReviewEntity) action.getOrDefault(REVIEW_ENTITY, null);
    }

    public static ComplaintEntity getComplaint(Action action) {
        return (ComplaintEntity) action.getOrDefault(COMPLAINT_ENTITY, null);
    }

    public static String getReviewUserId(Action action) {
        return (String) action.getOrDefault(REVIEW_USER_ID, null);
    }

    public static String getResult(Action action) {
        return (String) action.getOrDefault(Constant.COMPLAINT_RESULT, null);
    }

    public static int getCreditFromActionParam(Action action) {
        String val = (String) action.getOrDefault(Constant.COMPLAINT_RESULT_CREDIT, "0");
        return Integer.parseInt(val);
    }
}
