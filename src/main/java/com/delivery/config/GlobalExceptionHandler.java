package com.delivery.config;

import com.delivery.common.Response;
import com.delivery.common.SedException;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 系统异常处理，比如：404,500
     *
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        logger.error("", e);
        Response response = new Response();
        response.setMessage(e.getMessage());
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            response.setStatus(404);
        }else if (e instanceof SedException){
            SedException sed = (SedException) e;
            response.setStatus(sed.getStatus());
            response.setMessage(sed.getMessage());
        } else {
            response.setStatus(500);
        }
        response.setData(null);
        return response;
    }
}
