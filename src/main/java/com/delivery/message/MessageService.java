package com.delivery.message;

import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.action.ActionType;
import com.delivery.common.response.Response;
import com.delivery.event.EventPublisher;
import org.springframework.stereotype.Component;

/**
 * Created by finderlo on 17/04/2017.
 */
@Component
public class MessageService implements ActionHandler,EventPublisher {
    /**
     * 能否执行Action
     *
     * @author finderlo
     */
    @Override
    public boolean canHandleAction(Action action) {
        //todo
        return action.getType() == ActionType.MESSAGE;
    }

    /**
     * 执行Action
     *
     * @author finderlo
     */
    @Override
    public Response execute(Action action) {
        //todo
        return null;
    }

    /**
     * 发送一条消息
     *
     * @param action 发送者ID（系统或管理员）
     *               接受者ID（所有用户或者一类用户）
     *               消息内容结构体
     * @return success ； error 权限不足等造成失败
     * @author finderlo
     */
    public Response send(Action action) {
        //todo
        return null;
    }

    /**
     * 获取用户的消息
     *
     * @param action 用户ID及用户身份（用于获取群发的消息）
     *               消息类型（可选）
     * @author finderlo
     */
    public Response timeline(Action action) {
        //todo

        return null;
    }

    /**
     * 将消息设为已读状态
     *
     * @param action 消息ID
     * @author finderlo
     */
    public Response read(Action action) {
        //todo
        return null;
    }
}
