package com.delivery.common.util;

import com.delivery.common.SedException;
import com.delivery.common.action.Action;
import com.delivery.common.dao.OrdersDao;
import com.delivery.common.dao.OrdersLogDao;
import com.delivery.common.entity.OrdersEntity;
import com.delivery.common.entity.OrdersOperationLogEntity;
import com.delivery.common.entity.UsersEntity;
import com.delivery.common.response.ErrorCode;
import com.delivery.common.response.Response;
import com.delivery.dispatch.Dispatcher;
import com.delivery.event.EventContext;
import com.delivery.order.OrderActionType;
import com.delivery.order.OrderState;

import javax.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.delivery.common.constant.Constant.USER_ID;
import static com.delivery.user.UserConstant.*;
import static com.delivery.common.response.ErrorCode.*;
import static com.delivery.common.constant.Constant.*;

/**
 * @author finderlo
 * @date 21/04/2017
 */
public class Util {


    public static void sendSysMsg(String msg, String replacementId, Dispatcher dispatcher) {
        //todo
    }

    public static <T> T getActionSubType(Action action, Class<T> classT){
        String type = (String) action.get(ACTION_SUB_TYPE);
        checkNull(type,DISPATCHER_UNKNOWN_ACTION_TYPE);
        T[] values = classT.getEnumConstants();
        for (T t : values) {
            Enum ty = (Enum) t;
            if (ty.name().equals(type)){
                return t;
            }
        }
        return null;

    }

    private static void checkNull(String type, ErrorCode errorCode) {
        if(type == null){
            throw new SedException(errorCode);
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
    public static boolean validUser(String userId, String psd, UsersEntity correctUsers) {
        if (correctUsers == null) return false;
        if ("".equals(userId) || "".equals(psd)) return false;
        return psd.equals(correctUsers.getUserPassword()) && userId.equals(correctUsers.getUserId());
    }

    public static EventContext parseEventfromUsersEntity(UsersEntity user) {
        EventContext context = new EventContext();
        context.put(EVENT_USER_REGISTER_USERENTITY, user);
        return context;
    }

    /**
     * 从Action中提取信息封装为UsersEntity
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public static UsersEntity getUser(Action action) {
        //todo 注册时，获取所有的信息，返回成一个Userentity
        return null;
    }

    public static void checkNull(Object o) {
        if (o == null) {
            throw new SedException(SYSTEM_NULL_OBJECT);
        }
    }

    public static boolean isSuccess(Response response) {
        return response.getError_code() == DEFAULT_SUCCESS.getCode();
    }


    public static UsersEntity getUser(Action action, Dispatcher dispatcher) {
        return (UsersEntity) action.getOrDefault(USER_ENTITY, null);
    }


    public static Map<String, String> getAttr(Action action) {
        //todo 返回action中order查询条件
        return null;
    }

    public static OrderState getOrderState(Action action) {
        String state = (String) action.getOrDefault(ORDER_ATTR_STATE, null);
        checkNull(state);
        return OrderState.valueOf(state);
    }

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


}
