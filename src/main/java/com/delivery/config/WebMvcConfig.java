package com.delivery.config;

import com.delivery.config.annotation.AuthorizationInterceptor;
import com.delivery.config.annotation.CurrentUserMethodArgumentResolver;
import com.delivery.config.annotation.EnumPramMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author finderlo
 * @date 15/05/2017
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    EnumPramMethodArgumentResolver enumPramMethodArgumentResolver;

    @Autowired
    CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver;

    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    /**
     * 增加参数解析器
     * @author Ticknick Hou
     * @date 15/05/2017
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(enumPramMethodArgumentResolver);
        argumentResolvers.add(currentUserMethodArgumentResolver);
        super.addArgumentResolvers(argumentResolvers);
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor);
    }
}
