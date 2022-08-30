
package org.springcrazy.modules.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.wf.captcha.SpecCaptcha;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.common.cache.CacheNames;
import org.springcrazy.core.log.annotation.ApiLog;
import org.springcrazy.core.secure.AuthInfo;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.constant.CrazyConstant;
import org.springcrazy.core.tool.support.Kv;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.RedisUtil;
import org.springcrazy.core.tool.utils.WebUtil;
import org.springcrazy.modules.auth.enums.UserEnum;
import org.springcrazy.modules.auth.granter.ITokenGranter;
import org.springcrazy.modules.auth.granter.PasswordTokenGranter;
import org.springcrazy.modules.auth.granter.TokenGranterBuilder;
import org.springcrazy.modules.auth.granter.TokenParameter;
import org.springcrazy.modules.auth.service.OnlineUserService;
import org.springcrazy.modules.auth.utils.TokenUtil;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 认证模块

 */
@RestController
@AllArgsConstructor
@RequestMapping("crazy-auth")
@Api(value = "用户授权认证", tags = "授权接口")
public class AuthController {

	private RedisUtil redisUtil;
	private OnlineUserService onlineUserService;



	@ApiLog("登录用户获取token")
	@PostMapping("token")
	@ApiOperation(value = "获取认证token", notes = "传入租户ID:tenantId,账号:account,密码:password")
	public R<AuthInfo> token(@ApiParam(value = "授权类型", required = true) @RequestParam(defaultValue = "password", required = false) String grantType,
							 @ApiParam(value = "刷新令牌") @RequestParam(required = false) String refreshToken,
							 @ApiParam(value = "令牌") @RequestParam(required = false) String token,
							 @ApiParam(value = "租户ID", required = true) @RequestParam(defaultValue = "000000", required = false) String tenantId,
							 @ApiParam(value = "账号") @RequestParam(required = false) String account,
							 @ApiParam(value = "密码") @RequestParam(required = false) String password, HttpServletRequest request) {

		String userType = Func.toStr(WebUtil.getRequest().getHeader(TokenUtil.USER_TYPE_HEADER_KEY), TokenUtil.DEFAULT_USER_TYPE);

		TokenParameter tokenParameter = new TokenParameter();
		tokenParameter.getArgs().set("tenantId", tenantId)
			.set("account", account)
			.set("password", password)
			.set("grantType", grantType)
			.set("refreshToken", refreshToken)
			.set("token", token)
			.set("userType", userType);

		ITokenGranter granter = TokenGranterBuilder.getGranter(grantType);
		UserInfo userInfo = granter.grant(tokenParameter);

		if (userInfo == null || userInfo.getUser() == null) {
			return R.fail(TokenUtil.USER_NOT_FOUND);
		}
		AuthInfo authInfo = TokenUtil.createAuthInfo(userInfo);
		//当为password 登录时，清除redis token缓存
		if(StrUtil.equals(grantType, PasswordTokenGranter.GRANT_TYPE)){
			redisUtil.del("token" + authInfo.getAccount());
		}
		//只记录学生在线用户数据
		if (userType.equals(UserEnum.STUDENT.getName())) {
			onlineUserService.save(authInfo);
		}
		//记录登录记录
		String dateStr = DateUtil.format(DateUtil.now(), DateUtil.PATTERN_DATE);
		//时间为10天
		redisUtil.sSetAndTime(CacheNames.LOGINLOG+"_"+ dateStr,864000,userInfo.getUser().getUserId());
		return R.data(authInfo);
	}

	@ApiLog("验证token")
	@PostMapping("checkToken")
	@ApiOperation(value = "验证token", notes = "验证token")
	public R checkToken(
			 @ApiParam(value = "令牌") @RequestParam(required = false) String token,
			 HttpServletRequest request) {

		Claims claims = SecureUtil.parseJWT(token);
		boolean flag = false;
		if (claims != null) {
			Object o = redisUtil.get(CrazyConstant.ONLINE_KEY + claims.get(SecureUtil.ACCOUNT));
			if(Func.isNotEmpty(o)){
				Map<String, Object> onlineUser = Func.toMap(o);
				String jwt = Func.toStr(onlineUser.get("key"));
				if(Func.equals(token,jwt)){
					flag = true;
				}
			}
		}

		return R.data(flag);
	}

	@GetMapping("/captcha")
	public R<Kv> captcha() {
		SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
		String verCode = specCaptcha.text().toLowerCase();
		String key = UUID.randomUUID().toString();
		// 存入redis并设置过期时间为30分钟
		redisUtil.set(CacheNames.CAPTCHA_KEY + key, verCode, 30L, TimeUnit.MINUTES);
		// 将key和base64返回给前端
		return R.data(Kv.init().set("key", key).set("image", specCaptcha.toBase64()));
	}

}
