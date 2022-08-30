package org.springcrazy.common.tool;

import org.springcrazy.core.tool.sms.IMsgSender;
import org.springcrazy.core.tool.sms.sender.AliyunSMS;
import org.springcrazy.core.tool.sms.sender.TencentSMS;
import org.springcrazy.modules.cms.entity.WebsiteProfile;

import java.util.Map;

/**
 * 消息发送器工厂类
 */
public class MsgSMSFactory {

    /**
     * 根据消息类型获取对应的消息发送器
     *
     * @return IMsgSender
     */
    public static IMsgSender getMsgSender() {
        IMsgSender iMsgSender = null;
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.SMS);
        String supplier = config.get("sms_supplier");
        String appid = config.get("appid");
        String appkey = config.get("appkey");
        String sdkAppId = config.get("sdkAppId");
        switch (supplier) {
            case WebsiteProfile.ALIYUN:
                iMsgSender = new AliyunSMS(appid,appkey);
                break;
            case WebsiteProfile.TENCENTYUN:
                iMsgSender = new TencentSMS(appid,appkey,sdkAppId);
                break;
            default:
                break;
        }
        return iMsgSender;
    }

    /**
     * 根据消息类型获取对应的消息发送器
     *
     * @return IMsgSender
     */
    public static IMsgSender getMsgSender(String supplier,String appid,String appkey,String sdkAppId) {
        IMsgSender iMsgSender = null;
        switch (supplier) {
            case WebsiteProfile.ALIYUN:
                iMsgSender = new AliyunSMS(appid,appkey);
                break;
            case WebsiteProfile.TENCENTYUN:
                iMsgSender = new TencentSMS(appid,appkey,sdkAppId);
                break;
            default:
                break;
        }
        return iMsgSender;
    }



}
