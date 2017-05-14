package com.delivery.credit;

import com.delivery.common.ErrorCode;
import com.delivery.common.SedException;
import com.delivery.common.action.Action;
import com.delivery.common.action.ActionHandler;
import com.delivery.common.Response;
import com.delivery.common.action.ActionType;
import com.delivery.common.constant.Constant;
import com.delivery.common.dao.CreditRecordDao;
import com.delivery.common.entity.ComplaintEntity;
import com.delivery.common.entity.CreditRecordEntity;
import com.delivery.common.entity.OrderEntity;
import com.delivery.common.entity.UserEntity;
import com.delivery.common.util.Util;
import com.delivery.dispatch.Dispatcher;
import com.delivery.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.delivery.common.Response.*;
import static com.delivery.common.util.UserUtil.checkAdmin;
import static com.delivery.common.util.Util.*;

/**
 * @author finderlo
 * @date 20/04/2017
 */
@Component
public class CreditSystem implements EventPublisher, ActionHandler {

    Dispatcher dispatcher;

    @Autowired
    CreditRecordDao creditRecordDao;


    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * 初始化信用体系相关的事件接收器
     *
     * @author finderlo
     * @date 02/05/2017
     */
    public void initEventListener() {
        EventManager manager = dispatcher.getEventManager();
        manager.register(Event.UserRegisterEvent, userRegisterEventExecutor());
        manager.register(Event.OrderCommentSuccessEvent, orderCommentEventExecutor());
        manager.register(Event.OrderCompleteSuccessEvent, orderCompleteSuccessEventExecutor());
        manager.register(Event.OrderReplacementCancelEvent, orderReplacementCancelEventExecutor());
        manager.register(Event.ManualHandleComplainResultEvent,manualHandleComplainResultEventEventExecutor());
    }


    @Override
    public boolean canHandleAction(Action action) {
        return action.getType().equals(ActionType.CREDIT);
    }

    @Override
    public Response execute(Action action) {
        CreditActionType type = getActionSubType(action, CreditActionType.class);
        checkNull(type);
        switch (type){
            case saveNewRecord:
                return save(action);
            case userCreditValue:
                return userCreditValue(action);
            case userRecords:
                return userRecords(action);
            default:
                return error(ErrorCode.CREDIT_UNKNOWN_ACTION_TYPE);
        }
    }

    /**
     * 保存一个信用记录信息
     *
     * @author Ticknick Hou
     * @date 02/05/2017
     */
    public Response save(Action action) {
        checkAdmin(action);
        String userId = Util.getUserId(action);
        String event = "Manual Change Value";
        int changeValue = Util.getCreditChangeValue(action);
        String remark = Util.getRemark(action);
        saveRecord(userId, event, changeValue, remark);
        return success();
    }

    /**
     * 查询某一个用户的信用值，不能指定，只能通过TOKEN
     *
     * @author Ticknick Hou
     * @date 02/05/2017
     */
    public Response userCreditValue(Action action) {
        UserEntity user = getUser(action);
        int val = creditRecordDao.getCreditValueByUserId(user.getId());
        return success(val);
    }


    /**
     * 管理员查询某一个用户的信用值，通过参数
     *
     * @author Ticknick Hou
     * @date 02/05/2017
     */
    public Response userCreditValueAdmin(Action action) {
        String userId = getUserId(action);
        checkNotEqual(userId,"",ErrorCode.SYSTEM_NULL_OBJECT);
        int val = creditRecordDao.getCreditValueByUserId(userId);
        return success(val);
    }


    /**
     * 查询某一用户的信用值变化信息
     *
     * @author Ticknick Hou
     * @date 02/05/2017
     */
    public Response userRecords(Action action) {
        UserEntity user = getUser(action);
        List<CreditRecordEntity> records = creditRecordDao.findByUserId(user.getId());
        return success(records);
    }

    /**
     * 申诉处理结果，事件执行器，主要执行信用值的记录变化
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    public EventExecutor manualHandleComplainResultEventEventExecutor() {
        EventExecutor executor = new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.ManualHandleComplainResultEvent) return;
                int credit = (int) context.get(Constant.EVENT_COMPLAINT_RESULT_CREDIT_VALUE_INT);
                ComplaintEntity complaint = (ComplaintEntity) context.get(Constant.EVENT_COMPLAINT_ENTITY);
                saveRecord(complaint.getOrder().getReplacementId(),event.name(),credit,"");
            }
        };
        return executor;
    }


    /**
     * 用户注册，事件执行器
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    public EventExecutor userRegisterEventExecutor() {
        EventExecutor executor = new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.UserRegisterEvent) return;
                UserEntity user = (UserEntity) context.get(Constant.EVENT_USER_REGISTER_USERENTITY);
                saveRecord(user.getId(), event.name(), 100, null);
            }
        };
        return executor;
    }

    /**
     * 订单评价，事件执行器
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    public EventExecutor orderCommentEventExecutor() {
        EventExecutor executor = new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.OrderCommentSuccessEvent) return;
                OrderEntity order = (OrderEntity) context.get(Constant.EVENT_ORDER_ENTITY);
                int grade = Integer.parseInt(order.getGrade());
                //todo
//                saveRecord(order.getRecipientId(), event.name(), grade - 2, order.toString());
//                saveRecord(order.getReplacementId(), event.name(), grade - 2, order.toString());
            }
        };
        return executor;
    }

    /**
     * 订单成功完成，事件执行器
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    public EventExecutor orderCompleteSuccessEventExecutor() {
        EventExecutor executor = new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.OrderCompleteSuccessEvent) return;
                OrderEntity order = (OrderEntity) context.get(Constant.EVENT_ORDER_ENTITY);
                saveRecord(order.getReplacementId(), event.name(), 1, order.toString());
                saveRecord(order.getRecipientId(), event.name(), 1, order.toString());
            }
        };
        return executor;
    }

    /**
     * 代取人取消订单，事件执行器
     *
     * @author Ticknick
     * @date 02/05/2017
     */
    public EventExecutor orderReplacementCancelEventExecutor() {
        EventExecutor executor = new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.OrderReplacementCancelEvent) return;
                String replacement = (String) context.get(Constant.EVENT_ORDER_CANCEL_TYPE_REPLACEMENT);
                saveRecord(replacement, event.name(), 100, null);
            }
        };
        return executor;
    }

    public void saveRecord(String userId, String event, int changeValue, String remark) {
        CreditRecordEntity record = new CreditRecordEntity();
        record.setUserId(userId);
        record.setEvent(event);
        record.setCreditChange(changeValue);
        record.setEventInformation(remark);
        try {
            creditRecordDao.save(record);
        } catch (Exception e) {
            throw new SedException(ErrorCode.CREDIT_SAVE_RECORD_ERROR, e);
        }
    }
}
