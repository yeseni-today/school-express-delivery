package com.delivery.order;

import com.delivery.common.action.Action;
import com.delivery.common.dao.OrdersLogDao;
import com.delivery.common.entity.OrdersEntity;
import com.delivery.common.entity.OrdersOperationLogEntity;
import com.delivery.common.entity.UsersEntity;
import com.delivery.dispatch.Dispatcher;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @author finderlo
 * @date 20/04/2017
 */
public class OrderUtil {

    public static UsersEntity getUser(Action action, Dispatcher dispatcher){
        //todo 通过action获取UsersEntity
        return null;
    }


    public static Map<String,String> getAttr(Action action){
        //todo 返回action中order查询条件
        return null;
    }

    public static int getState(Action action){
        //todo 判断state值 未完成或者已完成
        return 0;
    }

    public static String getUserID(Action action){
        //todo 返回用户ID
        return null;
    }

    public static OrdersEntity getOrders(Action action,Dispatcher dispatcher){
        //todo 从action取出order entity
        return null;
    }

    public static String getOrdersId(Action action){
        //todo 获取OrdersID，action attr中
        return "";
    }

    public static void  pushlishOrdersLog(String logId, OrderState state, OrdersLogDao ordersLogDao){
        OrdersOperationLogEntity log = new OrdersOperationLogEntity();
        log.setOrdersId(logId);
        log.setStateChangeTime(new Timestamp(System.currentTimeMillis()));
        log.setStateType(state.name());
        ordersLogDao.save(log);
    }
    /**
     * 根据用户ID，来获取信用值
     * @author finderlo
     * @date 21/04/2017
     */
    public static  int getCredit(Action action,Dispatcher dispatcher){
        //todo 通过用户ID，向信用请求计算并返回信用值
        return 0;
    }

    public static int getOrderCount(String userID,int state){
        //todo 返回用户当前订单数，0位未完成，1位已完成
        return 0;
    }

    public static List<OrdersEntity> getOyders(String userId,int state){
        //todo 返回用户当前订单列表，0位未完成，1位已完成
        return null;
    }
}
