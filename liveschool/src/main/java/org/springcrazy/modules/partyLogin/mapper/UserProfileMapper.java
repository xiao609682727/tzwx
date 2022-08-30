
package org.springcrazy.modules.partyLogin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.partyLogin.entity.UserProfile;
import org.springcrazy.modules.partyLogin.vo.UserProfileVO;

import java.util.List;

/**
 * 用户第三方绑定 Mapper 接口
 *
 * @author TongZhou
 * @since 2021-03-15
 */
public interface UserProfileMapper extends BaseMapper<UserProfile> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param userProfile
	 * @return
	 */
	List<UserProfileVO> selectUserProfilePage(IPage page, UserProfileVO userProfile);

	/**
	 * 查询第三方用户数据是否存在
	 * */

	UserProfile getStudetByOpintId(@Param("e") UserProfile userProfile);

	/**
	 * 查询第三方用户数据是否存在
	 * */

	List<UserProfile> getStudetThreeLise(@Param("e") UserProfile userProfile);

	/**
	 * 删除用户第三方登录信息
	 * */
	void delectUserProfile(@Param("e") UserProfile userProfile);


	/**
	 * 查询有关微信绑定
	 * */
	/**
	 * 查询第三方用户数据是否存在
	 * */

	List<UserProfile> getStudetWxLise(@Param("e") UserProfile userProfile);

	/**
	 * 查询有关QQ绑定
	 * */
	/**
	 * 查询第三方用户数据是否存在
	 * */

	List<UserProfile> getStudetQqLise(@Param("e") UserProfile userProfile);


	/**
	 * 查询有关微博绑定
	 * */
	/**
	 * 查询第三方用户数据是否存在
	 * */

	List<UserProfile> getStudetWeiboLise(@Param("e") UserProfile userProfile);

}
