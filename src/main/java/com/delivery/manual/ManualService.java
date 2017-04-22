package com.delivery.manual;

import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.Response;
import com.delivery.event.EventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author finderlo
 * @date 17/04/2017
 */
@Component
public class ManualService implements ActionHandler, EventPublisher {
    @Override
    public boolean canHandleAction(Action action) {
        //todo
        return false;
    }

    @Override
    public Response execute(Action action) {
        //todo
        return null;
    }

    /**
     * 用户申请升级
     *
     * @param action 用户ID
     *               0.检测用户是否已经存在审核类型为升级且审核状态为待审核状态
     *               1.保存申请单数据 设置审核类型为1[升级] 设置申请状态为待审核
     *               2.将用户提交的个人数据保存至Users表
     * @author finderlo
     * @date 17/04/2017
     */
    public Response applyUpgrade(Action action) {
        //todo
        return null;
    }


    /**
     * 用户申请降级
     *
     * @param action 用户ID
     *               1.保存申请单数据 设置审核类型为2[降级]设置申请状态为待审核
     * @author finderlo
     * @date 17/04/2017
     */
    public Response applyDegrade(Action action) {
        //todo
        return null;
    }

    /**
     * 人工处理升级
     *
     * @param action 审核单ID
     *               管理员ID
     *               1.保存信息并修改审核状态通过/不通过
     *               2.修改Users表的 身份 若通过设为代取人 不通过则不变
     * @author finderlo
     * @date 17/04/2017
     */
    public Response handleUpgrade(Action action) {
        //todo
        return null;
    }


    /**
     * 人工处理降级
     *
     * @param action 审核单ID
     *               管理员ID
     *               1.保存信息并修改审核状态通过/不通过
     *               2.修改Users表的 身份 若通过设为代取人 不通过则不变
     * @author finderlo
     * @date 17/04/2017
     */
    public Response handleDegrade(Action action) {
        //todo
        return null;
    }

    /**
     * 向用户展示申请的具体情况和实时状态
     *
     * @param action 用户ID
     *               1.根据用户ID查出所有的审核单
     *               2.返回一个包含这些审核单的list
     * @author lx
     * @date 2017/4/22
     */
    public Response showReviewToUser(Action action) {
        //todo
        return null;
    }

    /**
     * 向管理员展示申请的具体内容
     *
     * @param action 用户ID
     *               1.根据用户ID查出待审核的审核单
     *               2.根据用户ID查出用户的详细信息（Users表）
     *               3.返回审核单及用户详细信息
     * @author lx
     * @date 2017/4/22
     */
    public Response showReviewToManager(Action action) {
        //todo
        return null;
    }

    /**
     * 用户申诉一个订单
     *
     * @param action 订单号
     *               申诉申请人ID（complaint表中的user_ID）
     *               0.检测订单是否已经在申诉中 如果在则创建失败
     *               1.创建一个申诉单
     *               2.修改订单状态为申诉中
     * @author finderlo
     * @date 17/04/2017
     */
    public Response orderComplain(Action action) {
        //todo
        return null;
    }


    /**
     * 向管理员展示申诉单详情
     * <p>
     * 1.查询所有待处理的申诉单信息并返回
     *
     * @author lx
     * @date 2017/4/22
     */
    public Response showComplainToManager(Action action) {
        //todo
        return null;
    }


    /**
     * 人工处理一个订单申诉，成功或者失败，发布相对应事件
     *
     * @param action 申诉单号
     *               管理员号
     *               申诉处理结果（用于修改订单状态）（具体见状态转换图的申诉）
     *               1.保存申诉单的修改信息
     *               2.修改订单状态
     *               3.发布对应事件通知双方（发送个消息）
     * @author finderlo
     * @date 17/04/2017
     */
    public Response orderHandleComplain(Action action) {
        //todo
        return null;
    }
}
