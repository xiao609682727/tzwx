package org.springcrazy.common.tool;

import cn.hutool.http.HttpUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.tool.jackson.JsonUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.edu.entity.LiveRoom;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Created by Administrator on 2017/3/7.
 */
@Slf4j
public class BajiayunUtil {

    /*获取获取回放token*/
    public static  String getBackToken(String room_id){
        SortedMap map = new TreeMap<>();
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        String partnerId = config.get("PartnerId");
        map.put("partner_id",partnerId);
        map.put("room_id",room_id);
        map.put("expires_in","0");
        map.put("timestamp",new Date().getTime()/1000+"");

        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/playback/getPlayerToken",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            return  "";
        }
        Map data = (Map) tokenMap.get("data");
        return baijiayunURL + "/web/playback/index?classid="+room_id+"&token="+data.get("token");
    }

    /*获取播放token*/
    public static  String getToken(String videoId) {
        SortedMap map = new TreeMap<>();

        map.put("video_id",videoId);
        map.put("expires_in","0");
        map.put("timestamp",new Date().getTime()/1000+"");
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/video/getPlayerToken",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            throw new ServiceException(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data.get("token")+"";
    }

    /*创建房间签名*/
    public static  String getUrlCreateSign(SortedMap<String,String> message,Map<String, String> config){
        String origin_str = "";
        if (Func.isBlank(config.get("PartnerId"))||Func.isBlank(config.get("PartnerKey"))){
            //message.put("partner_id","");
            origin_str = createBaijiayunSign(message,"");
        }else {
            //message.put("partner_id",config.get("PartnerId"));
            origin_str = createBaijiayunSign(message,config.get("PartnerKey"));
        }
        return origin_str;
    }

    /*创建房间签名*/
    public static  String createSign(SortedMap<String,String> message,Map<String, String> config){
        String origin_str = "";
        if (Func.isBlank(config.get("PartnerId"))||Func.isBlank(config.get("PartnerKey"))){
            message.put("partner_id","");
            origin_str = createBaijiayunSign(message,"");
        }else {
            message.put("partner_id",config.get("PartnerId"));
            origin_str = createBaijiayunSign(message,config.get("PartnerKey"));
        }
        return origin_str;
    }
    /**
     * 创建百家云签名
     */
    public static String createBaijiayunSign(SortedMap<String, String> packageParams,String key) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String,String>> es = packageParams.entrySet();
        Iterator<Map.Entry<String,String>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String,String> entry = it.next();
            String k = entry.getKey();
            String v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)&& !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        if (Func.isNotEmpty(key)){
            sb.append("partner_key="+key);
        }
        String sign = getMD5(sb.toString());
        return sign;

    }
    /*获取百家云房间url*/
    public static String roomUrl(String roomId, String type, String userType,Map<String,String> userMap) {
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        String  baijiayunURL = config.get("URL").toString();
        if(Func.isEmpty(baijiayunURL)){
            baijiayunURL = "api.baijiacloud.com";
        }

        SortedMap<String,String> map = new TreeMap<>();
        DecimalFormat df = new DecimalFormat("##0");
        map.put("room_id",roomId);
        if ("student".equals(userType)){
            /*获取学员信息*/
            map.put("user_number",userMap.get("userId")+"");
            map.put("user_name",userMap.get("userName"));
            map.put("user_role",0+"");
        }else if ("teacher".equals(userType)){
            /*获取学员信息*/
            map.put("user_number",userMap.get("userId")+"");
            map.put("user_name",userMap.get("userName"));
            map.put("user_role",1+"");
        }else {
            /*获取助教信息*/
            map.put("user_number",userMap.get("userId")+"");
            map.put("user_name",userMap.get("userName"));
            map.put("user_role",2+"");
        }

        String sign =  getUrlCreateSign(map,config);
            /*参数*/
        String msg="";
            /*如果是WEB为网页端*/
        if ("WEB".equals(type)){
            msg = baijiayunURL + "/web/room/enter?"+ maptoString(map,sign);
            /*如果是APP为客户端*/
        }else if ("APP".equals(type)){
            msg = baijiayunURL + "/web/room/enter?"+ maptoString(map,sign);
                /*客户端请求需要url转码 和在请求前加baijiacloud://urlpath=*/
            try {
                msg = URLEncoder.encode(msg,"utf8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            msg = "baijiacloud://urlpath="+msg+"&token=token&ts=ts";
        }
        return msg;
    }
    /**
     * map返回string
     */
    public static String maptoString(Map<String, String> packageParams,String key)  {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String,String>> es = packageParams.entrySet();
        Iterator<Map.Entry<String,String>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String,String> entry = it.next();
            String k = entry.getKey();
            String v = null;
            try {
                v = URLEncoder.encode(entry.getValue(),"utf8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (null != v && !"".equals(v) && !"sign".equals(k)&& !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("sign="+key);
        return sb.toString();

    }



    public final static String getMD5(String s){

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd','e', 'f'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>>4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (Exception e){
            return null;
        }
    }


    /**
     * 创建房间
     * @return
     */
    public static  Map createRoom(LiveRoom liveRoom){

        String title = liveRoom.getName();
        long startTime=liveRoom.getStartTime().toEpochSecond(ZoneOffset.of("+8"));
        long endTime=liveRoom.getEndTime().toEpochSecond(ZoneOffset.of("+8"));

        String autoPlaybackRecord = "1";
        if(Func.isNotEmpty(liveRoom.getAutoPlaybackRecord())){
            autoPlaybackRecord = liveRoom.getAutoPlaybackRecord();
        }
        String endDelayTime = "0";
        if(Func.isNotEmpty(liveRoom.getEndDelayTime())){
            endDelayTime = liveRoom.getEndDelayTime();
        }
        String mIsVideoMain = "1";
        if(Func.isNotEmpty(liveRoom.getMobileIsVideoMain())){
            mIsVideoMain = liveRoom.getMobileIsVideoMain();
        }
        String isVideoMain = "2";
        if(Func.isNotEmpty(liveRoom.getIsVideoMain())){
            isVideoMain = liveRoom.getIsVideoMain();
        }



        SortedMap map = new TreeMap<>();

        map.put("title",title);
        map.put("start_time",startTime+"");
        map.put("end_time",endTime+"");
        //2:普通大班课
        map.put("type","2");
        //学生可提前进入的时间，单位为秒
        map.put("pre_enter_time","300");
        //是否是长期房间，0:普通房间(注：普通房间时长小于24小时)
        map.put("is_long_term","0");
        //可选值，教育直播：doubleCamera(双摄像头)、classic(经典模板)、triple(三分屏)、liveWall(视频墙)，默认triple;
        map.put("template_name","triple");
        //指定PC端是否以视频为主 1:以视频为主 2:以PPT为主 （默认是以ppt为主，该选项只针对三分屏有效）
        map.put("is_video_main",isVideoMain);
        //指定手机H5页面是否以视频为主 1:以视频为主 2:以PPT为主 （默认是以视频为主）
        map.put("m_is_video_main",mIsVideoMain);
        //课程预设的结束时间后可以拖堂的时间，到时间会强制下课，单位（秒），0不强制，大于0生效,最大不可超过7200秒（两小时）
        map.put("end_delay_time",endDelayTime);
        //普通教室教室级别控制是否自动录制。0：默认，读取后台配置 1：开 2：关
        map.put("auto_playback_record",autoPlaybackRecord);

        map.put("timestamp",new Date().getTime()/1000+"");
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/room/create",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            throw new ServiceException(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data;
    }


    /**
     * 创建房间
     * @param title
     * @param startTime
     * @param endTime
     * @return
     */
    public static  Map updateRoom(String roomId,String title,long startTime ,long endTime){
        SortedMap map = new TreeMap<>();
        map.put("room_id",roomId);
        map.put("title",title);
        map.put("start_time",startTime+"");
        map.put("end_time",endTime+"");
        map.put("timestamp",new Date().getTime()/1000+"");
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/room/update",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            throw new ServiceException(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data;
    }


    /**
     * 创建房间
     * @return
     */
    public static  Map deleteRoom(String roomId){
        SortedMap map = new TreeMap<>();
        map.put("room_id",roomId);
        map.put("timestamp",new Date().getTime()/1000+"");
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/room/delete",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            throw new ServiceException(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data;
    }

    /**
     * 获取子账号信息
     * @return
     */
    public static  Map getAccountInfo(String partnerId){
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        SortedMap map = new TreeMap<>();
        map.put("partner_id",partnerId);
        map.put("sub_partner_id",config.get("PartnerId"));
        map.put("timestamp",new Date().getTime()/1000+"");

        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/new_sub/getAccountInfo",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            throw new ServiceException(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data;
    }

    /**
     * 获取视频/音频上传地址
     * @return
     */
    public static  Map getUploadUrl(String fileName){
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        SortedMap map = new TreeMap<>();
        map.put("partner_id",config.get("PartnerId"));
        map.put("file_name",fileName);
        //清晰度调整
        //map.put("definition","16,1,2,4,8");
        map.put("definition","16");
        map.put("timestamp",new Date().getTime()/1000+"");
        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/video/getUploadUrl",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            throw new ServiceException(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data;
    }


    /**
     * 获取断点续传地址
     * @return
     */
    public static  Map getResumeUploadUrl(String videoId){
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        SortedMap map = new TreeMap<>();
        map.put("partner_id",config.get("PartnerId"));
        map.put("video_id",videoId);
        map.put("timestamp",new Date().getTime()/1000+"");
        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/video/getResumeUploadUrl",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            throw new ServiceException(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data;
    }

    /**
     * 获取指定ID视频信息
     * @return
     */
    public static  Map getInfo(String videoId){
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        log.info("百家云配置信息："+JsonUtil.toJson(config));
        SortedMap map = new TreeMap<>();
        map.put("partner_id",config.get("PartnerId"));
        map.put("video_id",videoId);
        map.put("timestamp",new Date().getTime()/1000+"");
        map.put("sign", BajiayunUtil.createSign(map,config));


        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/video/getInfo",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            throw new ServiceException(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data;
    }

    /**
     * 查询转码后的mp4地址
     * @return
     */
    public static  String getUrl(String videoId){
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        SortedMap map = new TreeMap<>();
        map.put("partner_id",config.get("PartnerId"));
        map.put("video_id",videoId);
        map.put("timestamp",new Date().getTime()/1000+"");
        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/video/getUrl",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            log.error(tokenMap.get("msg")+"");
        }
        Map<String,Object> data = (Map<String, Object>) tokenMap.get("data");
        Map<String,Object> playInfo = (Map<String, Object>) data.get("play_info");
        Map<String,Object> low = (Map<String, Object>)  playInfo.get("low");
        return low.get("url").toString();
    }

    /**
     * 删除视频
     * @return
     */
    public static  Map delete(String videoId){
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        SortedMap map = new TreeMap<>();
        map.put("partner_id",config.get("PartnerId"));
        map.put("video_id",videoId);
        map.put("timestamp",new Date().getTime()/1000+"");
        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/video/delete",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            log.error(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data;
    }


    /**
     * 查询直播回放信息
     * @return
     */
    public static  Map getBasicInfo (String roomId){
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        SortedMap map = new TreeMap<>();
        map.put("partner_id",config.get("PartnerId"));
        map.put("room_id",roomId);
        map.put("timestamp",new Date().getTime()/1000+"");
        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/playback/getBasicInfo",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            log.error(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data;
    }


    /**
     * 查询直播回放信息
     * @return
     */
    public static  Map playbackDelete (String roomId){
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        SortedMap map = new TreeMap<>();
        map.put("partner_id",config.get("PartnerId"));
        map.put("room_id",roomId);
        map.put("timestamp",new Date().getTime()/1000+"");
        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/playback/delete",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            log.error(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data;
    }

    /**
     * 获取子账号点播信息
     * @return
     */
    public static  Map getVodAccountInfo(String partnerId){
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        SortedMap map = new TreeMap<>();
        map.put("partner_id",partnerId);
        map.put("timestamp",new Date().getTime()/1000+"");
        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/video_account/getAccountInfo",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            log.error(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data;
    }


    /**
     * 配置点播回放的回调地址
     * @return
     */
    public static  Map setTranscodeCallbackUrl(){
        CrazyProperties crazyProperties = SpringUtil.getBean(CrazyProperties.class);
        Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VIDEO);
        SortedMap map = new TreeMap<>();
        map.put("partner_id",config.get("PartnerId"));
        map.put("timestamp",new Date().getTime()/1000+"");
        String domain = crazyProperties.get("domain");
        map.put("url",domain + "/front/playback");
        map.put("sign", BajiayunUtil.createSign(map,config));
        /*获取视频token*/
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/video_account/setTranscodeCallbackUrl",map);
        log.info("接口返回："+roomMsg);
        Map<String,Object> tokenMap = JsonUtil.toMap(roomMsg);
        if(!tokenMap.get("code").equals(0)){
            log.error(tokenMap.get("msg")+"");
        }
        Map data = (Map) tokenMap.get("data");
        return data;
    }

    public static void main(String[] args) {
/*        Map<String, String> config = Maps.newHashMap();
        config.put("URL","https://e45480488.at.baijiayun.com/");
        config.put("PartnerId","45480488");
        config.put("SecretKey","de78c615153d407f7f2d72b67d99b9aa");
        config.put("PartnerKey","pdbyc9YAbPBM1nUYSK8E98rV+QGuUq0+kByjBcFJlS+4v3eC/HY+Kn8htAZOX+slaps2mEH1bjxl9V20uKyF48eLHjiK");
        SortedMap map = new TreeMap<>();
        map.put("partner_id",config.get("PartnerId"));
//        map.put("product_type","1");
//        map.put("start_date","2020-02-01");
//        map.put("end_date","2020-11-15");

//        map.put("url","http://182.92.233.160:15099/front/playback");
        map.put("timestamp",new Date().getTime()/1000+"");
        map.put("sign", BajiayunUtil.createSign(map,config));
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/video_account/getAccountInfo",map);
//        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/live_account/getDayMediaLength",map);
//        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/video_account/setTranscodeCallbackUrl",map);
        log.info("接口返回："+roomMsg);*/


        Map<String, String> config = Maps.newHashMap();
        config.put("URL","https://e45546025.at.baijiayun.com/");
        config.put("PartnerId","45546025");
        config.put("SecretKey","3068cecb29959c102ac188172ef3b101");
        config.put("PartnerKey","DK83MBY0zCZV8a1mozgRwEQcqrDLaJsKUc6e41pPM+4s3YIHUUTbgpW9RO2CEh/zUZ2PIBReM5mPQojpKpT4ECw6dKpQ");
        SortedMap map = new TreeMap<>();
        map.put("partner_id",config.get("PartnerId"));
        map.put("product_type","1");
        map.put("page","1");
        map.put("page_size","123456");
//        map.put("product_type","1");
//        map.put("start_date","2020-02-01");
//        map.put("end_date","2020-11-15");
//
//        map.put("mobile","15232866132");
//        map.put("email","501849930@qq.com");
//        map.put("password","xiao19910306");
//        map.put("credit_money","100");
//        map.put("flow","1");
//        map.put("max_flow","1");
//        map.put("effect_time","2021-11-11");
//        map.put("expire_time","2021-11-11");
//        map.put("url","http://182.92.233.160:15099/front/playback");
        map.put("timestamp",new Date().getTime()/1000+"");
        map.put("sign", BajiayunUtil.createSign(map,config));
        String  baijiayunURL = config.get("URL");
        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/playback/getList",map);
//        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/live_account/getDayMediaLength",map);
//        String roomMsg = HttpUtil.post(baijiayunURL + "/openapi/video_account/setTranscodeCallbackUrl",map);
        log.info("接口返回："+roomMsg);

    }


}
