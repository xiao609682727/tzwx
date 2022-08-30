package org.springcrazy.modules.pay.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>微信配置 Bean</p>
 *
 * @author Javen
 */
@Data
@Component
public class WxPayV3Bean {
    private String keyPath;
    private String certPath;
    private String certP12Path;
    private String platformCertPath;
    private String mchId;
    private String apiKey;
    private String apiKey3;
    private String domain;


    @Override
    public String toString() {
        return "WxPayV3Bean{" +
                "keyPath='" + keyPath + '\'' +
                ", certPath='" + certPath + '\'' +
                ", certP12Path='" + certP12Path + '\'' +
                ", platformCertPath='" + platformCertPath + '\'' +
                ", mchId='" + mchId + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", apiKey3='" + apiKey3 + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}
