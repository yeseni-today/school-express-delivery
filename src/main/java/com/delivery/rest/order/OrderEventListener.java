package com.delivery.rest.order;

import com.delivery.common.dao.OrderDao;
import com.delivery.common.entity.OrderEntity;
import com.delivery.event.Event;
import com.delivery.event.EventContext;
import com.delivery.event.EventExecutor;
import com.delivery.event.EventManager;
import com.delivery.event.context.ComplaintResultEventContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author finderlo
 * @date 16/05/2017
 */
@Component
public class OrderEventListener {

    @Autowired
    private OrderDao orderDao;


    @Autowired
    private void registerEventListener(EventManager manager) {
        manager.register(Event.ComplaintResultEvent, handleComplaintResultEvent());
    }

    private EventExecutor handleComplaintResultEvent() {
        return new EventExecutor() {
            @Override
            public void execute(Event event, EventContext context) {
                if (event != Event.ComplaintResultEvent) return;
                ComplaintResultEventContext context1 = (ComplaintResultEventContext) context;
                OrderEntity order = context1.getComplaint().getOrder();
                order.setState(context1.getOrderState());
                orderDao.update(order);
            }

        };
    }

}
