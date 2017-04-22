package com.delivery.common.util;

import com.delivery.common.SedException;
import com.delivery.common.action.Action;
import com.delivery.common.dao.OrdersDao;
import com.delivery.common.dao.OrdersLogDao;
import com.delivery.common.entity.OrdersEntity;
import com.delivery.common.entity.OrdersOperationLogEntity;
import com.delivery.common.entity.UsersEntity;
import com.delivery.common.ErrorCode;
import com.delivery.common.Response;
import com.delivery.dispatch.Dispatcher;
import com.delivery.event.EventContext;
import com.delivery.order.OrderState;

import javax.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.delivery.common.constant.Constant.USER_ID;
import static com.delivery.user.UserConstant.*;
import static com.delivery.common.ErrorCode.*;
import static com.delivery.common.constant.Constant.*;

/**
 * @author finderlo
 * @date 21/04/2017
 */
public class Util {


    public static void sendSysMsg(String msg, String replacementId, Dispatcher dispatcher) {
        //todo
    }

    public static <T extends Enum<T>> T getActionSubType(Action action, Class<T> classT) {
        T type = (T) action.get(ACTION_SUB_TYPE);
        checkNull(type, DISPATCHER_UNKNOWN_ACTION_TYPE);
        T[] values = classT.getEnumConstants();
        for (T t : values) {
            Enum ty = (Enum) t;
            if (ty.equals(type)) {
                return t;
            }
        }
        return null;

    }

//    public static void main(String[] args) {
//        Action action = new Action();
//        action.put(ACTION_SUB_TYPE, OrderActionType.accept);
//        System.out.println(getActionSubType(action,OrderActionType.class).ordinal());
//    }

    public static void checkNull(Object type, ErrorCode errorCode) {
        if (type == null) {
            throw new SedException(errorCode);
        }
    }

    public static void checkAdmin(Action action){
        checkRightType(action, UsersEntity.UserType.SYSTEM);
        checkRightType(action, UsersEntity.UserType.ADMINSTARTE);
    }

    public static void checkRightType(Action action,UsersEntity.UserType userType){
        UsersEntity.UserType userType1 = (UsersEntity.UserType) action.get(USER_TYPE);
        if (!userType1.equals(userType)){
            throw new SedException(SYSTEM_NO_ENOUGH_PERMISSION);
        }
    }

    /**
     * 从Action中获取TOKEN属性
     *
     * @return 返回TOKEN，没有此属性返回""；
     * @author finderlo
     */
    @NotNull
    public static String getToken(Action action) {
        return (String) action.getOrDefault(USER_TOKEN, "");
    }

    @NotNull
    public static String getUserPsd(Action action) {
        return (String) action.getOrDefault(USER_PASSWORD, "");
    }

    @NotNull
    public static String getUserId(Action action) {
        return (String) action.getOrDefault(USER_ID, "");
    }

    @NotNull
    public static String getUserPhone(Action action) {
        return (String) action.getOrDefault(USER_PHONE, "");
    }

    /**
     * 校验密码的正确性
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public static boolean validUser(String phone, String psd, UsersEntity correctUsers) {
        if (correctUsers == null) return false;
        if ("".equals(phone) || "".equals(psd)) return false;
        return psd.equals(correctUsers.getUserPassword()) && phone.equals(correctUsers.getUserPhone());
    }

    public static EventContext parseEventfromUsersEntity(UsersEntity user) {
        EventContext context = new EventContext();
        context.put(EVENT_USER_REGISTER_USERENTITY, user);
        return context;
    }


    public static void checkNull(Object o) {
        if (o == null) {
            throw new SedException(SYSTEM_NULL_OBJECT);
        }
    }

    public static boolean isSuccess(Response response) {
        return response.getError_code() == DEFAULT_SUCCESS.getCode();
    }


    /**
     * 取出action中用户，没有则抛出异常
     * @author finderlo
     * @date 22/04/2017
     * @throws SedException
     */
    public static UsersEntity getUser(Action action) {
        UsersEntity user =  (UsersEntity) action.getOrDefault(USER_ENTITY, null);
        checkNull(user,SYSTEM_USER_NO_EXIST);
        return user;
    }
    public static OrdersEntity getOrder(Action action) {
        OrdersEntity order =  (OrdersEntity) action.getOrDefault(ORDER_ENTITY, null);
        checkNull(order,SYSTEM_ORDER_NO_EXIST);
        return order;
    }

    /**
     * 管理员查询订单
     * @author finderlo
     * @date 22/04/2017
     */
    public static Map<String, String> getAttr(Action action) {
        //返回action中order查询条件

        //action.get*() 中的key,是网页或手机中传来中请求的参数
        Map<String,String> attr = new HashMap<>();
        String recipient = (String) action.getOrDefault("recipient_ID","");
        if (!recipient.equals("")) attr.put("recipientId",recipient);

        String replacement = (String) action.getOrDefault("replacement_ID","");

        if (!replacement.equals(""))
            attr.put("replacementId",recipient);

        String orderid = (String) action.getOrDefault("orders_ID","");
        if (!orderid.equals(""))
        attr.put("ordersId",orderid);

        return attr;
    }

    public static OrderState getOrderState(Action action) {
        String state = (String) action.getOrDefault(ORDER_ATTR_STATE, null);
        checkNull(state);
        return OrderState.valueOf(state);
    }

    /**
     * 获取订单完成状态 0时未完成 1时已完成
     * @author finderlo
     * @date 23/04/2017
     */
    public static int getOrderCompleteState(Action action) {
        String state = (String) action.getOrDefault(ORDER_ATTR_COMPLETE_STATE, "0");
        return Integer.valueOf(state);
    }

    @NotNull
    public static String getOrdersGrade(Action action) {
        return (String) action.getOrDefault(ORDER_ATTR_GRADLE, "");
    }


    public static OrdersEntity getOrdersById(Action action, OrdersDao ordersDao) {
        String orderid = getOrdersId(action);
        assert !Objects.equals(orderid, "");
        OrdersEntity order = ordersDao.findById(orderid);
        checkNull(order);
        return order;
    }

    public static String getOrdersId(Action action) {
        return (String) action.getOrDefault(ORDER_ID, "");
    }

    public static void pushlishOrdersLog(String logId, OrderState state, OrdersLogDao ordersLogDao) {
        OrdersOperationLogEntity log = new OrdersOperationLogEntity();
        log.setOrdersId(logId);
        log.setStateChangeTime(new Timestamp(System.currentTimeMillis()));
        log.setStateType(state.name());
        ordersLogDao.save(log);
    }

    /**
     * 根据用户ID，来获取信用值
     *
     * @author finderlo
     * @date 21/04/2017
     */
    public static int getCredit(Action action, Dispatcher dispatcher) {
        //todo 通过用户ID，向信用请求计算并返回信用值
        return 0;
    }

    public static int getOrderCountByReplace(String userID, int state, OrdersDao ordersDao) {
        return getOrdersByReplace(userID, state, ordersDao).size();
    }

    public static List<OrdersEntity> getOrdersByReplace(String userId, int state, OrdersDao ordersDao) {
        boolean tar = state != 0;
        List<OrdersEntity> orders = ordersDao.findByReplacementId(userId);
        return orders.stream().filter(e -> !e.getOrdersState().isComplete() ^ tar).collect(Collectors.toList());
    }

    public static Response handleException(Exception e) {
        if (e instanceof SedException) {
            return Response.error(((SedException) e).getErrorCode());
        } else return Response.error(ErrorCode.SYSTEM_UNKNOWN_ERROR);
    }


}
