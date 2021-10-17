package com.lee.crm.settings.web.controller;

import com.lee.crm.settings.domain.User;
import com.lee.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/settings/user/")
public class UserController{
    @Autowired
    UserService userService;

    @PostMapping("login.do")
    public Map<String,Object> login(HttpServletRequest request, String loginAct, String loginPwd) {
        String ip = request.getRemoteAddr();
        Map<String,Object> map = new HashMap<>(2);
        try{
            User user = userService.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            map.put("success",true);
            return map;
        }catch (Exception e){
            String msg = e.getMessage();
            map.put("success",false);
            map.put("msg",msg);
            return map;
        }
    }

    @GetMapping("getUserList.do")
    public List<User> getUserList() {
        return userService.getUserList();
    }

    @GetMapping("getCurrentUser.do")
    private User getCurrentUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }
}
