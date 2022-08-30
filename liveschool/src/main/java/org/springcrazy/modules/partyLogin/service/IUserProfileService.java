
package org.springcrazy.modules.partyLogin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.partyLogin.entity.UserProfile;
import org.springcrazy.modules.partyLogin.vo.UserProfileVO;
import org.springcrazy.modules.user.entity.Student;

import java.util.List;

/**
 * 用户第三方绑定 服务类
 *
 * @author TongZhou
 * @since 2021-03-15
 */
public interface IUserProfileService extends IService<UserProfile> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param userProfile
	 * @return
	 */
	IPage<UserProfileVO> selectUserProfilePage(IPage<UserProfileVO> page, UserProfileVO userProfile);


	/**
	 * 查询第三方用户数据是否存在
	 * */

	Student getStudetByOpintId(Student student, String type ,String openid,	String unionid);

	/**
	 * 查询第三方用户数据是否存在
	 * */

	List<UserProfile> getStudetThreeLise(Integer userId);

	/**
	 * 删除用户第三方登录信息
	 * */
	void delectUserProfile(List<Integer> userId);

	/**
	 * 查询有关微信绑定
	 * */
	/**
	 * 查询第三方用户数据是否存在
	 * */

	List<UserProfile> getStudetWxLise(Integer userId);

	/**
	 * 查询有关qq绑定
	 * */
	/**
	 * 查询第三方用户数据是否存在
	 * */

	List<UserProfile> getStudetQqLise(Integer userId);

	/**
	 * 查询有关微博绑定
	 * */
	/**
	 * 查询第三方用户数据是否存在
	 * */

	List<UserProfile> getStudetWeiboLise(Integer userId);
}
