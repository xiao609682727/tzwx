package org.springcrazy.common.tool;

import com.google.common.collect.Maps;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.cms.service.IWebsiteProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class ProfileConfig {
    private static Map<String,Map<String,String>> map = Maps.newHashMap();

    @Autowired
    private IWebsiteProfileService websiteProfileService;

    private static IWebsiteProfileService realWebsiteProfileService;

    @PostConstruct
    public void init() {
        realWebsiteProfileService = websiteProfileService;
    }

    public static Map<String,String> getConfig(String configType){
        Map<String, String> configMap = map.get(configType);

        if(Func.isNull(configMap)){
            Map<String, String> config = realWebsiteProfileService.getConfig(configType);
            map.put(configType,config);
            return config;
        }else{
            return configMap;
        }
    }

    public static void clearMap(){
        map.clear();
    }

}
