package org.springcrazy.common.tool;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.StringUtil;
import org.springcrazy.modules.cms.entity.WebsiteProfile;

import java.util.HashMap;
import java.util.Map;

public class AliyunLiveUtils {

    private static final Logger log = LoggerFactory.getLogger(AliyunLiveUtils.class);

    /**
     * 是否开启鉴权
     */
    private static Boolean ALIYUN_AUTH = false;

    /**
     * 推流域名
     */
    private static String   ALIYUN_LIVE_PUSH_DOMAIN="";
    /**
     * 推流签名key
     */
    private static String    ALIYUN_LIVE_PUSH_IDENT_KEY="";
    /**
     * 拉流域名
     */
    private static String   ALIYUN_LIVE_PULL_DOMAIN="";
    /**
     * 拉流签名key
     */
    private static String   ALIYUN_LIVE_PULL_IDENT_KEY="";
    /**
     * 应用名称
     */
    private static String   ALIYUN_LIVE_APPNAME="";
    /**
     * 直播streamName
     */
    private static String  ALIYUN_LIVE_STREAMNAME="{}{}";
    /**
     * 超时时间
     */
    private static Integer  ALIYUN_LIVE_IDENT_URL_VALIDTIME =7200;
    /**
     * 直播类型
     */
    private static String  LIVE_TYPE="";


    public static void init(){
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.ALIYUN_LIVE);
        ALIYUN_AUTH = Func.equals("1",config.get("auth"));
        ALIYUN_LIVE_PUSH_DOMAIN = config.get("live_push_domain");
        ALIYUN_LIVE_PUSH_IDENT_KEY = config.get("live_push_key");
        ALIYUN_LIVE_PULL_DOMAIN = config.get("live_pull_domain");
        ALIYUN_LIVE_PULL_IDENT_KEY = config.get("live_pull_key");

        ALIYUN_LIVE_APPNAME = config.get("live_appname");
        ALIYUN_LIVE_IDENT_URL_VALIDTIME = Func.toInt(config.get("live_validtime"));
    }


    /**
     * 根据源id创建该id的推流url
     * @param sourceId 资源的id
     * @return 返回推流地址
     */
    public static String createLivePushUrl(String sourceId) {
        init();
        // 流名称
        String streamName = StringUtil.format(ALIYUN_LIVE_STREAMNAME, LIVE_TYPE, sourceId);
        // 计算过期时间
        String timestamp = String.valueOf((System.currentTimeMillis() / 1000) + ALIYUN_LIVE_IDENT_URL_VALIDTIME);

        // 组合推流域名前缀
        String rtmpUrl = StringUtil.format("rtmp://{}/{}/{}", ALIYUN_LIVE_PUSH_DOMAIN, ALIYUN_LIVE_APPNAME, streamName);
        log.debug("推流域名前缀，rtmpUrl=" + rtmpUrl);
        // 组合md5加密串
        String md5Url = StringUtil.format("/{}/{}-{}-0-0-{}", ALIYUN_LIVE_APPNAME, streamName, timestamp, ALIYUN_LIVE_PUSH_IDENT_KEY);

        // md5加密
        String md5Str = DigestUtils.md5Hex(md5Url);
        log.debug("md5加密串，md5Url=" + md5Url + "------md5加密结果，md5Str=" + md5Str);
        // 组合最终鉴权过的推流域名
        String finallyPushUrl = rtmpUrl;
        //开启鉴权
        if(ALIYUN_AUTH){
            finallyPushUrl = StringUtil.format("{}?auth_key={}-0-0-{}", rtmpUrl, timestamp, md5Str);
        }

        log.debug("最终鉴权过的推流域名=" + finallyPushUrl);

        return finallyPushUrl;
    }

    /**
     * 创建拉流域名，key=rtmpUrl、flvUrl、m3u8Url，代表三种拉流类型域名
     *
     * @param sourceId 资源的id
     * @return 返回播流地址
     */
    public static Map<String, String> createLivePullUrl(String sourceId) {
        init();
        // 流名称
        String streamName = StringUtil.format(ALIYUN_LIVE_STREAMNAME, LIVE_TYPE, sourceId);
        // 计算过期时间
        String timestamp = String.valueOf((System.currentTimeMillis() / 1000) + ALIYUN_LIVE_IDENT_URL_VALIDTIME);

        // 组合通用域名
        String pullUrl = StringUtil.format("{}/{}/{}", ALIYUN_LIVE_PULL_DOMAIN, ALIYUN_LIVE_APPNAME, streamName);
        log.debug("组合通用域名，pullUrl=" + pullUrl);

        // 组合md5加密串
        String md5Url = StringUtil.format("/{}/{}-{}-0-0-{}", ALIYUN_LIVE_APPNAME, streamName, timestamp, ALIYUN_LIVE_PULL_IDENT_KEY);
        String md5FlvUrl = StringUtil.format("/{}/{}.flv-{}-0-0-{}", ALIYUN_LIVE_APPNAME, streamName, timestamp, ALIYUN_LIVE_PULL_IDENT_KEY);
        String md5M3u8Url = StringUtil.format("/{}/{}.m3u8-{}-0-0-{}", ALIYUN_LIVE_APPNAME, streamName, timestamp, ALIYUN_LIVE_PULL_IDENT_KEY);
        // md5加密
        String md5Str = DigestUtils.md5Hex(md5Url);
        String md5FlvStr = DigestUtils.md5Hex(md5FlvUrl);
        String md5M3u8Str = DigestUtils.md5Hex(md5M3u8Url);
        log.debug("md5加密串，md5Url    =" + md5Url + "       ------     md5加密结果，md5Str=" + md5Str);
        log.debug("md5加密串，md5FlvUrl =" + md5FlvUrl + "    ------    md5加密结果，md5FlvStr=" + md5FlvStr);
        log.debug("md5加密串，md5M3u8Url=" + md5M3u8Url + "   ------    md5加密结果，md5M3u8Str=" + md5M3u8Str);

        // 组合三种拉流域名前缀
        String rtmpUrl = StringUtil.format("rtmp://{}", pullUrl);;
        String flvUrl = StringUtil.format("http://{}.flv", pullUrl);;
        String m3u8Url = StringUtil.format("http://{}.m3u8", pullUrl);;
        if(ALIYUN_AUTH){
            rtmpUrl = StringUtil.format("rtmp://{}?auth_key={}-0-0-{}", pullUrl, timestamp, md5Str);
            flvUrl = StringUtil.format("http://{}.flv?auth_key={}-0-0-{}", pullUrl, timestamp, md5FlvStr);
            m3u8Url = StringUtil.format("http://{}.m3u8?auth_key={}-0-0-{}", pullUrl, timestamp, md5M3u8Str);
        }


        log.debug("最终鉴权过的拉流rtmp域名=" + rtmpUrl);
        log.debug("最终鉴权过的拉流flv域名 =" + flvUrl);
        log.debug("最终鉴权过的拉流m3u8域名=" + m3u8Url);

        HashMap<String, String> urlMap = new HashMap<>(3);
        urlMap.put("rtmpUrl", rtmpUrl);
        urlMap.put("flvUrl", flvUrl);
        urlMap.put("m3u8Url", m3u8Url);

        return urlMap;
    }

}
