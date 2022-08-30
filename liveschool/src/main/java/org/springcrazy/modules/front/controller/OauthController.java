package org.springcrazy.modules.front.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xkcoding.justauth.AuthRequestFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.secure.AuthInfo;
import org.springcrazy.core.secure.constant.SecureConstant;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.RedisUtil;
import org.springcrazy.modules.auth.service.OnlineUserService;
import org.springcrazy.modules.auth.utils.TokenUtil;
import org.springcrazy.modules.partyLogin.entity.UserProfile;
import org.springcrazy.modules.partyLogin.service.IUserProfileService;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
@RequestMapping("/front/oauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "第三方登录接口", tags = "第三方登录接口")
public class OauthController {
    private final AuthRequestFactory factory;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    CrazyProperties crazyProperties;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private IUserProfileService userProfileService;

    /**
     * 登录
     *
     * @param oauthType 第三方登录类型
     * @param response  response
     * @throws IOException
     */
    @ApiOperation("第三方登录类型")
    @GetMapping("/login/{oauthType}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oauthType", value = "登录类型"),
            @ApiImplicitParam(name = "type", value = "bind,login"),
            @ApiImplicitParam(name = "userId", value = "当type 为bind时传入"),
            @ApiImplicitParam(name = "clientType", value = "pc,h5 手机授权回调使用h5 pc授权成功回调使用pc"),
    })
    public void renderAuth(@PathVariable String oauthType, HttpServletResponse response,String type,String userId,@RequestParam(defaultValue = "pc") String clientType) throws IOException {
        AuthRequest authRequest = factory.get(oauthType);
        String authorize = authRequest.authorize(oauthType + "," + AuthStateUtils.createState() + "," + type + "," + userId + "," + clientType);
        response.sendRedirect(authorize);
    }


    /**
     * 登录成功后的回调
     *
     * @param oauthType 第三方登录类型
     * @param callback  携带返回的信息
     * @return 登录成功后的信息
     */
    @ApiOperation("登录成功后的回调")
    @GetMapping("/{oauthType}/callback")
    public String login(@PathVariable String oauthType, HttpServletResponse response, AuthCallback callback, HttpServletRequest request, RedirectAttributes attr) throws IOException {
        //设置授权和业务无关
        request.setAttribute(SecureConstant.BASIC_HEADER_KEY,"Basic dG9uZ3pob3U6dG9uZ3pob3Vfc2VjcmV0");

        AuthRequest authRequest = factory.get(oauthType);
        AuthResponse authResponse = authRequest.login(callback);
        if(authResponse.getCode() != 2000){
            throw new ServiceException("获取用户信息失败");
        }
        //获取state 参数并处理
        String state = callback.getState();
        List<String> arr = Func.toStrList(",",state);
        String type = arr.get(2);
        String clientType = arr.get(4);
        String domain = crazyProperties.get("front-domain");
        String url = domain+"/oauth/back";
        if(Func.equals(clientType,"h5")){
            url = domain+"/schoolapp/#/pages/oauth/back";
        }
        //绑定用户数据
        AuthUser authUser = (AuthUser) authResponse.getData();
        String msg = "登录成功";
        String unionid="";
        if(Func.isEmpty(unionid)){
            unionid=  getUserUnionid(authUser,oauthType);
        }
        String profileType;
        if(Func.equals("qq",oauthType)){
            profileType="qqPC";
        }else if(Func.equals("wechat_open",oauthType)){
            profileType="wxPC";
        }else {
            profileType="weibo";
        }
        log.info("查看登录类型："+profileType);
        String value=unionid;
        if(Func.equals("bind",type)){
            Integer userId = Func.toInt(arr.get(3));
            boolean flag = true;
            if(Func.isNotEmpty(authUser)){
                Student student =userProfileService.getStudetByOpintId(new Student(),profileType,authUser.getUuid(),unionid);
                if(student.getId()!=1){
                    log.info("查询到的用户："+student.getId());
                    flag = false;
                    msg = "该帐号已被其他用户绑定";
                }
                if(flag){
                    log.info("开始创建用户profile"+userId);
                    UserProfile userProfile = new UserProfile();
                    userProfile.setOpenid(value);
                    userProfile.setUnionid(unionid);
                    userProfile.setProfiletype(profileType);
                    userProfile.setUserid(userId);
                    userProfile.setName(authUser.getNickname());
                    userProfile.setProfiledate(LocalDateTime.now());
                    userProfileService.saveOrUpdate(userProfile);
                }
            }
        }
        String userFlag = "2";
        //用户快捷登录
        if(Func.equals("login",type)){
            QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
            Student student= new Student();
            student =userProfileService.getStudetByOpintId(student,profileType,authUser.getUuid(),unionid);
            log.info("登录用户类型："+profileType+"用户的opintId"+authUser.getUuid()+"用的唯一id"+unionid);
            //无用户信息，
            if(student.getId()!=1){
                UserInfo userInfo = studentService.userInfo(student.getId());
                AuthInfo authInfo = TokenUtil.createAuthInfo(userInfo);
                //记录学生在线用户数据
                onlineUserService.save(authInfo);
                redisUtil.set(value,authInfo,300);
                userFlag = "1";
            }else{
                //token 没有无参的构造方法，在放入redis中报错
                if(Func.isNotEmpty(authUser.getToken())){
                    authUser.setToken(null);
                }
                redisUtil.set(value,authUser,300);
            }
        }

        log.info("【response】= {}", JSONUtil.toJsonStr(authResponse));
        /**
         *
         * type   bind    login
         * 当type 为bind  userFlag，value无参
         * 当type 为 login  userFlag 1由用户已绑定，2每用户去绑定 value  当userFlag 为1 则是rediskey 为2则是 uuid
         */
        String redirect = url+"?type="+type+"&userFlag="+userFlag+"&value="+value+"&msg="+URLEncoder.encode(msg,"UTF-8");
        log.info(redirect);
        return "redirect:"+redirect;

    }

    @ApiOperation("授权失败回调")
    @ResponseBody
    @GetMapping("/{oauthType}/revoke/{token}")
    public Object revokeAuth(@PathVariable("token") String token,@PathVariable String oauthType) throws IOException {
        AuthRequest authRequest = factory.get(oauthType);
        return authRequest.revoke(AuthToken.builder().accessToken(token).build());
    }

    @ApiOperation("根据授权返回的rediskey获取用户id")
    @ResponseBody
    @GetMapping("/getAuthInfo")
    public Object getAuthInfo(HttpServletResponse response,String id) throws IOException {
        return R.data(redisUtil.get(id));
    }

    public String getUserUnionid(AuthUser authUser,String oauthType){
        if(Func.equals("qq",oauthType)){
            AuthToken token= authUser.getToken();
            String result = HttpUtil.get("https://graph.qq.com/oauth2.0/me?access_token="+token.getAccessToken()+"&unionid=1&fmt=json");
            Map<String,Object> map1= JSONUtil.toBean(result,Map.class);
            return map1.get("unionid").toString();
        }else if(Func.equals("wechat_open",oauthType)){
            AuthToken token= authUser.getToken();
            return token.getUnionId();
        }else {
            return authUser.getUuid();
        }
    }

    /**
     * 登录
     *
     * @param oauthType 第三方登录类型
     * @param response  response
     * @throws IOException
     */
    @ApiOperation("第三方登录类型")
    @GetMapping("/qqAndWx")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oauthType", value = "登录类型"),
            @ApiImplicitParam(name = "state", value = "返回的地址"),
            @ApiImplicitParam(name = "openId", value = "openId"),
            @ApiImplicitParam(name = "userName", value = "用户昵称"),
            @ApiImplicitParam(name = "unionId", value = "unionId"),
    })
    public R qqAndWx(HttpServletResponse response, String oauthType, String state, String openId, String userName, String unionId) throws IOException {
        UserProfile userProfile = new UserProfile();
        userProfile.setProfiletype(oauthType);
        userProfile.setOpenid(openId);
        userProfile.setName(userName);
        userProfile.setUnionid(unionId);
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        Student student= new Student();
        student =userProfileService.getStudetByOpintId(student,oauthType,openId,unionId);
        log.info("登录用户类型："+oauthType+"用户的opintId"+openId+"用的唯一id"+unionId);
        String msg = "登录成功";
        String userFlag = "2";
        Map<String,Object> returnMap = new HashMap<>();

        //有用户信息，
        if(student.getId()!=1){
            log.info("查询出来的用户"+student.getId());
            UserInfo userInfo = studentService.userInfo(student.getId());
            AuthInfo authInfo = TokenUtil.createAuthInfo(userInfo);
            //记录学生在线用户数据
            onlineUserService.save(authInfo);
            returnMap.put("user",authInfo);
            returnMap.put("isLogin",true);

        }else{
            returnMap.put("isLogin",false);

        }
        return R.data(returnMap);
    }


}
