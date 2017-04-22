package com.delivery.user;

import com.delivery.common.action.Action;
import com.delivery.common.constant.Constant;
import com.delivery.common.dao.UsersDao;
import com.delivery.common.entity.UsersEntity;
import com.delivery.event.Event;
import com.delivery.event.EventContext;
import org.hibernate.usertype.UserType;

import javax.validation.constraints.NotNull;

import static com.delivery.user.UserConstant.*;

/**
 * Created by finderlo on 15/04/2017.
 */
public class UserUtil {

    /**
     * 从Action中获取具体子类型
     *
     * @author finderlo
     */
    static UserActionType getType(Action action) {
        try {
            return (UserActionType) action.getOrDefault(Constant.ACTION_SUB_TYPE, UserActionType.UNKNOWN);
        } catch (ClassCastException e) {
            return UserActionType.UNKNOWN;
        }
    }


    /**
     * 注册时，判断注册信息是否正确
     *
     * @param usersEntity 注册的用户信息
     * @param usersDao    用来判断是否存在相同的账户名
     * @author finderlo
     */
    static boolean isRightRegisterUserInfo(UsersEntity usersEntity, UsersDao usersDao) {
        //todo 确认注册时候的信息是否正确
        return false;
    }

}
