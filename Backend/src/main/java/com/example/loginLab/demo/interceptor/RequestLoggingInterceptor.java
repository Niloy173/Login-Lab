package com.example.loginLab.demo.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /* request record */
        String method = request.getMethod();
        String url = request.getRequestURI();
        String queryString = request.getQueryString();
        String ip = request.getRemoteAddr();

        /* extract token based information */
        Object user = request.getAttribute("username");
        String username = (user != null) ? user.toString() : "Annonymous";

        /* store the start time */
        request.setAttribute("startTime", System.currentTimeMillis());

        log.info( "{} - {} - {} - {} - {}", method, url, queryString, ip, username);

        //Store information by calling audit log/logger service


        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

       Long startTime = (Long) request.getAttribute("startTime");
       Long duration = System.currentTimeMillis() - startTime;

       log.info("Request took {} ms", duration);


        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
