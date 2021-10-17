package com.lee.crm.workbench.service.impl;

import com.github.pagehelper.PageHelper;
import com.lee.crm.settings.dao.UserDao;
import com.lee.crm.settings.domain.User;
import com.lee.crm.util.DateTimeUtil;
import com.lee.crm.util.UUIDUtil;
import com.lee.crm.workbench.dao.*;
import com.lee.crm.workbench.domain.*;
import com.lee.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Lee
 * @date: 2021/8/3 17:47
 * @description:
 */
@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueDao clueDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ClueRemarkDao clueRemarkDao;
    @Autowired
    private ClueActivityRelationDao clueActivityRelationDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private ContactsDao contactsDao;
    @Autowired
    private CustomerRemarkDao customerRemarkDao;
    @Autowired
    private ContactsRemarkDao contactsRemarkDao;
    @Autowired
    private ContactsActivityRelationDao contactsActivityRelationDao;
    @Autowired
    private TranDao tranDao;
    @Autowired
    private TranHistoryDao tranHistoryDao;

    @Override
    public boolean saveClue(Clue clue) {
        return clueDao.insertClue(clue)==1;
    }

    @Override
    public Map<String, Object> getPage(int pageNo, int pageSize, Clue clue) {
        Map<String,Object> map = new HashMap<>(2);
        map.put("total",clueDao.selectTotal(clue));
        PageHelper.startPage(pageNo,pageSize);
        map.put("dataList",clueDao.selectPageList(clue));
        return map;
    }

    @Override
    public Map<String, Object> getClue(String id) {
        Clue clue = clueDao.selectById(id);
        List<User> userList = userDao.getUserList();
        Map<String, Object> map = new HashMap<>(2);
        map.put("clue",clue);
        map.put("userList",userList);
        return map;
    }

    @Override
    public boolean updateClue(Clue clue) {
        return clueDao.updateClue(clue) == 1;
    }

    @Override
    @Transactional
    public boolean removeClue(String[] ids) {
        clueRemarkDao.deleteByIds(ids);
        clueActivityRelationDao.deleteByIds(ids);
        return clueDao.deleteByIds(ids)==ids.length;
    }

    @Override
    public Map<String, Object> getDetail(String id) {
        Map<String,Object> map = new HashMap<>(3);
        Clue clue = clueDao.selectClue(id);
        List<ClueRemark> clueRemarkList = clueRemarkDao.selectByClue(id);
        List<Activity> activityList = activityDao.selectActivity(id);
        map.put("clue",clue);
        map.put("remarkList",clueRemarkList);
        map.put("activityList",activityList);
        return map;
    }

    @Override
    public boolean saveRemark(ClueRemark remark) {
        return clueRemarkDao.insertRemark(remark)==1;
    }

    @Override
    public boolean editRemark(ClueRemark remark) {
        return clueRemarkDao.updateRemark(remark)==1;
    }

    @Override
    public boolean removeRemark(String id) {
        return clueRemarkDao.deleteById(id) == 1;
    }

    @Override
    public boolean relateActs(String cid, String[] aids) {
        List<ClueActivityRelation> list = new ArrayList<>(aids.length);
        for (String aid : aids) {
            String id = UUIDUtil.getUUID();
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(id);
            clueActivityRelation.setClueId(cid);
            clueActivityRelation.setActivityId(aid);
            list.add(clueActivityRelation);
        }

        return clueActivityRelationDao.insertList(list)==list.size();
    }

    @Override
    public List<Activity> getActList(String id) {
        return activityDao.selectActivity(id);
    }

    @Override
    public boolean removeRelation(ClueActivityRelation relation) {
        return clueActivityRelationDao.deleteRelation(relation)==1;
    }

    @Override
    @Transactional
    public boolean clueConvert(String clueId, Tran tran) {
        Clue clue = clueDao.selectById(clueId);
        String createTime = DateTimeUtil.getSysTime();
        String createBy = tran.getCreateBy();
        String cusId = customerDao.selectIdByName(clue.getCompany());
        int cusNum = 1;
        if (cusId==null) {
            cusId = UUIDUtil.getUUID();
            Customer customer = new Customer();
            customer.setId(cusId);
            customer.setOwner(clue.getOwner());
            customer.setName(clue.getCompany());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());
            cusNum = customerDao.insertCustomer(customer);
        }

        String conId = UUIDUtil.getUUID();
        Contacts contacts = new Contacts();
        contacts.setId(conId);
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(cusId);
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        int conNum = contactsDao.insertContacts(contacts);

        List<ClueRemark> clueRemarkList = clueRemarkDao.selectByClue(clueId);
        for(ClueRemark remark:clueRemarkList){
            remark.setClueId(cusId);
        }
        int cur = 0;
        if (clueRemarkList.size() != 0){
            cur = customerRemarkDao.insertList(clueRemarkList);
        }

        for(ClueRemark remark:clueRemarkList){
            remark.setClueId(conId);
        }

        int cnr = 0;
        if (clueRemarkList.size() != 0){
            cnr = contactsRemarkDao.insertList(clueRemarkList);
        }

        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.selectByClueId(clueId);
        for(ClueActivityRelation relation:clueActivityRelationList){
            relation.setClueId(conId);
        }
        int car = 0;
        if(clueActivityRelationList.size() != 0){
            car = contactsActivityRelationDao.insertList(clueActivityRelationList);
        }

        int clueNum = 0;
        if (cusNum==1 && conNum==1 && cur==clueRemarkList.size() && cnr==clueRemarkList.size() && car==clueActivityRelationList.size()){
            clueActivityRelationDao.deleteByClueId(clueId);
            clueRemarkDao.deleteByClueId(clueId);
            clueNum = clueDao.deleteById(clueId);
        }

        int tranNum = 1;
        if (tran.getId()!=null){
            tran.setContactsId(conId);
            tran.setCustomerId(cusId);
            if (tranDao.insertTran(tran)==1){
                TranHistory history = new TranHistory();
                history.setId(UUIDUtil.getUUID());
                history.setStage(tran.getStage());
                history.setMoney(tran.getMoney());
                history.setExpectedDate(tran.getExpectedDate());
                history.setCreateTime(tran.getCreateTime());
                history.setCreateBy(createBy);
                history.setTranId(tran.getId());
                tranNum = tranHistoryDao.insertHistory(history);
            }
        }
        return clueNum==1 && tranNum==1;
    }

    @Override
    public List<Activity> getBoundAct(String id,String name) {
        return activityDao.selectBoundActivity(id,name);
    }
}
