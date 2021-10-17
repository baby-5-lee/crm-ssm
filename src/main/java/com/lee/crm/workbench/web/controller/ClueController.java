package com.lee.crm.workbench.web.controller;

import com.lee.crm.settings.domain.User;
import com.lee.crm.settings.service.UserService;
import com.lee.crm.util.DateTimeUtil;
import com.lee.crm.util.UUIDUtil;
import com.lee.crm.workbench.domain.*;
import com.lee.crm.workbench.service.ActivityService;
import com.lee.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/workbench/clue/")
@Controller
public class ClueController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ActivityService activityService;

    @GetMapping("getUserList.do")
    @ResponseBody
    public List<User> getUserList() {
        return userService.getUserList();
    }

    @PostMapping("saveClue.do")
    @ResponseBody
    public Map<String,Boolean> saveClue(Clue clue, HttpSession session) {
        String createBy = ((User)(session.getAttribute("user"))).getName();
        String createTime = DateTimeUtil.getSysTime();
        String id = UUIDUtil.getUUID();
        clue.setId(id);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        boolean success = clueService.saveClue(clue);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @GetMapping("pageList.do")
    @ResponseBody
    public Map<String,Object> getPageList(Integer pageNo, Integer pageSize, Clue clue) {
        return clueService.getPage(pageNo,pageSize,clue);
    }

    @GetMapping("getClue.do")
    @ResponseBody
    public Map<String,Object> getClue(String id) {
        return clueService.getClue(id);
    }

    @PostMapping("updateClue.do")
    @ResponseBody
    public Map<String, Boolean> updateClue(HttpSession session, Clue clue) {
        String editBy = ((User)(session.getAttribute("user"))).getName();
        String editTime = DateTimeUtil.getSysTime();
        clue.setEditBy(editBy);
        clue.setEditTime(editTime);
        boolean success = clueService.updateClue(clue);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @PostMapping("remove.do")
    @ResponseBody
    public Map<String, Boolean> removeClue(String[] id) {
        boolean success = clueService.removeClue(id);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @GetMapping("showDetail.do")
    @ResponseBody
    public Map<String, Object> showDetail(String id) {
        return clueService.getDetail(id);
    }

    @PostMapping("saveRemark.do")
    @ResponseBody
    public Map<String, Object> saveRemark(HttpSession session, ClueRemark remark) {
        String id = UUIDUtil.getUUID();
        String createBy = ((User)(session.getAttribute("user"))).getName();
        String createTime = DateTimeUtil.getSysTime();
        String flag = "0";
        remark.setId(id);
        remark.setCreateTime(createTime);
        remark.setCreateBy(createBy);
        remark.setEditFlag(flag);
        boolean success = clueService.saveRemark(remark);
        Map<String,Object> map = new HashMap<>(2);
        map.put("success",success);
        map.put("remark",remark);
        return map;
    }

    @PostMapping("editRemark.do")
    @ResponseBody
    public Map<String, Boolean> editRemark(HttpSession session, ClueRemark remark) {
        String editBy = ((User)(session.getAttribute("user"))).getName();
        String editTime = DateTimeUtil.getSysTime();
        String flag = "1";
        remark.setEditBy(editBy);
        remark.setEditTime(editTime);
        remark.setEditFlag(flag);
        boolean success = clueService.editRemark(remark);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @PostMapping("removeRemark.do")
    @ResponseBody
    public Map<String, Boolean> removeRemark(String id) {
        boolean success = clueService.removeRemark(id);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @GetMapping("getActList.do")
    @ResponseBody
    public List<Activity> getActList(String id, String name) {
        return activityService.getActList(id,name);
    }

    @PostMapping("relate.do")
    @ResponseBody
    public Map<String, Boolean> relate(String cid, String[] aid) {
        boolean success = clueService.relateActs(cid,aid);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @GetMapping("showAct.do")
    @ResponseBody
    public List<Activity> showAct(String id) {
        return clueService.getActList(id);
    }

    @PostMapping("removeRelation.do")
    @ResponseBody
    public Map<String, Boolean> removeRelation(ClueActivityRelation relation) {
        boolean success = clueService.removeRelation(relation);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @GetMapping("getBoundAct.do")
    @ResponseBody
    public List<Activity> getBoundAct(String id, String name) {
        return clueService.getBoundAct(id,name);
    }

    @RequestMapping("convert.do")
    public String convert(HttpServletRequest request, HttpSession session,String clueId,Tran tran) throws IOException {
        String createBy = ((User)(session.getAttribute("user"))).getName();
        tran.setCreateBy(createBy);
        if ("POST".equals(request.getMethod())){
            tran.setId(UUIDUtil.getUUID());
            tran.setType("新业务");
            tran.setCreateTime(DateTimeUtil.getSysTime());
        }
        clueService.clueConvert(clueId,tran);
        return "redirect:index.jsp";
    }
}
