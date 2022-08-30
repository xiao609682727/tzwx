
package org.springcrazy.modules.system.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.system.entity.User;
import org.springcrazy.modules.system.service.IDictService;
import org.springcrazy.modules.system.service.IUserService;
import org.springcrazy.modules.system.vo.UserVO;

import java.util.List;

/**
 * 包装类,返回视图层所需的字段
 *

 */
public class UserWrapper extends BaseEntityWrapper<User, UserVO> {

	private static IUserService userService;

	private static IDictService dictService;

	static {
		userService = SpringUtil.getBean(IUserService.class);
		dictService = SpringUtil.getBean(IDictService.class);
	}

	public static UserWrapper build() {
		return new UserWrapper();
	}

	@Override
	public UserVO entityVO(User user) {
		UserVO userVO = BeanUtil.copy(user, UserVO.class);
		if(Func.isNotEmpty(user.getRoleId())){
			List<String> roleName = userService.getRoleName(user.getRoleId());
			userVO.setRoleName(Func.join(roleName));
		}
		if(Func.isNotEmpty(user.getDeptId())){
			List<String> deptName = userService.getDeptName(user.getDeptId());
			userVO.setDeptName(Func.join(deptName));
		}
		userVO.setSexName(dictService.getValue("sex", Func.toInt(user.getSex())));
		return userVO;
	}

}
