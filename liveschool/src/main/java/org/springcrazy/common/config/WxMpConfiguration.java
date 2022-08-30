package org.springcrazy.common.config;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springcrazy.common.tool.ProfileConfig;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


/**
 * wechat mp configuration
 */
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpConfiguration {

    ProfileConfig profileConfig;

    @Bean
    public WxMpService wxMpService() {
        Map<String, String> config = profileConfig.getConfig(WebsiteProfile.WECHATMP);
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
        configStorage.setAppId(config.get("AppID"));
        configStorage.setSecret(config.get("AppSecret"));
        Map<String, WxMpConfigStorage> map = Maps.newHashMap();
        map.put(config.get("AppID"),configStorage);

        WxMpService service = new WxMpServiceImpl();
        service.setMultiConfigStorages(map);
        return service;
    }



}
