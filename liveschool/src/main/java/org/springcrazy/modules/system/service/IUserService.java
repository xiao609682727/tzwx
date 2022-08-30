
package org.springcrazy.modules.system.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.core.mp.base.BaseService;
import org.springcrazy.modules.system.entity.User;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springcrazy.modules.system.excel.ExportUserExcel;
import org.springcrazy.modules.system.excel.UserExcel;

import java.util.List;

/**
 * 服务类

 */
public interface IUserService extends BaseService<User> {

	/**
	 * 新增或修改用户
	 * @param user
	 * @return
	 */
	boolean submit(User user);

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param user
	 * @return
	 */
	IPage<User> selectUserPage(IPage<User> page, User user);

	/**
	 * 用户信息
	 *
	 * @param userId
	 * @return
	 */
	UserInfo userInfo(Long userId);

	/**
	 * 用户信息
	 *
	 * @param tenantId
	 * @param account
	 * @param password
	 * @return
	 */
	UserInfo userInfo(String tenantId, String account, String password);

	/**
	 * 给用户设置角色
	 *
	 * @param userIds
	 * @param roleIds
	 * @return
	 */
	boolean grant(String userIds, String roleIds);

	/**
	 * 初始化密码
	 *
	 * @param userIds
	 * @return
	 */
	boolean resetPassword(String userIds);

	/**
	 * 修改密码
	 *
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @param newPassword1
	 * @return
	 */
	boolean updatePassword(Integer userId, String oldPassword, String newPassword, String newPassword1);

	/**
	 * 修改密码
	 *
	 * @param userId
	 * @param newPassword
	 * @param newPassword1
	 * @return
	 */
	boolean updatePasswordnew(Integer userId,  String newPassword, String newPassword1);

	/**
	 * 修改密码
	 *
	 * @param userName
	 * @param newPassword
	 * @param newPassword1
	 * @return
	 */
	boolean updateUserNamePasswordnew(String userName,  String newPassword, String newPassword1);

	/**
	 * 获取角色名
	 *
	 * @param roleIds
	 * @return
	 */
	List<String> getRoleName(String roleIds);

	/**
	 * 获取部门名
	 *
	 * @param deptIds
	 * @return
	 */
	List<String> getDeptName(String deptIds);

	/**
	 * 导入用户数据
	 *
	 * @param data
	 * @return
	 */
	void importUser(List<UserExcel> data);

	/**
	 * 获取导出用户数据
	 *
	 * @param queryWrapper
	 * @return
	 */
	List<ExportUserExcel> exportUser(Wrapper<User> queryWrapper);
}
