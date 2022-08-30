
package org.springcrazy.modules.auth.granter;

import lombok.AllArgsConstructor;
import org.springcrazy.common.cache.CacheNames;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.tool.utils.*;
import org.springcrazy.modules.auth.enums.UserEnum;
import org.springcrazy.modules.auth.utils.TokenUtil;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springcrazy.modules.system.service.IUserService;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码TokenGranter

 */
@Component
@AllArgsConstructor
public class CaptchaTokenGranter implements ITokenGranter {

	public static final String GRANT_TYPE = "captcha";

	private IUserService userService;
	private RedisUtil redisUtil;
	private IStudentService studentService;

	@Override
	public UserInfo grant(TokenParameter tokenParameter) {
		HttpServletRequest request = WebUtil.getRequest();

		String key = request.getHeader(TokenUtil.CAPTCHA_HEADER_KEY);
		String code = request.getHeader(TokenUtil.CAPTCHA_HEADER_CODE);
		// 获取验证码
		String redisCode = String.valueOf(redisUtil.get(CacheNames.CAPTCHA_KEY + key));
		// 判断验证码
		if (code == null || !StringUtil.equalsIgnoreCase(redisCode, code)) {
			throw new ServiceException(TokenUtil.CAPTCHA_NOT_CORRECT);
		}

		String tenantId = tokenParameter.getArgs().getStr("tenantId");
		String account = tokenParameter.getArgs().getStr("account");
		String password = tokenParameter.getArgs().getStr("password");
		UserInfo userInfo = null;
		if (Func.isNoneBlank(account, password)) {
			// 获取用户类型
			String userType = tokenParameter.getArgs().getStr("userType");
			// 根据不同用户类型调用对应的接口返回数据，用户可自行拓展
			if (userType.equals(UserEnum.WEB.getName())) {
				userInfo = userService.userInfo(tenantId, account, DigestUtil.encrypt(password));
			} else if (userType.equals(UserEnum.APP.getName())) {
				userInfo = userService.userInfo(tenantId, account, DigestUtil.encrypt(password));
			}else if (userType.equals(UserEnum.STUDENT.getName())) {
				userInfo = studentService.userInfo(account, DigestUtil.encrypt(password));
			} else {
				userInfo = userService.userInfo(tenantId, account, DigestUtil.encrypt(password));
			}
		}
		return userInfo;
	}

}
