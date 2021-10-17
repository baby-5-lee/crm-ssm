package com.lee.crm.workbench.service.impl;

import com.github.pagehelper.PageHelper;
import com.lee.crm.workbench.dao.ContactsDao;
import com.lee.crm.workbench.dao.CustomerDao;
import com.lee.crm.workbench.dao.CustomerRemarkDao;
import com.lee.crm.workbench.dao.TranDao;
import com.lee.crm.workbench.domain.Contacts;
import com.lee.crm.workbench.domain.Customer;
import com.lee.crm.workbench.domain.CustomerRemark;
import com.lee.crm.workbench.domain.Tran;
import com.lee.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Lee
 * @date: 2021/9/14 19:23
 * @description:
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerRemarkDao customerRemarkDao;
    @Autowired
    private TranDao tranDao;
    @Autowired
    private ContactsDao contactsDao;

    @Override
    public Map<String, Object> getPageMap(int pageNo, int pageSize, Customer customer) {
        int total = customerDao.selectTotal(customer);
        int totalPage = (total - 1) / pageSize +1;
        pageNo = Math.min(pageNo, totalPage);
        PageHelper.startPage(pageNo,pageSize);
        List<Customer> customerList = customerDao.selectAllCustomers(customer);
        Map<String, Object> map = new HashMap<>(2);
        map.put("total",total);
        map.put("dataList", customerList);
        return map;
    }

    @Override
    public boolean saveCustomer(Customer customer) {
        return customerDao.insertCustomer(customer) == 1;
    }

    @Override
    public Customer getCustomer(String id) {
        return customerDao.selectById(id);
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        return customerDao.updateCustomer(customer) == 1;
    }

    @Override
    @Transactional
    public boolean removeCustomers(String[] ids) {
        int remarkCount = customerRemarkDao.countRemark(ids);
        if (remarkCount != 0){
            if(customerRemarkDao.deleteRemarks(ids) != remarkCount) {
                return false;
            }
        }
        return customerDao.deleteCustomers(ids) == ids.length;
    }

    @Override
    public Customer getCustomerDetail(String id) {
        return customerDao.getDetailById(id);
    }

    @Override
    public boolean saveRemark(CustomerRemark customerRemark) {
        return customerRemarkDao.insertRemark(customerRemark) == 1;
    }

    @Override
    public List<CustomerRemark> listRemarks(String id) {
        return customerRemarkDao.listRemarks(id);
    }

    @Override
    public boolean updateRemark(CustomerRemark remark) {
        return customerRemarkDao.updateRemark(remark) == 1;
    }

    @Override
    public boolean removeRemark(String id) {
        return customerRemarkDao.deleteRemark(id) == 1;
    }

    @Override
    public List<Tran> listTrans(String id) {
        return tranDao.listTransByCustomerId(id);
    }

    @Override
    public List<Contacts> listContacts(String id) {
        return contactsDao.listContactsByCustomerId(id);
    }
}
