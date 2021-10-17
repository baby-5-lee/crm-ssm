package com.lee.crm.workbench.web.controller;
/**
 * @author: Lee
 * @date: 2021/7/24 11:03
 * @description:
 */

import com.lee.crm.settings.domain.User;
import com.lee.crm.settings.service.UserService;
import com.lee.crm.util.DateTimeUtil;
import com.lee.crm.util.UUIDUtil;
import com.lee.crm.workbench.domain.Activity;
import com.lee.crm.workbench.domain.ActivityRemark;
import com.lee.crm.workbench.service.ActivityRemarkService;
import com.lee.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/workbench/activity/")
@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private  ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;


    @GetMapping("getUserList.do")
    @ResponseBody
    public List<User> getUserList() {
        return userService.getUserList();
    }

    @PostMapping("save.do")
    @ResponseBody
    public Map<String,Boolean> saveActivity(HttpSession session, Activity act) {
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) session.getAttribute("user")).getName();
        act.setId(id);
        act.setCreateTime(createTime);
        act.setCreateBy(createBy);
        boolean success = activityService.saveActivity(act);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @GetMapping("pageList.do")
    @ResponseBody
    public Map<String, Object> getPageList(Integer pageNo, Integer pageSize, Activity act) {
        return activityService.getPageList(pageNo,pageSize,act);
    }

    @PostMapping("delete.do")
    @ResponseBody
    public Map<String,Boolean> deleteActivity(String[] id) {
        boolean success = activityService.deleteActivity(id);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @GetMapping("getActivity.do")
    @ResponseBody
    public Map<String,Object> getActivity(String id) {
        Activity act = activityService.getActivity(id);
        List<User> userList = userService.getUserList();
        Map<String,Object> map = new HashMap<>(2);
        map.put("userList",userList);
        map.put("activity",act);
        return map;
    }

    @PostMapping("update.do")
    @ResponseBody
    public Map<String,Boolean> updateActivity(Activity act, HttpSession session) {;
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) session.getAttribute("user")).getName();
        act.setEditTime(editTime);
        act.setEditBy(editBy);
        boolean success = activityService.updateActivity(act);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @GetMapping("detail.do")
    public ModelAndView doDetail(String id) {
        Activity act = activityService.getActivity(id);
        User actOwner = userService.getUserById(act.getOwner());
        ModelAndView mv = new ModelAndView("detail.jsp");
        mv.addObject("activity",act);
        mv.addObject("actOwner",actOwner);
        return mv;
    }

    @GetMapping("getRemark.do")
    @ResponseBody
    public List<ActivityRemark> getRemark(String id) {
        return activityRemarkService.getRemark(id);
    }

    @PostMapping("editRemark.do")
    @ResponseBody
    public Map<String, Boolean> editRemark(ActivityRemark remark, HttpSession session) {
        String editBy = ((User)(session.getAttribute("user"))).getName();
        String editTime = DateTimeUtil.getSysTime();
        String flag = "1";
        remark.setEditBy(editBy);
        remark.setEditTime(editTime);
        remark.setEditFlag(flag);
        boolean success = activityRemarkService.editRemark(remark);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @PostMapping("removeRemark.do")
    @ResponseBody
    public Map<String,Boolean> removeRemark(String id) {
        boolean success = activityRemarkService.removeRemark(id);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @PostMapping("saveRemark.do")
    @ResponseBody
    public Map<String, Object> saveRemark(ActivityRemark remark,HttpSession session) {
        String id = UUIDUtil.getUUID();
        String createBy = ((User)(session.getAttribute("user"))).getName();
        String createTime = DateTimeUtil.getSysTime();
        String flag = "0";
        remark.setId(id);
        remark.setCreateTime(createTime);
        remark.setCreateBy(createBy);
        remark.setEditFlag(flag);
        boolean success = activityRemarkService.saveRemark(remark);
        Map<String,Object> map = new HashMap<>(2);
        map.put("success",success);
        map.put("remark",remark);
        return map;
    }
}
