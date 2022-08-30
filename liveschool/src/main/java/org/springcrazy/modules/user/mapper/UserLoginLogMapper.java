
package org.springcrazy.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.user.entity.UserLoginLog;
import org.springcrazy.modules.user.vo.UserLoginLogVO;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-06
 */
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param userLoginLog
	 * @return
	 */
	List<UserLoginLogVO> selectUserLoginLogPage(IPage page, UserLoginLogVO userLoginLog);

}
