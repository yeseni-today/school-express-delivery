package com.delivery.common.util;

import com.delivery.common.ErrorCode;
import com.delivery.common.SedException;
import com.delivery.common.action.Action;
import com.delivery.common.constant.Constant;
import com.delivery.common.dao.UserDao;
import com.delivery.common.entity.UserEntity;

import static com.delivery.common.ErrorCode.SYSTEM_NO_ENOUGH_PERMISSION;
import static com.delivery.common.ErrorCode.SYSTEM_UNKNOWN_ERROR;
import static com.delivery.common.ErrorCode.USER_REGISTER_INCORRECT_INFO;
import static com.delivery.common.constant.Constant.USER_TYPE;

/**
 * @author finderlo
 * @date 11/05/2017
 */
public class UserUtil {

    private static UserDao userDao;

    public static void setUserDao(UserDao userDao) {
        UserUtil.userDao = userDao;
    }

    public static UserEntity findUserById(String id){
        return userDao.findById(id);
    }

    public static void checkAdmin(Action action) {
        if (checkRightType(action, UserEntity.UserType.SYSTEM)) {
            if (checkRightType(action, UserEntity.UserType.ADMINSTARTE)) {
                return;
            } else throw new SedException(SYSTEM_NO_ENOUGH_PERMISSION);

        }

    }

    public static boolean checkRightType(Action action, UserEntity.UserType userType) {
        UserEntity.UserType userType1 = (UserEntity.UserType) action.get(USER_TYPE);
        if (!userType1.equals(userType)) {
            return false;
        }
        return false;
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
            user.setName((String) action.get("user_name"));
            user.setPhone((String) action.get("user_phone"));
            checkUniquePhone(user.getPhone(),dao);
            user.setPassword((String) action.get("user_password"));
            user.setSchoolCard((String) action.get("user_schoolcard"));
            user.setSex((String) action.get("user_sex"));
            user.setSchoolName((String) action.get("user_schoolname"));
            user.setSchoolAddress((String) action.get("user_schooladdress"));


            //默认参数
            user.setIdentity(Constant.getDefaultUserEntity().getIdentity());

            //id
            user.setId(dao.newUsersId());

            dao.save(user);
            return user;
        } catch (SedException e1) {
            throw e1;
        } catch (Exception e) {
            if (user == null) {
                throw new SedException(SYSTEM_UNKNOWN_ERROR);
            } else if (user.getId() == null) {
                throw new SedException(USER_REGISTER_INCORRECT_INFO);
            } else {
                String id = user.getId();
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
