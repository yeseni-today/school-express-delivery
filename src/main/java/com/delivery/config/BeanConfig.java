package com.delivery.config;

import com.delivery.common.action.ActionHandler;
import com.delivery.common.util.Timer;
import com.delivery.common.util.TimerImpl;
import com.delivery.credit.CreditSystem;
import com.delivery.dispatch.Dispatcher;
import com.delivery.dispatch.DispatcherImpl;
import com.delivery.event.EventManager;
import com.delivery.event.EventManagerImpl;
import com.delivery.funds.FundsService;
import com.delivery.manual.ManualService;
import com.delivery.message.MessageService;
import com.delivery.order.OrderService;
import com.delivery.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;

/**
 * @author finderlo
 * @date 22/04/2017
 */
@Configuration
public class BeanConfig {


//    @Bean
//    public UserService userService(){
//        return new UserService();
//    }
//
//    @Bean
//    public OrderService orderService(){
//        return new OrderService();
//    }
//
    @Bean
    public EventManager eventManager(){
        return new EventManagerImpl();
    }


    @Bean
    public Dispatcher dispatcher(
            OrderService orderService,
            UserService userService,
            EventManager eventManager,
            CreditSystem creditSystem,
            FundsService fun,
            ManualService manualService,
            MessageService service){

        //create a dispatcher
        DispatcherImpl dispatcher = new DispatcherImpl();

        //set up link from dispatcher to module
        dispatcher.setEventManager(eventManager);
        dispatcher.setOrderService(orderService);
        dispatcher.setTimer(new TimerImpl(dispatcher));
        dispatcher.setUserService(userService);

        //set up link from module to dispatcher
        creditSystem.setDispatcher(dispatcher);
        orderService.setDispatcher(dispatcher);
        userService.setDispatcher(dispatcher);

        //add all modules to the dispatcher
        ArrayList<ActionHandler> handlers = new ArrayList<>();
        handlers.add(orderService);
        handlers.add(userService);
        handlers.add(creditSystem);
        handlers.add(fun);
        handlers.add(manualService);
        handlers.add(service);
        dispatcher.setHandlers(handlers);

        //after,initialize module that need initialize
        creditSystem.initEventListener();

        return dispatcher;
    }
}
