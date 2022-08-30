
package org.springcrazy.modules.auth.granter;

import lombok.AllArgsConstructor;
import org.springcrazy.common.cache.CacheNames;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.RedisUtil;
import org.springcrazy.core.tool.utils.StringUtil;
import org.springcrazy.core.tool.utils.WebUtil;
import org.springcrazy.modules.auth.enums.UserEnum;
import org.springcrazy.modules.auth.utils.TokenUtil;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 通过手机验证码登录

 */
@Component
@AllArgsConstructor
public class SMSTokenGranter implements ITokenGranter {

	public static final String GRANT_TYPE = "sms";

	private IStudentService studentService;
	private RedisUtil redisUtil;


	@Override
	public UserInfo grant(TokenParameter tokenParameter) {
		String account = tokenParameter.getArgs().getStr("account");

		HttpServletRequest request = WebUtil.getRequest();
		String smsCode = request.getHeader(TokenUtil.CAPTCHA_HEADER_CODE);
		String redisCode = String.valueOf(redisUtil.get(CacheNames.SMSCODE_KEY + account));
		// 判断验证码
		if (Func.isBlank(smsCode) || !StringUtil.equalsIgnoreCase(redisCode, smsCode)) {
			throw new ServiceException("请输入正确的验证码");
		}


		UserInfo userInfo = null;
		if (Func.isNoneBlank(account)) {
			// 获取用户类型
			String userType = tokenParameter.getArgs().getStr("userType");
			// 学生用户
			if (userType.equals(UserEnum.STUDENT.getName())) {
				userInfo = studentService.userInfo(account);
			}
		}
		return userInfo;
	}

}
