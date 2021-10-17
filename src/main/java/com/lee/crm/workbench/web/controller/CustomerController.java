package com.lee.crm.workbench.web.controller;

import com.lee.crm.settings.domain.User;
import com.lee.crm.util.DateTimeUtil;
import com.lee.crm.util.UUIDUtil;
import com.lee.crm.workbench.domain.Contacts;
import com.lee.crm.workbench.domain.Customer;
import com.lee.crm.workbench.domain.CustomerRemark;
import com.lee.crm.workbench.domain.Tran;
import com.lee.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/workbench/customer/")
@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;


    @GetMapping("pageList.do")
    public Map<String,Object> getPageList(Integer pageNo, Integer pageSize, Customer customer) {
        return customerService.getPageMap(pageNo,pageSize,customer);
    }

    @PostMapping("saveCustomer.do")
    public Map<String, Boolean> saveCustomer(HttpSession session, Customer customer) {
        String id = UUIDUtil.getUUID();
        String createBy = ((User) session.getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        customer.setId(id);
        customer.setCreateBy(createBy);
        customer.setCreateTime(createTime);
        boolean success = customerService.saveCustomer(customer);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @GetMapping("getCustomer.do")
    public Customer getCustomer(String id) {
        return customerService.getCustomer(id);
    }

    @PostMapping("updateCustomer.do")
    public Map<String, Boolean> updateCustomer(HttpSession session, Customer customer) {
        String editBy = ((User) session.getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        customer.setEditBy(editBy);
        customer.setEditTime(editTime);
        boolean success = customerService.updateCustomer(customer);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @PostMapping("removeCustomers.do")
    public Map<String, Boolean> removeCustomers(String[] id) {
        boolean success = customerService.removeCustomers(id);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @GetMapping("getCustomerDetail.do")
    public Customer getCustomerDetail(String id) {
        return customerService.getCustomerDetail(id);
    }

    @PostMapping("saveRemark.do")
    public Map<String, Boolean> saveRemark(HttpSession session, CustomerRemark customerRemark) {
        String id = UUIDUtil.getUUID();
        String createBy = ((User)(session.getAttribute("user"))).getName();
        String createTime = DateTimeUtil.getSysTime();
        String editFlag = "0";
        customerRemark.setId(id);
        customerRemark.setCreateTime(createTime);
        customerRemark.setCreateBy(createBy);
        customerRemark.setEditFlag(editFlag);
        boolean success = customerService.saveRemark(customerRemark);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @GetMapping("listRemarks.do")
    public List<CustomerRemark> listRemarks(String id) {
        return customerService.listRemarks(id);
    }

    @PostMapping("updateRemark.do")
    public Map<String, Boolean> updateRemark(HttpSession session, CustomerRemark remark) {
        String editBy = ((User)(session.getAttribute("user"))).getName();
        String editTime = DateTimeUtil.getSysTime();
        String flag = "1";
        remark.setEditBy(editBy);
        remark.setEditTime(editTime);
        remark.setEditFlag(flag);
        boolean success = customerService.updateRemark(remark);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @PostMapping("removeRemark.do")
    public Map<String, Boolean> removeRemark(String id) {
        boolean success = customerService.removeRemark(id);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @GetMapping("listTrans.do")
    public List<Tran> listTrans(String id) {
        return customerService.listTrans(id);
    }

    @GetMapping("showContacts.do")
    public List<Contacts> showContacts(String id) {
        return customerService.listContacts(id);
    }
}
