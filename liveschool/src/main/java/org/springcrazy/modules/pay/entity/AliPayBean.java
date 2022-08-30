package org.springcrazy.modules.pay.entity;

import lombok.Data;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>支付宝配置 Bean</p>
 *
 * @author Javen
 */
@Data
public class AliPayBean {

    private String appId;
    private String privateKey;
    private String publicKey;
    private String appCertPath;
    private String aliPayCertPath;
    private String aliPayRootCertPath;
    private String serverUrl;
    private String domain;

    @Override
    public String toString() {
        return "AliPayBean{" +
                "appId='" + appId + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", appCertPath='" + appCertPath + '\'' +
                ", aliPayCertPath='" + aliPayCertPath + '\'' +
                ", aliPayRootCertPath='" + aliPayRootCertPath + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}
