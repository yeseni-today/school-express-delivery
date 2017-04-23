package com.delivery.order;

import com.delivery.common.ErrorCode;
import com.delivery.common.SedException;
import com.delivery.common.action.Action;
import com.delivery.common.dao.OrdersDao;
import com.delivery.common.entity.OrdersEntity;
import com.delivery.common.entity.UsersEntity;
import com.delivery.common.util.Util;

import java.sql.Timestamp;

import static com.delivery.common.ErrorCode.*;

/**
 * @author finderlo
 * @date 20/04/2017
 */
public class OrderUtil {

    public static String getAttrOrThrow(Action action, String attr, ErrorCode errorCode) {
        String res = null;
        res = (String) action.getOrDefault(attr, null);
        if (res == null) {
            throw new SedException(errorCode);
        }
        return res;
    }

    /**
     * 创建一个订单并且保存
     *
     * @author finderlo
     * @date 22/04/2017
     */
    public static synchronized OrdersEntity getOrderAndSave(Action action, OrdersDao ordersDao) {
        OrdersEntity order = null;

        try {
            order = new OrdersEntity();

            //通过TOKEN自动获取
            String userId;

            //用户必填：快递名称，取件地点，取件时间，送件地点，送件时间，大小件
            String expressname;
            String pickup_addr;
            Timestamp pickup_time;
            String delivery_addr;
            Timestamp delivery_time;
            //用户可选（填写默认值）：快递单号、取件号码、备注
            String express_code;
            String pickup_code;
            String remark;

            UsersEntity creator = Util.getUser(action);
            userId = creator.getUserId();
            expressname = getAttrOrThrow(action, "express_name", ORDER_CREATE_EXPRESSNAME_NOEXIST);
            pickup_addr = getAttrOrThrow(action, "pickup_address", ORDER_CREATE_PICKUP_ADDR_NOEXIST);

            //@param s timestamp in format <code>yyyy-[m]m-[d]d hh:mm:ss[.f...]</code>.
            // The fractional seconds may be omitted. The leading zero for <code>mm</code>
            //and <code>dd</code> may also be omitted.
            String pickup_time_str = getAttrOrThrow(action, "pickup_time", ORDER_CREATE_PICKTIME_NO_EXIST);

            pickup_time = Timestamp.valueOf(pickup_time_str);

            delivery_addr = getAttrOrThrow(action, "delivery_address",
                    ORDER_CREATE_DELIVERY_ADDR_NOEXIST);

            String delivery_time_str = getAttrOrThrow(action, "delivery_time",
                    ORDER_CREATE_DELIVERY_TIME_NOEXIST);
            delivery_time = Timestamp.valueOf(delivery_time_str);

            express_code = (String) action.getOrDefault("express_code", "");
            pickup_code = (String) action.getOrDefault("pickup_code", "");
            remark = (String) action.getOrDefault("orders_remark", "");

            order.setRecipientId(userId);
            order.setExpressName(expressname);
            order.setPickupTime(pickup_time);
            order.setPickupAddress(pickup_addr);
            order.setDeliveryAddress(delivery_addr);
            order.setDeliveryTime(delivery_time);
            order.setExpressCode(express_code);
            order.setPickupCode(pickup_code);
            order.setOrdersRemark(remark);

            //默认值
            order.setOrdersCreatetime(new Timestamp(System.currentTimeMillis()));
            order.setOrdersState(OrderState.WAIT_ACCEPT);

            String ordersId = ordersDao.newOrderId(OrdersDao.IdType.ORDER);
            order.setOrdersId(ordersId);
            ordersDao.save(order);
            System.out.println("订单创建完成");
            return order;
        } catch (IllegalArgumentException e) {
            throw new SedException(SYSTEM_TIME_FORMAT_ERROR);
        } catch (SedException e1) {
            throw e1;
        } catch (Exception e) {
            if (order == null) {
                throw new SedException(ORDER_CREATE_FAILED_UNKNOW_ERROR);
            } else if (order.getOrdersId() == null) {
                e.printStackTrace();
                throw new SedException(ORDER_CREATE_FAILED_UNKNOW_ERROR);
            } else {
                String id = order.getOrdersId();
                OrdersEntity e1 = ordersDao.findById(id);
                ordersDao.delete(e1);
                e.printStackTrace();
                throw new SedException(ORDER_CREATE_FAILED_UNKNOW_ERROR);
            }
        }
    }
}
