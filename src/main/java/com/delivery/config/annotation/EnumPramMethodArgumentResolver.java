package com.delivery.config.annotation;

import com.delivery.config.annotation.EnumParam;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author finderlo
 * @date 15/05/2017
 */
@Component
public class EnumPramMethodArgumentResolver implements HandlerMethodArgumentResolver {

    EnumPramMethodArgumentResolver(){
        System.out.println("初始化");
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(EnumParam.class)
                ;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        System.out.println("_________________开始执行");
        String name = parameter.getParameterName();
        int origin = Integer.parseInt(webRequest.getParameter(name));
        Class<?> enum1 = parameter.getParameterType();
        for (Object o : enum1.getEnumConstants()) {
            Enum e = (Enum) o;
            if (e.ordinal() == origin) {
                return e;
            }
        }
        throw new Exception("the param " + name + " has a wrong enum type origin value. Enum :" + parameter.getParameterType().getCanonicalName());
    }
}
