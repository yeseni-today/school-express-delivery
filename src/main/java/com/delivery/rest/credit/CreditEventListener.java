package com.delivery.rest.credit;

import com.delivery.common.ErrorCode;
import com.delivery.common.SedException;
import com.delivery.common.dao.CreditRecordDao;
import com.delivery.common.entity.ComplaintEntity;
import com.delivery.common.entity.CreditRecordEntity;
import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.event.Event;
import com.delivery.event.EventContext;
import com.delivery.event.EventExecutor;
import com.delivery.event.EventManager;
import com.delivery.event.context.ComplaintResultEventContext;
import com.delivery.event.context.OrderEventContext;
import com.delivery.event.context.OrderReplacementCancelEventContext;
import com.delivery.event.context.UserRegisterEventContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author finderlo
 * @date 16/05/2017
 */
@Component
public class CreditEventListener {

    @Autowired
    CreditRecordDao creditRecordDao;

    /**
     * 初始化信用体系相关的事件接收器
     *
     * @author finderlo
     * @date 02/05/2017
     */
    @Autowired
    public void registerEventListener(EventManager manager) {
        manager.register(Event.UserRegisterEvent, userRegisterEventExecutor());
        manager.register(Event.OrderCommentSuccessEvent, orderCommentEventExecutor());
        manager.register(Event.OrderCompleteSuccessEvent, orderCompleteSuccessEventExecutor());
        manager.register(Event.OrderReplacementCancelEvent, orderReplacementCancelEventExecutor());
        manager.register(Event.ComplaintResultEvent, complainResultEventEventExecutor());
    }


    /**
     * 申诉处理结果，事件执行器，主要执行信用值的记录变化
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    private EventExecutor complainResultEventEventExecutor() {
        return new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.ComplaintResultEvent) return;
                ComplaintResultEventContext context1 = (ComplaintResultEventContext) context;
                ComplaintEntity complaint = context1.getComplaint();
                int credit = context1.getCreditChangeValue();
                saveRecord(complaint.getOrder().getRecipient().getUid(), event.name(), credit, context1.getComplaint().getResult());
            }
        };
    }


    /**
     * 用户注册，事件执行器
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    private EventExecutor userRegisterEventExecutor() {
        return new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.UserRegisterEvent) return;
                UserRegisterEventContext context1 = (UserRegisterEventContext) context;
                UserEntity user = context1.getUser();
                saveRecord(user.getUid(), event.name(), 100, null);
            }
        };
    }

    /**
     * 订单评价，事件执行器
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    private EventExecutor orderCommentEventExecutor() {
        return new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.OrderCommentSuccessEvent) return;
                OrderEntity order = ((OrderEventContext) context).getOrder();
                int grade = Integer.parseInt(order.getGrade());
                saveRecord(order.getRecipientId(), event.name(), grade - 2, order.toString());
                saveRecord(order.getReplacementId(), event.name(), grade - 2, order.toString());
            }
        };
    }

    /**
     * 订单成功完成，事件执行器
     *
     * @author Ticknick
     * @serialData 02/05/2017
     */
    private EventExecutor orderCompleteSuccessEventExecutor() {
        return new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.OrderCompleteSuccessEvent) return;
                OrderEntity order = ((OrderEventContext) context).getOrder();
                saveRecord(order.getReplacementId(), event.name(), 1, order.toString());
                saveRecord(order.getRecipientId(), event.name(), 1, order.toString());
            }
        };
    }

    /**
     * 代取人取消订单，事件执行器
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    private EventExecutor orderReplacementCancelEventExecutor() {
        return new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.OrderReplacementCancelEvent) return;
                String replacement = ((OrderReplacementCancelEventContext) context).getReplacement().getUid();
                saveRecord(replacement, event.name(), -10, null);
            }
        };
    }

    private void saveRecord(String userId, String event, int changeValue, String remark) {
        CreditRecordEntity record = new CreditRecordEntity();
        record.setUserId(userId);
        record.setEvent(event);
        record.setValue(changeValue);
        record.setRemark(remark);
        try {
            creditRecordDao.save(record);
        } catch (Exception e) {
            throw new SedException(ErrorCode.CREDIT_SAVE_RECORD_ERROR, e);
        }
    }

}
