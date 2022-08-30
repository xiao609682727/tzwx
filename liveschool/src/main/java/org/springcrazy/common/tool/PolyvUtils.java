package org.springcrazy.common.tool;



import lombok.extern.slf4j.Slf4j;
import org.springcrazy.core.tool.utils.DigestUtil;
import org.springcrazy.core.tool.utils.OkHttpUtil;
import org.springcrazy.modules.cms.entity.WebsiteProfile;

import java.util.*;

@Slf4j
public class PolyvUtils {
	private String CREATE_CHANNEL_URL = "http://api.polyv.net/live/v2/channels/";
	private String GET_CHANNEL_URL = "http://api.polyv.net/live/v3/channel/basic/get";
	private String GET_CHANNEL_URL_PASS = "https://api.polyv.net/live/v2/channelAccount/";
    private String GET_CHANNEL_URL_PLAY_BLACK = "http://api.polyv.net/live/v2/channel/recordFile/";
    private String GET_CHANNEL_URL_SET_UP_PLAY_BLACK = "http://api.polyv.net/live/v3/channel/playback/set-setting";


	public Map<String,String> getConfig(){
		//获得 保利威 视频配置
		return ProfileConfig.getConfig(WebsiteProfile.POLYV);
	}
	public String createChannelDemo(String name,String channelPasswd,String scene) {
		Map<String, String> config = getConfig();
		String userId = config.get("userId");
		String appID = config.get("appID");
		String appSecret = config.get("appSecret");
		Map<String, String> params = new HashMap<>();
		params.put("appId", appID);
		params.put("channelPasswd", channelPasswd);
		params.put("name", name);
		params.put("scene", scene);
		params.put("timestamp", System.currentTimeMillis()+"");
		params.put("userId", userId);
		String sign = generateSign(params, appSecret);
		params.put("sign", sign);
		String result = OkHttpUtil.post(CREATE_CHANNEL_URL, params);
		log.info("create channel result=" + result);
		return result;
	}
	//获取验证的用户加密
	public static Map<String, String> createChannelDemoSign(String userId) {
		Map<String, String> params = new HashMap<>();
		params.put("userId",userId);
		Long ts =System.currentTimeMillis();
		params.put("ts",ts+"");
		PolyvUtils polyvUtils = new PolyvUtils();
		Map<String,String> datamap=polyvUtils.getConfig();
		String secretkey = datamap.get("secretkey");
		String sign = DigestUtil.md5Hex(secretkey+userId+secretkey+ts);
		params.put("sign",sign);
		return params;
	}

	public String queryChannelDemo(String channelId) {
		Map<String, String> config = getConfig();
		String userId = config.get("userId");
		String appID = config.get("appID");
		String appSecret = config.get("appSecret");
		Map<String, String> params = new HashMap<>();
		params.put("appId", appID);
		params.put("channelId", channelId);
		params.put("timestamp", System.currentTimeMillis()+"");
		params.put("userId", userId);
		String sign = generateSign(params, appSecret);
		params.put("sign", sign);
		String result = OkHttpUtil.post(GET_CHANNEL_URL, params);
		log.info("query channel result=" + result);
		return result;
	}
	/*获取的助教密码*/
	public String queryChannelDemoc(String channelId) {
		Map<String, String> config = getConfig();
		String userId = config.get("userId");
		String appID = config.get("appID");
		String appSecret = config.get("appSecret");
		String time =System.currentTimeMillis()+"";
		Map<String, String> params = new HashMap<>();
		params.put("appId", appID);
		params.put("account", "001"+channelId);
		params.put("timestamp", time);
		params.put("userId", userId);
		String sign = generateSign(params, appSecret);
		params.put("sign", sign);
		String queryString = "appId="+appID+"&account="+"001"+channelId+"&timestamp="+time+"&userId="+userId+"&sign="+sign;
		String result = OkHttpUtil.post(GET_CHANNEL_URL_PASS+channelId+"/account?"+queryString,null );
		log.info("query account result=" + result);
		return result;
	}

    public String queryChannePlayback(String channelId) {
		Map<String, String> config = getConfig();
		String userId = config.get("userId");
		String appID = config.get("appID");
		String appSecret = config.get("appSecret");
        String time =System.currentTimeMillis()+"";
        Map<String, String> params = new HashMap<>();
        params.put("appId", appID);
        params.put("timestamp", time);
        params.put("userId", userId);
        params.put("page","1");
        params.put("pageSize","20");
        params.put("listType","playback");
        String sign = generateSign(params, appSecret);
        params.put("sign", sign);
        String queryString = "appId="+appID+"&timestamp="+time+"&userId="+userId+"&page=1"+"&pageSize=20"+"&listType=playback"+"&sign="+sign;
        String result = OkHttpUtil.post(GET_CHANNEL_URL_PLAY_BLACK+channelId+"/playback/list?"+queryString, null);
        log.info("query account result=" + result);
        return result;
    }

    public String queryChanneSetUpPlayback(String channelId,String videoId) {
		Map<String, String> config = getConfig();
		String userId = config.get("userId");
		String appID = config.get("appID");
		String appSecret = config.get("appSecret");

        Map<String, String> params = new HashMap<>();
        params.put("appId", appID);
        params.put("channelId", channelId);
        params.put("playbackEnabled", "Y");
        params.put("type","single");
        params.put("origin","playback");
        params.put("videoId",videoId);
        params.put("timestamp", System.currentTimeMillis()+"");
        params.put("userId", userId);
        String sign = generateSign(params, appSecret);
        params.put("sign", sign);
        String result = OkHttpUtil.post(GET_CHANNEL_URL_SET_UP_PLAY_BLACK, params);
        log.info("query channel result=" + result);
        return result;
    }

	protected static String generateSign(Map<String, String> parray, String secretKey) {
		Map<String, String> params = paraFilter(parray);
		String concatedStr = concatParams(params);
		String plain = secretKey + concatedStr + secretKey;
		String encrypted = DigestUtil.md5Hex(plain);
		String upperCase = encrypted.toUpperCase();
		return upperCase;
	}

	private static Map<String, String> paraFilter(Map<String, String> sArray) {
		Map<String, String> result = new HashMap<String, String>();
		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}

	private static String concatParams(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			sb.append(key).append(value);
		}
		return sb.toString();
	}

}