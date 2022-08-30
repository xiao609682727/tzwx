
package org.springcrazy.modules.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.user.entity.UserLoginLog;
import org.springcrazy.modules.user.vo.UserLoginLogVO;

/**
 *  服务类
 *
 * @author TongZhou
 * @since 2020-05-06
 */
public interface IUserLoginLogService extends IService<UserLoginLog> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param userLoginLog
	 * @return
	 */
	IPage<UserLoginLogVO> selectUserLoginLogPage(IPage<UserLoginLogVO> page, UserLoginLogVO userLoginLog);

}
