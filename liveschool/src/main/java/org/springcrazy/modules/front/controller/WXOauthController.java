package org.springcrazy.modules.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springcrazy.common.tool.ProfileConfig;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.secure.AuthInfo;
import org.springcrazy.core.secure.constant.SecureConstant;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.RedisUtil;
import org.springcrazy.modules.auth.service.OnlineUserService;
import org.springcrazy.modules.auth.utils.TokenUtil;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.partyLogin.service.IUserProfileService;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * <p>
 * 第三方登录 Controller
 * </p>
 *
 * @package: com.xkcoding.oauth.controller
 * @description: 第三方登录 Controller
 * @author: yangkai.shen
 * @date: Created in 2019-05-17 10:07
 * @copyright: Copyright (c) 2019
 * @version: V1.0
 * @modified: yangkai.shen
 */
@Slf4j
@Controller
@RequestMapping("/front/wx")
@AllArgsConstructor
@Api(value = "微信公众号授权", tags = "微信公众号授权")
public class WXOauthController {

    private final WxMpService wxService;
    CrazyProperties crazyProperties;
    private IStudentService studentService;
    private OnlineUserService onlineUserService;
    private RedisUtil redisUtil;
    ProfileConfig profileConfig;
    private IUserProfileService userProfileService;

    @GetMapping("/oauth")
    @ApiOperation("微信公众号授权登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scope", value = "授权方式静默授权还是 用户信息授权"),
            @ApiImplicitParam(name = "state", value = "授权传入信息"),
    })
    public String oauth(String scope,String state) {
        Map<String, String> config = profileConfig.getConfig(WebsiteProfile.WECHATMP);
        String appid = config.get("AppID");
        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        String domain = crazyProperties.get("domain");
        String url = domain + "/front/wx/" + appid + "/greet";
        return "redirect:"+wxService.oauth2buildAuthorizationUrl(url,scope,state);
    }

    @ApiOperation("微信公众号授权登录回调")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appid", value = "公众号appid"),
            @ApiImplicitParam(name = "code", value = "回调code"),
            @ApiImplicitParam(name = "state", value = "授权传入信息"),
    })
    @RequestMapping("/{appid}/greet")
    public String greetUser(HttpServletRequest request,@PathVariable String appid, @RequestParam String code,@RequestParam String state)throws IOException,WxErrorException {
        request.setAttribute(SecureConstant.BASIC_HEADER_KEY,"Basic dG9uZ3pob3U6dG9uZ3pob3Vfc2VjcmV0");

        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        String domain = crazyProperties.get("front-domain");
        WxMpOAuth2AccessToken accessToken = wxService.oauth2getAccessToken(code);
        WxMpUser wxMpUser =wxService.oauth2getUserInfo(accessToken,"zh_CN");
        log.info("用户的微信返回信息"+wxMpUser);
        String openId = wxMpUser.getOpenId();
        String unionId = wxMpUser.getUnionId();
        String userName = wxMpUser.getNickname();
        log.info("用户的openId"+openId);
        log.info("用户的unionId"+unionId);
        log.info("用户的昵称"+userName);
        String userNames = URLEncoder.encode(userName,"UTF-8");
        log.info("编码后的用户的昵称"+userNames);
        Student student =userProfileService.getStudetByOpintId(new Student(),"wxH5",openId,unionId);
        //有用户信息，
        if(student.getId()!=1){
            log.info("查询出来的用户"+student.getId());
            UserInfo userInfo = studentService.userInfo(student.getId());
            AuthInfo authInfo = TokenUtil.createAuthInfo(userInfo);
            //记录学生在线用户数据
            onlineUserService.save(authInfo);
            redisUtil.set(openId,authInfo,300);
        }else{
            /*WxMpUser user = wxService.oauth2getUserInfo(accessToken, null);
            if(Func.isEmpty(user)){
                redisUtil.set(openId,accessToken);
            }else{
                redisUtil.set(openId,user);
            }*/
            //当用户没有注册时，进行注册

            String url = domain+"/schoolapp/#/pages/login/bind";
            String redirect = url+"?value="+openId+"&unionId="+unionId+"&userName="+userNames;
            return "redirect:"+redirect;
        }

        String url = domain+"/schoolapp/#/"+state;
        String redirect = url+"?value="+openId+"&unionId="+unionId+"&userName="+userNames;
        log.info(redirect);
        return "redirect:"+redirect;
    }

    @ResponseBody
    @GetMapping("/createJsapiSignature")
    @ApiOperation("获取sdj js签名信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "当前url"),
    })
    public Object createJsapiSignature(String url) throws WxErrorException {
        Map<String, String> config = profileConfig.getConfig(WebsiteProfile.WECHATMP);
        String appid = config.get("AppID");
        final WxJsapiSignature jsapiSignature = this.wxService.switchoverTo(appid).createJsapiSignature(url);
        return R.data(jsapiSignature);
    }


    @GetMapping("/getUserOpenId")
    @ApiOperation("获取用户的OpenId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scope", value = "授权方式静默授权还是 用户信息授权"),
            @ApiImplicitParam(name = "state", value = "授权传入信息"),
            @ApiImplicitParam(name = "urlType", value = "confirmOrder 订单详情页  myBalance 储值页"),
    })
    public String getUseropenId(String scope,String courseId,String orderId,@RequestParam(defaultValue = "confirmOrder") String urlType) {
        Map<String, String> config = profileConfig.getConfig(WebsiteProfile.WECHATMP);
        String appid = config.get("AppID");
       if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        String domain = crazyProperties.get("domain");
        String url = domain + "/front/wx/" + appid + "/setUserOpintId";
        String state = courseId+","+orderId+","+urlType;
        return "redirect:"+wxService.oauth2buildAuthorizationUrl(url,scope,state);
    }

    @ApiOperation("获取用户的OpenId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appid", value = "公众号appid"),
            @ApiImplicitParam(name = "code", value = "回调code"),
            @ApiImplicitParam(name = "state", value = "授权传入信息"),
    })
    @RequestMapping("/{appid}/setUserOpintId")
    public String setUserOpintId(HttpServletRequest request,@PathVariable String appid, @RequestParam String code,@RequestParam String state)throws IOException,WxErrorException {
        request.setAttribute(SecureConstant.BASIC_HEADER_KEY,"Basic dG9uZ3pob3U6dG9uZ3pob3Vfc2VjcmV0");
        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        String domain = crazyProperties.get("front-domain");
        WxMpOAuth2AccessToken accessToken = wxService.oauth2getAccessToken(code);
        String openId = accessToken.getOpenId();
        String[] split = state.split(",");
        String urlType = split[2];
        String redirect = "";
        if(Func.equals(urlType,"confirmOrder")){
            String url = domain+"/schoolapp/#/pages/order/confirmOrder?courseId="+split[0]+"&orderId="+split[1];
            redirect = url+"&openId="+openId;
        }
        if(Func.equals(urlType,"myBalance")){
            String url = domain+"/schoolapp/#/pages/user/myBalance";
            redirect = url+"?openId="+openId;
        }
        log.info(redirect);
        return "redirect:"+redirect;
    }

}
