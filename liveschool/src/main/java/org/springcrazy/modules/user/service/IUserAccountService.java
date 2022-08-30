
package org.springcrazy.modules.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.user.entity.UserAccount;
import org.springcrazy.modules.user.vo.UserAccountVO;

import java.math.BigDecimal;

/**
 * 账户信息账户信息，记录用户的账户金额 服务类
 *
 * @author TongZhou
 * @since 2020-05-07
 */
public interface IUserAccountService extends IService<UserAccount> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param userAccount
	 * @return
	 */
	IPage<UserAccountVO> selectUserAccountPage(IPage<UserAccountVO> page, UserAccountVO userAccount);

	void money(Integer userId, BigDecimal money,String type,String bizType,String desc,String orderNO);

}
