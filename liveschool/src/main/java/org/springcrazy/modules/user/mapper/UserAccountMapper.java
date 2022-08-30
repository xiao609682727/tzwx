
package org.springcrazy.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.user.entity.UserAccount;
import org.springcrazy.modules.user.vo.UserAccountVO;

import java.util.List;

/**
 * 账户信息账户信息，记录用户的账户金额 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-07
 */
public interface UserAccountMapper extends BaseMapper<UserAccount> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param userAccount
	 * @return
	 */
	List<UserAccountVO> selectUserAccountPage(IPage page, @Param("userAccount") UserAccountVO userAccount);

}
