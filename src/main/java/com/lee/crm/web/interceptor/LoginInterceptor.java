package com.lee.crm.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author: Lee
 * @date: 2021/10/13 15:59
 * @description:
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("user") != null){
            return true;
        }else {
            response.sendRedirect(request.getContextPath()+"/login.html");
            return false;
        }
    }
}
