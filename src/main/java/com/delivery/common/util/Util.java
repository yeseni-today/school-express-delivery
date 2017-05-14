package com.delivery.common.util;

import com.delivery.common.SedException;
import com.delivery.common.action.Action;
import com.delivery.common.dao.OrderDao;
import com.delivery.common.dao.OrderLogDao;
import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.OrderLogEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.common.ErrorCode;
import com.delivery.common.Response;
import com.delivery.dispatch.Dispatcher;
import com.delivery.event.EventContext;
import com.delivery.order.OrderState;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;
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


    public static void checkNull(Object o) {
        checkNull(o, SYSTEM_NULL_OBJECT);
    }

    public static void checkNull(Object type, ErrorCode errorCode) {
        if (type == null) {
            throw new SedException(errorCode);
        }
    }

    public static void checkNotEqual(@Null Object type, @NotNull Object target, ErrorCode errorCode) {
        if (!target.equals(type)) {
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
        String ls = (String) action.getOrDefault(USER_TOKEN, "");
//        System.out.println(ls);
        return ls;
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
    public static boolean validUser(String phone, String psd, UserEntity correctUsers) {
        if (correctUsers == null) return false;
        if ("".equals(phone) || "".equals(psd)) return false;
        return psd.equals(correctUsers.getPassword()) && phone.equals(correctUsers.getPhone());
    }

    public static EventContext parseEventfromUsersEntity(UserEntity user) {
        EventContext context = new EventContext();
        context.put(EVENT_USER_REGISTER_USERENTITY, user);
        return context;
    }


    public static boolean isSuccess(Response response) {
        return response.getError_code() == DEFAULT_SUCCESS.getCode();
    }


    /**
     * 取出action中用户，没有则抛出异常
     *
     * @throws SedException
     * @author finderlo
     * @date 22/04/2017
     */
    public static UserEntity getUser(Action action) {
        UserEntity user = (UserEntity) action.getOrDefault(USER_ENTITY, null);
        checkNull(user, SYSTEM_USER_NO_EXIST);
        return user;
    }

    public static OrderEntity getOrder(Action action) {
        OrderEntity order = (OrderEntity) action.getOrDefault(ORDER_ENTITY, null);
        checkNull(order, SYSTEM_ORDER_NO_EXIST);
        return order;
    }


    public static void pushlishOrdersLog(String logId, OrderState state, OrderLogDao orderLogDao) {
        try {


            OrderLogEntity log = new OrderLogEntity();
            log.setOrderId(logId);
            log.setChangeTime(new Timestamp(System.currentTimeMillis()));
            log.setState(state);
            orderLogDao.save(log);

        } catch (Exception e) {
            throw new SedException(ORDER_OPERATION_LOG_PUBLISH_ERROR);
        }
    }

    /**
     * 根据用户ID，来获取信用值
     *
     * @author finderlo
     * @date 21/04/2017
     */
    public static int getCredit(Action action, Dispatcher dispatcher) {
        //todo 通过用户ID，向信用请求计算并返回信用值
        return 100;
    }

    public static int getOrderCountByReplace(String userID, int state, OrderDao orderDao) {
        return getOrdersByUser(userID, state, orderDao).size();
    }

    public static List<OrderEntity> getOrdersByUser(String userId, int state, OrderDao orderDao) {
        boolean tar = state != 0;
        List<OrderEntity> orders = new ArrayList<>();
        List<OrderEntity> ordersRepl = orderDao.findByReplacementId(userId);
        orders.addAll(ordersRepl.stream().filter(e -> !e.getState().isComplete() ^ tar).collect(Collectors.toList()));


        List<OrderEntity> ordersReci = orderDao.findByRecipientId(userId);
        orders.addAll(ordersReci.stream().filter(e -> !e.getState().isComplete() ^ tar).collect(Collectors.toList()));

        return orders;
    }

    public static Response handleException(Exception e) {
        if (e instanceof SedException) {
            return Response.error(((SedException) e).getErrorCode());
        } else return Response.error(ErrorCode.SYSTEM_UNKNOWN_ERROR);
    }


    public static int getCreditChangeValue(Action action) {
        Object valo = action.getOrDefault(CREDIT_CHANGE_VALUE, 0);
        int val = 0;
        if (valo instanceof String) {
            String valstr = (String) valo;
            valo = Integer.valueOf(valstr);
        } else if (valo instanceof Integer) {
            val = (int) valo;
        } else {
            throw new SedException(ErrorCode.CREDIT_CHANGE_VALUE_ERROR_TYPE);
        }
        return val;
    }


    public static String getRemark(Action action) {
        String remark = (String) action.getOrDefault(CREDIT_REMARK, "");
        return remark;
    }


    public static Map<String, Object> entityToMap(Object object) {
        Map<String, Object> attr = new HashMap<>();
        if (object == null) {
            return attr;
        }
        Class clazz = object.getClass();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                attr.put(field.getName(), field.get(object));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return attr;
    }

    public  static  <T extends Enum<T>> T  getEnumParamByIntger(Action action, String paramName, Class<T> classZ) {

        String type = (String) action.get(paramName);
        int ori = Integer.parseInt(type);
        checkNull(type, SYSTEM_WRONG_PARAM_NAME_INTGER);
        T[] values = classZ.getEnumConstants();
        for (T t : values) {
            Enum ty = (Enum) t;
            if (ty.ordinal() == ori) {
                return t;
            }
        }
        throw new SedException(SYSTEM_WRONG_PARAM_NAME_INTGER);
    }
}
