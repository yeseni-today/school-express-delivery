package com.delivery.user;

import com.delivery.common.SedException;
import com.delivery.common.action.Action;
import com.delivery.common.constant.Constant;
import com.delivery.common.dao.UserDao;
import com.delivery.common.entity.UserEntity;
import com.delivery.common.ErrorCode;

import static com.delivery.common.ErrorCode.SYSTEM_UNKNOWN_ERROR;
import static com.delivery.common.ErrorCode.USER_REGISTER_INCORRECT_INFO;

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
            UserActionType ace = (UserActionType) action.get(Constant.ACTION_SUB_TYPE);
            return ace;
        } catch (ClassCastException e) {
            return UserActionType.UNKNOWN;
        }
    }

    /**
     * 从Action中提取信息封装为UsersEntity并保持
     *
     * @author finderlo
     * @date 17/04/2017
     */
    public static synchronized UserEntity getUserAndSave(Action action, UserDao dao) {
        UserEntity user = null;
        try {
            user = new UserEntity();
            //用户填写的参数:name\phone\password\schoolcard\sex\schoolname\schooladdress
            user.setUserName((String) action.get("user_name"));
            user.setUserPhone((String) action.get("user_phone"));
            checkUniquePhone(user.getUserPhone(),dao);
            user.setUserPassword((String) action.get("user_password"));
            user.setUserSchoolcard((String) action.get("user_schoolcard"));
            user.setUserSex((String) action.get("user_sex"));
            user.setUserSchoolname((String) action.get("user_schoolname"));
            user.setUserSchooladdress((String) action.get("user_schooladdress"));


            //默认参数
            user.setUserIdentity(Constant.getDefaultUserEntity().getUserIdentity());

            //id
            user.setUserId(dao.newUsersId());

            dao.save(user);
            return user;
        } catch (SedException e1) {
            throw e1;
        } catch (Exception e) {
            if (user == null) {
                throw new SedException(SYSTEM_UNKNOWN_ERROR);
            } else if (user.getUserId() == null) {
                throw new SedException(USER_REGISTER_INCORRECT_INFO);
            } else {
                String id = user.getUserId();
                UserEntity e1 = dao.findById(id);
                dao.delete(e1);
                throw new SedException(SYSTEM_UNKNOWN_ERROR);
            }
        }
    }

    private static void checkUniquePhone(String phone, UserDao dao) {
        if (dao.findByUserPhone(phone) != null){
            throw new SedException(ErrorCode.USER_REGISTER_DEP_PHONE);
        }
    }

}
