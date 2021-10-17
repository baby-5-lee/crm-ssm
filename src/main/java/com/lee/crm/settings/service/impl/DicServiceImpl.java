package com.lee.crm.settings.service.impl;

import com.lee.crm.settings.dao.DicTypeDao;
import com.lee.crm.settings.dao.DicValueDao;
import com.lee.crm.settings.domain.DicValue;
import com.lee.crm.settings.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Lee
 * @date: 2021/8/3 18:18
 * @description:
 */
@Service
public class DicServiceImpl implements DicService {
    @Autowired
    private DicTypeDao dicTypeDao;
    @Autowired
    private DicValueDao dicValueDao;

    @Override
    public Map<String, List<DicValue>> getDicMap() {
        List<String> codes = dicTypeDao.selectDicCode();
        Map<String, List<DicValue>> map = new HashMap<>(10);
        for (String code:codes) {
            List<DicValue> dicValueList = dicValueDao.selectByCode(code);
            map.put(code,dicValueList);
        }
        return map;
    }
}
