package com.delivery.config.annotation;

import com.delivery.common.Response;
import com.delivery.common.constant.Constant;
import com.delivery.common.constant.HttpStatus;
import com.delivery.common.dao.UserDao;
import com.delivery.common.entity.UserEntity;
import com.delivery.common.util.Assert;
import com.delivery.rest.token.TokenManager;
import com.delivery.rest.token.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author finderlo
 * @date 15/05/2017
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);
    @Autowired
    private TokenManager manager;

    @Autowired
    private UserDao userDao;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        logger.info("preHandle: request:" + request.getRequestURI() + "  " + request.getMethod());

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // get token from header
        String authorization = request.getParameter(Constant.AUTHORIZATION);
        TokenModel model = manager.getToken(authorization);
        //check authorization
        if (manager.checkToken(model)) {
            //验证成功后，如果含有管理员认证，则判断是否为管理员
            if (method.getAnnotation(AdminAuthorization.class) != null) {
                //todo 使用token存储身份，不然这样太耗时了
                logger.info("begin check admin authorization");
                UserEntity admin = userDao.findById(model.getUid());
                Assert.notNull(admin, HttpStatus.WRONG_AUGUMENT, "wrong admin uid");
                if (!admin.getIdentity().equals(UserEntity.UserIdentity.ADMINISTRATOR)
                        && admin.getUid().length() > 5) {
                    logger.warn("Admin authorization fail, user id is " + model.getUid());
                    Response response1 = new Response(HttpStatus.UNAUTHORIZATON, "Admin authorization fail, user id is " + model.getUid(), null);
                    response.getWriter().append(response1.toString()).flush();
                    return false;
                }
                logger.info("admin authorization success");
            }
            logger.info("user authorization ok, user id is " + model.getUid());
            request.setAttribute(Constant.CURRENT_USER_ID, model.getUid());
            return true;
        }

        if (method.getAnnotation(Authorization.class) != null) {
            logger.info("user authorization fail, user token model is " + model);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().append(Response.error(HttpStatus.UNAUTHORIZATON, "unauthoriation").toString()).flush();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
        logger.info("postHandle request: " + request.getRequestURI() + "  " + request.getMethod());
        logger.info("postHandle response: " + modelAndView.getModel().toString());

    }
}
