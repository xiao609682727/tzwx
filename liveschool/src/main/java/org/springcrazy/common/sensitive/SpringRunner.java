
package org.springcrazy.common.sensitive;

import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.xkcoding.justauth.autoconfigure.JustAuthProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import org.springcrazy.common.tool.ProfileConfig;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.pay.entity.AliPayBean;
import org.springcrazy.modules.pay.entity.WxPayBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @date 2020-09-02
 */
@Component
@AllArgsConstructor
@Slf4j
public class SpringRunner implements ApplicationRunner {

    private SensitiveWordInit sensitiveWordInit;
    ProfileConfig profileConfig;

    /**
     * 项目启动时敏感词初始化任务
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments){
        log.info("--------------------敏感词初始化开始---------------------");
        sensitiveWordInit.initKeyWord();
        log.info("--------------------敏感词初始化完成---------------------");
        //更新快捷登录信息
        log.info("--------------------快捷登录信息初始化开始---------------------");
        CrazyProperties crazyProperties = SpringUtil.getBean(CrazyProperties.class);
        String domain = crazyProperties.get("domain");
        JustAuthProperties justAuthProperties = SpringUtil.getBean(JustAuthProperties.class);
        //更新qq快捷登录信息
        Map<AuthDefaultSource, AuthConfig> type = justAuthProperties.getType();
        AuthConfig qqAuthConfig = type.getOrDefault(AuthDefaultSource.QQ,new AuthConfig());
        Map<String, String> qqLogin = ProfileConfig.getConfig("qqlogin");
        qqAuthConfig.setClientId(qqLogin.getOrDefault("qqClientId",""));
        qqAuthConfig.setClientSecret(qqLogin.getOrDefault("qqClientSecret",""));

        qqAuthConfig.setRedirectUri(qqLogin.getOrDefault("qqRedirectUri",domain + "/front/oauth/qq/callback"));
        type.put(AuthDefaultSource.QQ,qqAuthConfig);
        justAuthProperties.setType(type);

        //更新微博快捷登录信息
        AuthConfig weiboAuthConfig = type.getOrDefault(AuthDefaultSource.WEIBO,new AuthConfig());
        Map<String, String> weiboLogin = ProfileConfig.getConfig("weibologin");
        weiboAuthConfig.setClientId(weiboLogin.getOrDefault("weiboClientId",""));
        weiboAuthConfig.setClientSecret(weiboLogin.getOrDefault("weiboClientSecret",""));
        weiboAuthConfig.setRedirectUri(weiboLogin.getOrDefault("weiboRedirectUri",domain + "/front/oauth/weibo/callback"));
        type.put(AuthDefaultSource.WEIBO,weiboAuthConfig);
        justAuthProperties.setType(type);
        //更新微信快捷登录信息
        AuthConfig wechatAuthConfig = type.getOrDefault(AuthDefaultSource.WECHAT_OPEN,new AuthConfig());
        Map<String, String> wechatLogin = ProfileConfig.getConfig("wechatlogin");
        wechatAuthConfig.setClientId(wechatLogin.getOrDefault("wechatClientId",""));
        wechatAuthConfig.setClientSecret(wechatLogin.getOrDefault("wechatClientSecret",""));
        wechatAuthConfig.setRedirectUri(wechatLogin.getOrDefault("wechatRedirectUri",domain + "/front/oauth/wechat_open/callback"));
        type.put(AuthDefaultSource.WECHAT_OPEN,wechatAuthConfig);
        justAuthProperties.setType(type);
        log.info("--------------------快捷登录信息初始化完成---------------------");
        AliPayBean aliPayBean = new AliPayBean();
        Map<String,String> map = profileConfig.getConfig(WebsiteProfile.ALIPAY);
        aliPayBean.setAppId(map.get("appId"));;
        aliPayBean.setPrivateKey(map.get("privateKey"));
        aliPayBean.setPublicKey(map.get("publicKey"));

        aliPayBean.setServerUrl("https://openapi.alipay.com/gateway.do");
        AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
                .setAppId(aliPayBean.getAppId())
                .setAliPayPublicKey(aliPayBean.getPublicKey())
//                    .setAppCertPath(aliPayBean.getAppCertPath())
//                    .setAliPayCertPath(aliPayBean.getAliPayCertPath())
//                    .setAliPayRootCertPath(aliPayBean.getAliPayRootCertPath())
                .setCharset("UTF-8")
                .setPrivateKey(aliPayBean.getPrivateKey())
                .setServiceUrl(aliPayBean.getServerUrl())
                .setSignType("RSA2")
                // 普通公钥方式
                .build();
        // 证书模式
//                    .buildByCert();
        AliPayApiConfigKit.putApiConfig(aliPayApiConfig);


        WxPayBean wxPayBean = new WxPayBean();
        map = ProfileConfig.getConfig(WebsiteProfile.WECHATPAY);
        wxPayBean.setAppId(map.get("appId"));;
        wxPayBean.setAppSecret(map.get("AppSecret"));
        wxPayBean.setMchId(map.get("wxMchId"));
        wxPayBean.setPartnerKey(map.get("privateKey"));
        wxPayBean.setDomain(crazyProperties.get("domain"));

        WxPayApiConfig apiConfig = WxPayApiConfig.builder()
                .appId(wxPayBean.getAppId())
                .mchId(wxPayBean.getMchId())
                .partnerKey(wxPayBean.getPartnerKey())
                .certPath(wxPayBean.getCertPath())
                .domain(wxPayBean.getDomain())
                .build();

        WxPayApiConfigKit.putApiConfig(apiConfig);
    }
}
