package org.springcrazy.modules.pay.config;

import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import lombok.AllArgsConstructor;
import org.springcrazy.common.tool.ProfileConfig;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.pay.entity.AliPayBean;
import org.springcrazy.modules.pay.entity.WxPayBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Map;

@Configuration
@AllArgsConstructor
public class PayConfig {

    CrazyProperties crazyProperties;
    ProfileConfig profileConfig;
//    AliPayBean aliPayBean;
//    WxPayBean wxPayBean;

    @Bean
    @Lazy
    public AliPayBean getAliPayBean(){
        AliPayBean aliPayBean = new AliPayBean();
        Map<String,String> map = profileConfig.getConfig(WebsiteProfile.ALIPAY);
        aliPayBean.setAppId(map.get("appId"));;
        aliPayBean.setPrivateKey(map.get("privateKey"));
        aliPayBean.setPublicKey(map.get("publicKey"));

        aliPayBean.setServerUrl("https://openapi.alipay.com/gateway.do");
//        if(SystemConstant.me().isDevMode()){
//            aliPayBean.setServerUrl("https://openapi.alipaydev.com/gateway.do");
//        }else{
//            aliPayBean.setServerUrl("https://openapi.alipay.com/gateway.do");
//        }

        aliPayBean.setDomain(crazyProperties.get("domain"));
        return aliPayBean;
    }

    @Bean
    public WxPayBean getWxPayBean(){
        WxPayBean wxPayBean = new WxPayBean();
        Map<String,String> map = ProfileConfig.getConfig(WebsiteProfile.WECHATPAY);
        wxPayBean.setAppId(map.get("appId"));;
        wxPayBean.setAppSecret(map.get("AppSecret"));
        wxPayBean.setMchId(map.get("wxMchId"));
        wxPayBean.setPartnerKey(map.get("privateKey"));
        wxPayBean.setDomain(crazyProperties.get("domain"));
        return wxPayBean;
    }

    @Bean
    public WxPayBean getWxAppPayBean(){
        WxPayBean wxPayBean = new WxPayBean();
        Map<String,String> map = ProfileConfig.getConfig(WebsiteProfile.WECHATAPPPAY);
        wxPayBean.setAppId(map.get("appId"));
        wxPayBean.setAppSecret(map.get("AppSecret"));
        wxPayBean.setMchId(map.get("wxMchId"));
        wxPayBean.setPartnerKey(map.get("privateKey"));
        wxPayBean.setDomain(crazyProperties.get("domain"));
        return wxPayBean;
    }

    @Bean
    @Lazy
    public AliPayApiConfig getAliPayApiConfig() {
        AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder();
        //判断是否填写支付宝配置如果没有则不创建
        if(Func.isNotBlank(getAliPayBean().getAppId()) || Func.isNotBlank(getAliPayBean().getPublicKey()) || Func.isNotBlank(getAliPayBean().getPrivateKey())){
            try {
                aliPayApiConfig = AliPayApiConfigKit.getApiConfig(getAliPayBean().getAppId());
            } catch (Exception e) {
                aliPayApiConfig = AliPayApiConfig.builder()
                        .setAppId(getAliPayBean().getAppId())
                        .setAliPayPublicKey(getAliPayBean().getPublicKey())
//                    .setAppCertPath(aliPayBean.getAppCertPath())
//                    .setAliPayCertPath(aliPayBean.getAliPayCertPath())
//                    .setAliPayRootCertPath(aliPayBean.getAliPayRootCertPath())
                        .setCharset("UTF-8")
                        .setPrivateKey(getAliPayBean().getPrivateKey())
                        .setServiceUrl(getAliPayBean().getServerUrl())
                        .setSignType("RSA2")
                        // 普通公钥方式
                        .build();
                // 证书模式
//                    .buildByCert();
                AliPayApiConfigKit.putApiConfig(aliPayApiConfig);

            }
        }
        return aliPayApiConfig;
    }

    @Bean
    public WxPayApiConfig getWxPayApiConfig() {
        WxPayApiConfig apiConfig;
        WxPayApiConfig apiAppConfig;


        try {
            apiConfig = WxPayApiConfigKit.getApiConfig(getWxPayBean().getAppId());
        } catch (Exception e) {
            apiConfig = WxPayApiConfig.builder()
                    .appId(getWxPayBean().getAppId())
                    .mchId(getWxPayBean().getMchId())
                    .partnerKey(getWxPayBean().getPartnerKey())
                    .certPath(getWxPayBean().getCertPath())
                    .domain(getWxPayBean().getDomain())
                    .build();
            WxPayApiConfigKit.putApiConfig(apiConfig);
        }
        try {

            apiAppConfig = WxPayApiConfigKit.getApiConfig(getWxAppPayBean().getAppId());
        } catch (Exception e) {
            apiAppConfig = WxPayApiConfig.builder()
                    .appId(getWxAppPayBean().getAppId())
                    .mchId(getWxAppPayBean().getMchId())
                    .partnerKey(getWxAppPayBean().getPartnerKey())
                    .certPath(getWxAppPayBean().getCertPath())
                    .domain(getWxAppPayBean().getDomain())
                    .build();
            WxPayApiConfigKit.putApiConfig(apiAppConfig);
        }
        return apiAppConfig;
    }

}
