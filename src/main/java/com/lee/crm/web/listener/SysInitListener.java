package com.lee.crm.web.listener;

import com.lee.crm.settings.domain.DicValue;
import com.lee.crm.settings.service.DicService;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DicService dicService = Objects.requireNonNull(WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext())).getBean(DicService.class);
        Map<String, List<DicValue>> map = dicService.getDicMap();
        ServletContext application = sce.getServletContext();
        for(String key: map.keySet()){
            application.setAttribute(key,map.get(key));
        }
    }

}
