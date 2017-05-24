package com.delivery.rest.credit;

import com.delivery.common.Response;
import com.delivery.common.StringMap;
import com.delivery.common.dao.CreditRecordDao;
import com.delivery.common.entity.CreditRecordEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.config.annotation.AdminAuthorization;
import com.delivery.config.annotation.Authorization;
import com.delivery.config.annotation.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.delivery.common.Response.*;

/**
 * @author finderlo
 * @date 20/04/2017
 */
@RestController
@RequestMapping("/credits")
public class CreditController {

    @Autowired
    private CreditRecordDao creditRecordDao;


    /**
     * 管理员查询某一个用户的信用值，通过参数
     *
     * @author Ticknick Hou
     * @date 02/05/2017
     */
    @GetMapping("/{user_id}")
    @AdminAuthorization
    public Response userCreditValueAdmin(
            @PathVariable String user_id) {
        List<CreditRecordEntity> records = creditRecordDao.findByUserId(user_id);
        int credit = 0;
        for (CreditRecordEntity record : records) {
            credit += record.getValue();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid", user_id);
        map.put("credit_value", credit);
        return ok(map);
    }


    /**
     * 查询某一用户的信用值变化信息
     * list
     *
     * @author Ticknick Hou
     * @date 02/05/2017
     */
    @GetMapping("/token")
    @Authorization
    public Response userRecords(
            @CurrentUser UserEntity user) {
        return ok(creditRecordDao.findByUserId(user.getUid()));
    }

}
