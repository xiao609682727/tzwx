
package org.springcrazy.modules.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.common.cache.CacheNames;
import org.springcrazy.common.tool.CommonUtil;
import org.springcrazy.common.tool.CourseUtils;
import org.springcrazy.common.tool.ProfileConfig;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.secure.AuthInfo;
import org.springcrazy.core.secure.CrazyUser;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.api.ResultCode;
import org.springcrazy.core.tool.utils.*;
import org.springcrazy.modules.auth.granter.ITokenGranter;
import org.springcrazy.modules.auth.granter.TokenGranterBuilder;
import org.springcrazy.modules.auth.granter.TokenParameter;
import org.springcrazy.modules.auth.service.OnlineUserService;
import org.springcrazy.modules.auth.utils.TokenUtil;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.edu.dto.OrdersDTO;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.CoursePackage;
import org.springcrazy.modules.edu.entity.Orders;
import org.springcrazy.modules.edu.entity.TrxorderDetail;
import org.springcrazy.modules.edu.service.ICoursePackageService;
import org.springcrazy.modules.edu.service.ICourseService;
import org.springcrazy.modules.edu.service.IOrdersService;
import org.springcrazy.modules.edu.service.ITrxorderDetailService;
import org.springcrazy.modules.lineclass.entity.LineclassEnroll;
import org.springcrazy.modules.lineclass.service.ILineclassEnrollService;
import org.springcrazy.modules.partyLogin.entity.UserProfile;
import org.springcrazy.modules.partyLogin.service.IUserProfileService;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.service.IStudentService;
import org.springcrazy.modules.web.entity.MsgConfig;
import org.springcrazy.modules.web.service.IMsgConfigService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * banner图管理 控制器
 *
 * @author TongZhou
 * @since 2020-03-06
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/front")
@Api(value = "前端用户相关接口", tags = "前端接口")
public class UserFrontController extends CrazyController {

    IStudentService studentService;
    private OnlineUserService onlineUserService;
    private RedisUtil redisUtil;
    IMsgConfigService msgConfigService;
    private ICourseService courseService;
    private ICoursePackageService coursePackageService;

    private IOrdersService ordersService;
    private ITrxorderDetailService trxorderDetailService;
    private ILineclassEnrollService lineclassEnrollService;
    private IUserProfileService userProfileService;

    /**
     * 用户PC前端注册
     */
    @PostMapping("/user/register")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "用户注册", notes = "用户注册 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",value="账号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="mobile",value="手机号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="password",value="用户密码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="smsCode",value="短信验证码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="openId",value="微信openId",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="registerFrom",value="注册来源 register_from\t\t注册来源  1(pc网站) /2(h5注册) /3(app) /4(后台管理单独) 5( 5后台管理批量) 6 (代理商开通)",dataType="string", paramType = "query")})
    public R<AuthInfo> register(String username,String mobile,String password,String smsCode,String openId,@RequestParam(defaultValue = "1") String registerFrom, HttpServletRequest request) {
        //短信验证码判断
        Map<String,String> map = ProfileConfig.getConfig(WebsiteProfile.WEBREGISTER);
        String smsOpenFlag = map.get("smsOpenFlag");
        if(Func.equals(smsOpenFlag,"1")){
            // 获取验证码
            String redisCode = String.valueOf(redisUtil.get(CacheNames.SMSCODE_KEY + mobile));
            // 判断验证码
            if (Func.isBlank(smsCode) || !StringUtil.equalsIgnoreCase(redisCode, smsCode)) {
                throw new ServiceException("请输入正确的验证码");
            }
        }

        //判断账号是否注册
        int count =0;
        // count=studentService.count(new QueryWrapper<Student>().lambda().eq(Student::getUserName, username));
        //if(count > 0) throw new ServiceException("账号已注册");
        //判断手机号是否注册
        count = studentService.count(new QueryWrapper<Student>().lambda().eq(Student::getMobile, mobile));
        if(count > 0) throw new ServiceException("手机号已注册");
        //创建用户
        Student student = new Student();
        student.setUserName(username);
        student.setPassword(password);
        student.setMobile(mobile);
        student.setRegisterFrom("1");
        student.setOpenId(openId);
        studentService.register(student);
        //更新用户默认昵称
        student.setShowName("小学徒"+student.getId());
        studentService.updateById(student);
        //注册成功完成登录流程，生成token
        TokenParameter tokenParameter = new TokenParameter();
        tokenParameter.getArgs()
                .set("account", mobile)
                .set("password", password)
                .set("grantType", "")
                .set("userType", "student");

        ITokenGranter granter = TokenGranterBuilder.getGranter("password");
        UserInfo userInfo = granter.grant(tokenParameter);

        if (userInfo == null || userInfo.getUser() == null) {
            return R.fail(TokenUtil.USER_NOT_FOUND);
        }
        AuthInfo authInfo = TokenUtil.createAuthInfo(userInfo);
        //添加在线用户数据 实现spring 异步事件监听
        onlineUserService.save(authInfo);
        return R.data(authInfo);
    }

    /**
     * 用户H5和APP微信注册
     */
    @PostMapping("/user/wx/register")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "用户注册", notes = "用户注册 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",value="账号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="mobile",value="手机号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="password",value="用户密码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="smsCode",value="短信验证码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="openId",value="微信openId",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="unionId",value="微信unionId",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="wxName",value="微信用户名称userName",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="oauthType",value="登录类型",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="registerFrom",value="来源 1pc 网站  2 后台管理  3 h5注册",dataType="string", paramType = "query")})
    public R<AuthInfo> registerWx(String username,String mobile,String password,String smsCode,String openId,String unionId,String wxName,String oauthType,@RequestParam(defaultValue = "1") String registerFrom, HttpServletRequest request) {
        //短信验证码判断
        Map<String,String> map = ProfileConfig.getConfig(WebsiteProfile.WEBREGISTER);
        String smsOpenFlag = map.get("smsOpenFlag");
        if(Func.equals(smsOpenFlag,"1")){
            // 获取验证码
            String redisCode = String.valueOf(redisUtil.get(CacheNames.SMSCODE_KEY + mobile));
            // 判断验证码
            if (Func.isBlank(smsCode) || !StringUtil.equalsIgnoreCase(redisCode, smsCode)) {
                throw new ServiceException("请输入正确的验证码");
            }
        }

        //判断账号是否注册
        int count =0;
        // count = studentService.count(new QueryWrapper<Student>().lambda().eq(Student::getUserName, username));
        //if(count > 0) throw new ServiceException("账号已注册");
        //判断手机号是否注册
        count = studentService.count(new QueryWrapper<Student>().lambda().eq(Student::getMobile, mobile));
        if(count > 0) throw new ServiceException("手机号已注册");
        //创建用户
        Student student = new Student();
        student.setUserName(username);
        student.setPassword(password);
        student.setMobile(mobile);
        student.setRegisterFrom(registerFrom);
        studentService.register(student);
        //更新用户默认昵称
        student.setShowName("小学徒"+student.getId());
        studentService.updateById(student);

        if("".equals(oauthType)){
            oauthType="wxH5";
        }
        if(ObjectUtil.isNotEmpty(openId)){
            UserProfile userProfile = new UserProfile();
            userProfile.setOpenid(openId);
            userProfile.setUnionid(unionId);
            userProfile.setProfiletype(oauthType);
            userProfile.setUserid(student.getId());
            userProfile.setName(wxName);
            userProfile.setProfiledate(LocalDateTime.now());
            userProfileService.saveOrUpdate(userProfile);
        }
        //注册成功完成登录流程，生成token
        TokenParameter tokenParameter = new TokenParameter();
        tokenParameter.getArgs()
                .set("account", mobile)
                .set("password", password)
                .set("grantType", "")
                .set("userType", "student");

        ITokenGranter granter = TokenGranterBuilder.getGranter("password");
        UserInfo userInfo = granter.grant(tokenParameter);

        if (userInfo == null || userInfo.getUser() == null) {
            return R.fail(TokenUtil.USER_NOT_FOUND);
        }
        AuthInfo authInfo = TokenUtil.createAuthInfo(userInfo);
        //添加在线用户数据 实现spring 异步事件监听
        onlineUserService.save(authInfo);
        return R.data(authInfo);
    }

    /**
     * 检测注册手机号是否注册
     */
    @GetMapping("/user/checkMobile")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "检查手机号是否注册", notes = "检查手机号是否注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name="mobile",value="手机号/账号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="type",value="类型 1账号验证，2 手机号验证  3 邮箱",dataType="string", paramType = "query")})
    public R<String> checkMobile(String mobile,String type) {
        if(Func.equals(type,"1")){
            //判断账号是否注册
            int count = studentService.count(new QueryWrapper<Student>().lambda().eq(Student::getUserName, mobile));
            if(count > 0) throw new ServiceException("账号已注册");
            return R.status(true);
        }else if(Func.equals(type,"3")){
            //判断邮箱是否注册
            int count = studentService.count(new QueryWrapper<Student>().lambda().eq(Student::getEmail, mobile));
            if(count > 0) throw new ServiceException("邮箱已注册");
            return R.status(true);
        } else{
            //判断手机号是否注册
            int count = studentService.count(new QueryWrapper<Student>().lambda().eq(Student::getMobile, mobile));
            if(count > 0) throw new ServiceException("手机号已注册");
            return R.status(true);
        }
    }

    /**
     * 检查验证码
     */
    @GetMapping("/user/checkCaptcha")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "检查验证码", notes = "检查验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="key",value="验证码key",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="code",value="验证码",dataType="string", paramType = "query")})
    public R checkCaptcha(String code,String key) {
        // 获取验证码
        String redisCode = String.valueOf(redisUtil.get(CacheNames.CAPTCHA_KEY + key));
        // 判断验证码
        if (code == null || !StringUtil.equalsIgnoreCase(redisCode, code)) {
            throw new ServiceException(TokenUtil.CAPTCHA_NOT_CORRECT);
        }
        return R.data(true);
    }

    /**
     * 注册验证码发送
     */
    @GetMapping("/user/sendSMS")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "发送验证码", notes = "发送验证码 10分钟有效 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="key",value="验证码key",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="mobile",value="手机号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="code",value="验证码",dataType="string", paramType = "query")})
    public R<String> sendSMS(String mobile, String code, String key, @RequestParam(defaultValue = MsgConfig.CODE_REGISTER) String type) {
        // 获取验证码
        String redisCode = String.valueOf(redisUtil.get(CacheNames.CAPTCHA_KEY + key));
        // 判断验证码
        if (code == null || !StringUtil.equalsIgnoreCase(redisCode, code)) {
            throw new ServiceException(TokenUtil.CAPTCHA_NOT_CORRECT);
        }
        //发送验证码
        String verCode = Func.random(4, RandomType.INT);
        redisUtil.set(CacheNames.SMSCODE_KEY + mobile, verCode, 10L, TimeUnit.MINUTES);
        Student student = new Student();
        student.setMobile(mobile);
        Map<String,String> params = Maps.newLinkedHashMap();
        params.put("code",verCode);
        msgConfigService.sendMsg(student,params, type);
        return R.data(verCode);
    }


    /**
     * 登录验证码发送
     */
    @GetMapping("/user/sendSMSByMobile")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "发送验证码", notes = "发送验证码 10分钟有效 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name="mobile",value="手机号",dataType="string", paramType = "query")})
    public R<String> sendSMS(String mobile, @RequestParam(defaultValue = MsgConfig.CODE_LOGIN) String type) {
        // 获取验证码
        Object mobileKey = redisUtil.get(CacheNames.SMSCODECHECK_KEY + mobile);
        // 判断验证码
        if (Func.notNull(mobileKey)) {
            throw new ServiceException("短信验证码60s内不能重复发送");
        }
        redisUtil.set(CacheNames.SMSCODECHECK_KEY + mobile, mobile, 1L, TimeUnit.MINUTES);
        //发送验证码
        String verCode = Func.random(4, RandomType.INT);
        redisUtil.set(CacheNames.SMSCODE_KEY + mobile, verCode, 10L, TimeUnit.MINUTES);
        Student student = new Student();
        student.setMobile(mobile);
        Map<String,String> params = Maps.newLinkedHashMap();
        params.put("code",verCode);
        msgConfigService.sendMsg(student,params, type);
        return R.data(verCode);
    }

    /**
     * 用户登出
     */
    @GetMapping("/user/logout")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "用户登出", notes = "用户登出")
    public R<String> logout(String mobile) {
        //清除服务端在线用户信息
        CrazyUser user = SecureUtil.getUser();
        if(Func.isNotEmpty(user)){
            onlineUserService.logout(user.getAccount());
        }
        return R.status(true);
    }

    /**
     * 邮箱绑定
     */
    @Deprecated
    @GetMapping("/user/sendEmail")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "发送邮箱", notes = "发送邮箱")
    public R<String> sendEmail(String email) {
        Integer userId = SecureUtil.getUserId();
        //判断如果没有用户信息则进行登录
        if(Func.equals(userId,-1)){
            return R.fail(ResultCode.UN_AUTHORIZED);
        }
        //判断邮箱是否已经注册
        int count = studentService.count(new QueryWrapper<Student>().lambda().eq(Student::getEmail, email));
        if(count >0){
            return R.fail("邮箱已注册");
        }
        //邮件发送
        Student student = studentService.getById(userId);
        student.setEmail(email);
        //生成激活地址
        CrazyProperties crazyProperties = SpringUtil.getBean(CrazyProperties.class);
        String domain = crazyProperties.get("domain");
        //生成激活码  激活码有效时间10分钟
        String activeCode = Func.random(4, RandomType.INT);
        redisUtil.set(CacheNames.ACTIVECODE_KEY + userId, activeCode, 10L, TimeUnit.MINUTES);
        Map<String,String> params = Maps.newLinkedHashMap();
        params.put("showName",student.getShowName());
        params.put("activeCode",activeCode);
        msgConfigService.sendMsg(student,params, MsgConfig.ACTIVE_EMAIL);
        return R.status(true);
    }

    /**
     * 邮箱绑定
     */
    @Deprecated
    @GetMapping("/user/bindEmail")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "绑定邮箱", notes = "绑定邮箱")
    public R<String> bindEmail(String email,String activeCode) {
        Integer userId = SecureUtil.getUserId();
        //判断如果没有用户信息则进行登录
        if(Func.equals(userId,-1)){
            return R.fail(ResultCode.UN_AUTHORIZED);
        }
        //获取激活码
        String ractiveCode = String.valueOf(redisUtil.get(CacheNames.ACTIVECODE_KEY + userId));
        String msg = "激活成功";
        if(Func.isNull(ractiveCode)){
            msg = "激活码已失效";
            return R.fail(msg);
        }
        if(!Func.equals(ractiveCode,activeCode)){
            msg = "激活码错误";
            return R.fail(msg);
        }
        //绑定邮箱
        Student student = studentService.getById(userId);
        student.setEmail(email);
        studentService.updateById(student);
        return R.success(msg);
    }

    /**
     * 修改密码
     */
    @Deprecated
    @GetMapping("/user/updatePassword")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "修改密码", notes = "修改密码")
    public R<String> updatePassword(String oldPassword,String newPassword) {
        Integer userId = SecureUtil.getUserId();
        //判断如果没有用户信息则进行登录
        if(Func.equals(userId,-1)){
            return R.fail(ResultCode.UN_AUTHORIZED);
        }
        Student student = studentService.getById(userId);
        //判断密码是否正确
        if(!Func.equals(student.getPassword(),DigestUtil.encrypt(oldPassword))){
            return R.fail("原密码不正确");
        }
        student.setPassword(DigestUtil.encrypt(newPassword));
        studentService.updateById(student);
        return R.status(true);
    }

    /**
     * 修改手机号
     */
    @Deprecated
    @GetMapping("/user/updateMobile")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "修改手机号", notes = "修改手机号")
    public R<String> updateMobile(String code,String mobile) {
        Integer userId = SecureUtil.getUserId();
        //判断如果没有用户信息则进行登录
        if(Func.equals(userId,-1)){
            return R.fail(ResultCode.UN_AUTHORIZED);
        }
        Student student = studentService.getById(userId);
        //判断验证码是否正确
        String redisCode = String.valueOf(redisUtil.get(CacheNames.SMSCODE_KEY + mobile));
        // 判断验证码
        if (Func.isBlank(code) || !StringUtil.equalsIgnoreCase(redisCode, code)) {
            throw new ServiceException("请输入正确的验证码");
        }
        student.setMobile(mobile);
        studentService.updateById(student);
        return R.status(true);
    }


    /**
     * 发送邮箱重置密码
     */
    @GetMapping("/user/sendEmailResetPwd")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "发送邮箱", notes = "发送邮箱重置密码")
    public R<String> sendEmailResetPwd(String email,String code,String key) {
        //判断邮箱是否已经注册
        int count = studentService.count(new QueryWrapper<Student>().lambda().eq(Student::getEmail, email));
        if(count == 0){
            return R.fail("该邮箱无效");
        }
        // 获取验证码
        String redisCode = String.valueOf(redisUtil.get(CacheNames.CAPTCHA_KEY + key));
        if(code == null || !StringUtil.equalsIgnoreCase(redisCode, code)){
            return R.fail("验证码不正确");
        }
        //邮件发送
        Student student = studentService.getOne(new QueryWrapper<Student>().lambda().eq(Student::getEmail,email));
        if(Func.isNull(student)){
            return R.fail("该邮箱无效");
        }
        student.setEmail(email);
        //生成新密码
        String pwd = Func.random(8, RandomType.INT);
        //修改密码
        student.setPassword(DigestUtil.encrypt(pwd));
        studentService.updateById(student);
        //发送邮件
        Map<String,String> params = Maps.newLinkedHashMap();
        params.put("newpassword",pwd);
        msgConfigService.sendMsg(student,params, MsgConfig.RETRIEVE_PASSWORD);
        return R.status(true);
    }


    /**
     * 通过手机号修改密码
     */
    @GetMapping("/user/updatePwdByMobile")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "修改手机号", notes = "修改手机号")
    public R<String> updatePwdByMobile(String code,String mobile,String newPwd) {
        Student student = studentService.getOne(new QueryWrapper<Student>().lambda().eq(Student::getMobile,mobile));
        if(Func.isNull(student)){
            return R.fail("暂无手机号");
        }
        //判断验证码是否正确
        String redisCode = String.valueOf(redisUtil.get(CacheNames.SMSCODE_KEY + mobile));
        // 判断验证码
        if (Func.isBlank(code) || !StringUtil.equalsIgnoreCase(redisCode, code)) {
            throw new ServiceException("请输入正确的验证码");
        }
        student.setPassword(DigestUtil.encrypt(newPwd));
        studentService.updateById(student);
        return R.status(true);
    }

    /**
     * 创建充值订单
     */
    @Deprecated
    @GetMapping("/user/createOrder")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "创建充值订单", notes = "创建充值订单")
    public R<OrdersDTO> createOrder(String orderType,String money,String payType,@RequestParam(defaultValue = "web") String reqChannel) {
        Integer userId = SecureUtil.getUserId();
        //判断如果没有用户信息则进行登录
        if(Func.equals(userId,-1)){
            return R.fail(ResultCode.UN_AUTHORIZED);
        }
        BigDecimal bmoney = new BigDecimal(money);
        OrdersDTO order = new OrdersDTO();
        order.setUserId(userId);
        order.setOrderNo(CommonUtil.getOrderNum());
        order.setStates(Orders.STATES_INIT);
        order.setCreateTime(DateUtil.now());
        order.setPayType(payType);
        order.setReqChannel(reqChannel);
        order.setOrderAmount(bmoney);
        order.setCashAmount(BigDecimal.ZERO);
        order.setVmAmount(BigDecimal.ZERO);
        order.setBackAmount(BigDecimal.ZERO);
        order.setCouponAmount(BigDecimal.ZERO);
        order.setOutTradeNo(CommonUtil.getOrderNum());
        order.setOrderType(orderType);

        List<TrxorderDetail> list = Lists.newArrayList();
        //初始化订单详情
        TrxorderDetail detail = TrxorderDetail.builder().userId(userId).courseId(0).orderNo(order.getOrderNo()).losetype("0")
                .loseTime("").beginTime(DateUtil.now()).authTime(DateUtil.now()).createTime(DateUtil.now())
                .sourcePrice(bmoney).currentPrice(bmoney).courseName("账户充值").authStatus(TrxorderDetail.STATUS_INIT)
                .description("").lastUpdateTime(DateUtil.now()).delStatus("1").trxorderType(order.getOrderType()).build();
        list.add(detail);
        order.setList(list);
        ordersService.createOrder(order);

        return R.data(order);
    }

    /**
     * 创建课程订单
     */
    @Deprecated
    @GetMapping("/user/createCourseOrder")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "创建课程订单", notes = "创建课程订单")
    public R<OrdersDTO> createCourseOrder(Integer courseId ,String payType,@RequestParam(defaultValue = "web") String reqChannel) {
        Integer userId = SecureUtil.getUserId();
        //判断如果没有用户信息则进行登录
        if(Func.equals(userId,-1)){
            return R.fail(ResultCode.UN_AUTHORIZED);
        }



        Course course = courseService.getById(courseId);
        String orderType = "";
        if(Func.equals(course.getSellType(),"1")){
            orderType = "COURSE";
        }
        if(Func.equals(course.getSellType(),"2")){
            orderType = "LIVE";
        }
        if(Func.equals(course.getSellType(),"3")){
            orderType = "PACKAGE";
        }
        if(Func.equals(course.getSellType(),"4")){
            orderType = "LINECLASS";
        }
        //如果课程类型为到期时间
        if(Func.equals(course.getLosetype(),"0")){
            Date courseEndTime = course.getEndTime();
            if(Func.isNotEmpty(courseEndTime)){
                Date nowDate = new Date();
                //如果到期时间小于当前时间则不能下订单
                if(courseEndTime.compareTo(nowDate)<0){
                    return R.fail("课程已过期，请学习其他课程");
                }
            }
        }

        BigDecimal bmoney = course.getCurrentPrice();
        OrdersDTO order = new OrdersDTO();
        order.setUserId(userId);
        order.setOrderNo(CommonUtil.getOrderNum());
        order.setStates(Orders.STATES_INIT);
        order.setCreateTime(DateUtil.now());
        order.setPayType(payType);
        order.setReqChannel(reqChannel);
        order.setOrderAmount(bmoney);
        order.setCashAmount(BigDecimal.ZERO);
        order.setVmAmount(BigDecimal.ZERO);
        order.setBackAmount(BigDecimal.ZERO);
        order.setCouponAmount(BigDecimal.ZERO);
        order.setOutTradeNo(CommonUtil.getOrderNum());
        order.setOrderType(orderType);

        List<TrxorderDetail> list = Lists.newArrayList();
        //初始化订单详情
        TrxorderDetail detail = TrxorderDetail.builder().userId(userId).courseId(courseId).orderNo(order.getOrderNo()).losetype(course.getLosetype())
                .loseTime(course.getLoseTime()).loseAbsTime(course.getEndTime()).beginTime(DateUtil.now()).authTime(DateUtil.now()).createTime(DateUtil.now())
                .sourcePrice(bmoney).currentPrice(bmoney).courseName(course.getCourseName()).authStatus(TrxorderDetail.STATUS_INIT)
                .description(course.getTitle()).lastUpdateTime(DateUtil.now()).delStatus("1").trxorderType(order.getOrderType()).build();
        list.add(detail);
        order.setList(list);
        ordersService.createOrder(order);

        return R.data(order);
    }

    /**
     * 创建免费课程订单
     */
    @Deprecated
    @GetMapping("/user/createFreeCourseOrder")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "创建免费课程订单", notes = "创建免费课程订单")
    public R<OrdersDTO> createFreeCourseOrder(Integer courseId ,String payType,@RequestParam(defaultValue = "web") String reqChannel) {
        Integer userId = SecureUtil.getUserId();
        //判断如果没有用户信息则进行登录
        if(Func.equals(userId,-1)){
            return R.fail(ResultCode.UN_AUTHORIZED);
        }
        //判断该用户是否含有免费课程  如果没有则创建
        TrxorderDetail trxorderDetail = new TrxorderDetail();
        trxorderDetail.setUserId(userId);
        trxorderDetail.setCourseId(courseId);
        QueryWrapper<TrxorderDetail> trxorderDetailQueryWrapper = new QueryWrapper<>(trxorderDetail);
        trxorderDetailQueryWrapper.lambda().ge(TrxorderDetail::getAuthTime, DateUtil.now()).eq(TrxorderDetail::getAuthStatus,TrxorderDetail.STATUS_SUCCESS);
        int count = trxorderDetailService.count(trxorderDetailQueryWrapper);
        if(count == 0){
            Course course = courseService.getById(courseId);
            String orderType = "";
            if(Func.equals(course.getSellType(),"1")){
                orderType = "COURSE";
            }
            if(Func.equals(course.getSellType(),"2")){
                orderType = "LIVE";
            }
            if(Func.equals(course.getSellType(),"3")){
                orderType = "PACKAGE";
            }
            if(Func.equals(course.getSellType(),"4")){
                orderType = "LINECLASS";
            }


            BigDecimal bmoney = course.getCurrentPrice();
            OrdersDTO order = new OrdersDTO();
            order.setUserId(userId);
            order.setOrderNo(CommonUtil.getOrderNum());
            order.setStates(Orders.STATES_SUCCESS);
            order.setCreateTime(DateUtil.now());
            order.setPayType(payType);
            order.setPayTime(DateUtil.now());
            order.setReqChannel(reqChannel);
            order.setOrderAmount(bmoney);
            order.setCashAmount(BigDecimal.ZERO);
            order.setVmAmount(BigDecimal.ZERO);
            order.setBackAmount(BigDecimal.ZERO);
            order.setCouponAmount(BigDecimal.ZERO);
            order.setOutTradeNo(CommonUtil.getOrderNum());
            order.setOrderType(orderType);

            List<TrxorderDetail> list = Lists.newArrayList();
            //初始化订单详情
            TrxorderDetail detail = TrxorderDetail.builder().userId(userId).courseId(courseId).orderNo(order.getOrderNo()).losetype(course.getLosetype())
                    .loseTime(course.getLoseTime()).loseAbsTime(course.getEndTime()).beginTime(DateUtil.now()).authTime(CourseUtils.getAuthTime(course)).createTime(DateUtil.now())
                    .sourcePrice(bmoney).currentPrice(bmoney).courseName(course.getCourseName()).authStatus(TrxorderDetail.STATUS_SUCCESS)
                    .description(course.getTitle()).lastUpdateTime(DateUtil.now()).delStatus("1").trxorderType(order.getOrderType()).payTime(DateUtil.now()).build();
            list.add(detail);
            order.setList(list);
            ordersService.createOrder(order);
            //更新销售量 销售量+1
            //查询学习人数
            QueryWrapper<TrxorderDetail> queryWrapper = new QueryWrapper<TrxorderDetail>();
            queryWrapper.lambda().eq(TrxorderDetail::getCourseId,courseId).eq(TrxorderDetail::getAuthStatus,TrxorderDetail.STATUS_SUCCESS);
            int buyCount = trxorderDetailService.count(queryWrapper);
            course.setPageBuycount(buyCount);
            courseService.updateById(course);
            if("LINECLASS".equals(orderType)){
                LineclassEnroll lineclassEnroll = new LineclassEnroll();
                lineclassEnroll.setUserId(detail.getUserId());
                lineclassEnroll.setCourseId(detail.getCourseId());
                lineclassEnroll.setTrxorderId(detail.getTrxorderId());
                lineclassEnrollService.updateLineClassEnrol(lineclassEnroll);
            }
        }


        return R.status(true);
    }

    /**
     * 账户储值支付
     */
    @GetMapping("/user/accountPay")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "账户储值支付", notes = "账户储值支付")
    public R accountPay(Integer orderId) {
        return R.data(ordersService.accountPay(orderId));
    }

    /**
     * 检查订单是否成功
     */
    @GetMapping("/user/checkOrder")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "创建充值订单", notes = "创建充值订单")
    public R<OrdersDTO> checkOrder(Integer orderId) {
        Orders orders = ordersService.getById(orderId);
        if(Func.equals(Orders.STATES_INIT,orders.getStates())){
            return R.status(false);
        }
        return R.status(true);
    }

    /**
     * 检查是否拥有课程
     */
    @Deprecated
    @GetMapping("/user/checkHaveCourse")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "检查是否拥有课程", notes = "检查是否拥有课程")
    public R checkHaveCourse(Integer courseId) {
        //判断直播与点播课程
        Integer userId = SecureUtil.getUserId();
        //判断如果没有用户信息则进行登录
        if(Func.equals(userId,-1)){
            return R.fail(ResultCode.UN_AUTHORIZED);
        }

        TrxorderDetail td = new TrxorderDetail();
        td.setUserId(userId);
        td.setCourseId(courseId);
        td.setAuthStatus(TrxorderDetail.STATUS_SUCCESS);
        QueryWrapper<TrxorderDetail> trxorderDetailQueryWrapper = new QueryWrapper<>(td);
        trxorderDetailQueryWrapper.lambda().ge(TrxorderDetail::getAuthTime,new Date());
        int count = trxorderDetailService.count(trxorderDetailQueryWrapper);
        if(count == 0){
            //判断套餐中是否含有次课程
            TrxorderDetail detail = new TrxorderDetail();
            detail.setUserId(userId);
            detail.setAuthStatus(TrxorderDetail.STATUS_SUCCESS);
            detail.setTrxorderType("PACKAGE");
            QueryWrapper<TrxorderDetail> wrapper = new QueryWrapper<TrxorderDetail>(detail);
            wrapper.lambda().ge(TrxorderDetail::getAuthTime,new Date());
            List<TrxorderDetail> list = trxorderDetailService.list(wrapper);
            List<Integer> idArr = list.stream().map(d->d.getCourseId()).collect(Collectors.toList());
            QueryWrapper<CoursePackage> coursePackageQueryWrapper = new QueryWrapper<CoursePackage>();
            coursePackageQueryWrapper.lambda().eq(CoursePackage::getCourseId,courseId).in(CoursePackage::getParentCourseId,idArr);
            int pcount = coursePackageService.count(coursePackageQueryWrapper);
            if(pcount == 0){
                return R.data(false);
            }
        }
        return R.data(true);
    }
}
