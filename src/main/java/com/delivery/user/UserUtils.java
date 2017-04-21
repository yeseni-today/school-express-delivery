package com.delivery.user;

import com.delivery.common.action.Action;
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
public class UserUtils {

    /**
     * 从Action中获取具体子类型
     *
     * @author finderlo
     */
    static UserActionType getType(Action action) {
        try {
            return (UserActionType) action.getOrDefault(USER_ACTION_TYPE, UserActionType.UNKNOWN);
        } catch (ClassCastException e) {
            return UserActionType.UNKNOWN;
        }
    }

    /**
     * 从Action中获取TOKEN属性
     *
     * @return 返回TOKEN，没有此属性返回""；
     * @author finderlo
     */
    @NotNull
    static String getToken(Action action) {
        return (String) action.getOrDefault(USER_TOKEN, "");
    }

    @NotNull
    static String getUserPsd(Action action) {
        return (String) action.getOrDefault(USER_PASSWORD, "");
    }

    @NotNull
    static String getUserId(Action action) {
        return (String) action.getOrDefault(USER_ID, "");
    }

    @NotNull
    static String getUserPhone(Action action){
        return (String) action.getOrDefault(USER_PHONE,"");
    }
    /**
     * 校验密码的正确性
     * @author finderlo
     * @date 17/04/2017
     */
    static boolean validUser(String userId, String psd, UsersEntity correctUsers) {
        if (correctUsers == null) return false;
        if ("".equals(userId) || "".equals(psd)) return false;
        return psd.equals(correctUsers.getUserPassword()) && userId.equals(correctUsers.getUserId());
    }

    static EventContext parseEventfromUsersEntity(UsersEntity user){
        EventContext context = new EventContext();
        context.put(EVENT_USER_REGISTER_USERENTITY,user);
        return context;
    }

    /**
     * 从Action中提取信息封装为UsersEntity
     * @author finderlo
     * @date 17/04/2017
     */
    static UsersEntity getUser(Action action) {
        //todo 注册时，获取所有的信息，返回成一个Userentity
        return null;
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
