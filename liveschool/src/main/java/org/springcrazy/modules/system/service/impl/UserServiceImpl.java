
package org.springcrazy.modules.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import lombok.AllArgsConstructor;
import org.springcrazy.common.constant.CommonConstant;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.core.secure.CrazyUser;
import org.springcrazy.core.tool.utils.*;
import org.springcrazy.modules.agent.entity.AgentAccount;
import org.springcrazy.modules.agent.service.IAgentAccountService;
import org.springcrazy.modules.system.entity.User;
import org.springcrazy.modules.system.entity.UserInfo;
import org.springcrazy.modules.system.excel.ExportUserExcel;
import org.springcrazy.modules.system.excel.UserExcel;
import org.springcrazy.modules.system.mapper.UserMapper;
import org.springcrazy.modules.system.service.IDeptService;
import org.springcrazy.modules.system.service.IPostService;
import org.springcrazy.modules.system.service.IRoleService;
import org.springcrazy.modules.system.service.IUserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 服务实现类
 *

 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements IUserService {

	private IDeptService deptService;
	private IPostService postService;
	private IRoleService roleService;
	private IAgentAccountService agentAccountService;

	@Override
	public boolean submit(User user) {
		if (Func.isNotEmpty(user.getPassword())) {
			user.setPassword(DigestUtil.encrypt(user.getPassword()));
		}
		//如果租户id没有默认给用户赋租户id
		if(Func.isBlank(user.getTenantId())){
			user.setTenantId("000000");
		}
		Integer cnt = baseMapper.selectCount(Wrappers.<User>query().lambda().eq(User::getTenantId, user.getTenantId()).eq(User::getAccount, user.getAccount()));
		if (cnt > 0) {
			throw new ApiException("当前用户已存在!");
		}

		//添加账号
		boolean userFlag = saveOrUpdate(user);
		String roleId = user.getRoleId();
		String[] roleIdList = roleId.split(",");
		for(String roleId1:roleIdList){
			//如果角色包含代理商，则添加相关的账户
			if("12".equals(roleId1)){
				AgentAccount agentAccount = new AgentAccount();
				agentAccount.setAgentUserId(user.getId());
				agentAccount.setCreateTime(LocalDateTime.now());
				agentAccount.setUpdateTime(LocalDateTime.now());
				agentAccount.setBalance(new BigDecimal(0));
				agentAccountService.saveOrUpdate(agentAccount);
			}
		}
		return userFlag;
	}

	@Override
	public IPage<User> selectUserPage(IPage<User> page, User user) {
		return page.setRecords(baseMapper.selectUserPage(page, user));
	}

	@Override
	public UserInfo userInfo(Long userId) {
		UserInfo userInfo = new UserInfo();
		User user = baseMapper.selectById(userId);

		//封装 CrazyUser
		CrazyUser CrazyUser = new CrazyUser();
		CrazyUser.setUserId(user.getId());
		CrazyUser.setTenantId(user.getTenantId());
		CrazyUser.setAccount(user.getAccount());
		CrazyUser.setRoleId(user.getRoleId());
		CrazyUser.setUserName(user.getRealName());
		userInfo.setUser(CrazyUser);

		if (Func.isNotEmpty(user)) {
			List<String> roleAlias = baseMapper.getRoleAlias(Func.toStrArray(user.getRoleId()));
			userInfo.setRoles(roleAlias);
		}
		return userInfo;
	}

	@Override
	public UserInfo userInfo(String tenantId, String account, String password) {
		UserInfo userInfo = new UserInfo();
		User user = baseMapper.getUser(tenantId, account, password);

		if(Func.isNull(user)){
			throw new ServiceException("用户名或密码不正确");
		}


		//封装 CrazyUser
		CrazyUser CrazyUser = new CrazyUser();
		CrazyUser.setUserId(user.getId());
		CrazyUser.setTenantId(tenantId);
		CrazyUser.setAccount(account);
		CrazyUser.setRoleId(user.getRoleId());
		CrazyUser.setUserName(user.getRealName());
		userInfo.setUser(CrazyUser);
		if (Func.isNotEmpty(user)) {
			List<String> roleAlias = baseMapper.getRoleAlias(Func.toStrArray(user.getRoleId()));
			userInfo.setRoles(roleAlias);
		}
		return userInfo;
	}

	@Override
	public boolean grant(String userIds, String roleIds) {
		User user = new User();
		user.setRoleId(roleIds);
		return this.update(user, Wrappers.<User>update().lambda().in(User::getId, Func.toIntList(userIds)));
	}

	@Override
	public boolean resetPassword(String userIds) {
		User user = new User();
		user.setPassword(DigestUtil.encrypt(CommonConstant.DEFAULT_PASSWORD));
		user.setUpdateTime(DateUtil.now());
		return this.update(user, Wrappers.<User>update().lambda().in(User::getId, Func.toIntList(userIds)));
	}

	@Override
	public boolean updatePassword(Integer userId, String oldPassword, String newPassword, String newPassword1) {
		User user = getById(userId);
		if (!newPassword.equals(newPassword1)) {
			throw new ServiceException("请输入正确的确认密码!");
		}
		if (!user.getPassword().equals(DigestUtil.encrypt(oldPassword))) {
			throw new ServiceException("原密码不正确!");
		}
		return this.update(Wrappers.<User>update().lambda().set(User::getPassword, DigestUtil.encrypt(newPassword)).eq(User::getId, userId));
	}

	@Override
	public boolean updatePasswordnew(Integer userId, String newPassword, String newPassword1) {
		if (!newPassword.equals(newPassword1)) {
			throw new ServiceException("请输入正确的确认密码!");
		}
		return this.update(Wrappers.<User>update().lambda().set(User::getPassword, DigestUtil.encrypt(newPassword)).eq(User::getId, userId));

	}

	@Override
	public boolean updateUserNamePasswordnew(String userName, String newPassword, String newPassword1) {
		if (!newPassword.equals(newPassword1)) {
			throw new ServiceException("请输入正确的确认密码!");
		}
		return this.update(Wrappers.<User>update().lambda().set(User::getPassword, DigestUtil.encrypt(newPassword)).eq(User::getAccount, userName));
	}

	@Override
	public List<String> getRoleName(String roleIds) {
		return baseMapper.getRoleName(Func.toStrArray(roleIds));
	}

	@Override
	public List<String> getDeptName(String deptIds) {
		return baseMapper.getDeptName(Func.toStrArray(deptIds));
	}

	@Override
	public void importUser(List<UserExcel> data) {
		if (data.size() == 0){
			throw new ServiceException("文件无数据加载！！！");
		}

		int i = 1 ; //行数

		for (UserExcel userExcel : data){
			//集合添值
			User user= new User();

			//获取Excel中的数据
			User userInfo = Objects.requireNonNull(BeanUtil.copy(userExcel, User.class));

			//
			Integer cnt = baseMapper.selectCount(Wrappers.<User>query().lambda().eq(User::getAccount, userInfo.getAccount()));
			if (cnt > 0) {
				throw new ApiException("第"+ i + "行，当前用户账号已存在!");
			}

			//验证
			if (Func.isNotEmpty(userExcel.getAccount()) || Func.isNotEmpty(userExcel.getName()) ||
					Func.isNotEmpty(userExcel.getRealName()) || Func.isNotEmpty(userExcel.getRoleName()) ||
					Func.isNotEmpty(userExcel.getDeptName()) || Func.isNotEmpty(userExcel.getPostName()) ||
					Func.isNotEmpty(userExcel.getPassword()) ||Func.isNotEmpty(userExcel.getPhone()) ||
					Func.isNotEmpty(userExcel.getBirthday()) ||Func.isNotEmpty(userExcel.getEmail())){
				//账号
				if (Func.isNull(userExcel.getAccount())){
					throw new ServiceException("第"+ i + "行，账号为空");
				}else {
					user.setAccount(userExcel.getAccount());
				}
				//昵称
				if (Func.isNull(userExcel.getName())){
					throw new ServiceException("第"+ i + "行，昵称为空");
				}else {
					user.setName(userExcel.getName());
				}
				//姓名
				if (Func.isNull(userExcel.getRealName())){
					throw new ServiceException("第"+ i + "行，姓名为空");
				}else {
					user.setRealName(userExcel.getRealName());
				}
				// 设置角色ID
				if (Func.isNotEmpty(userExcel.getRoleName())){
					String roleId = roleService.getRoleIds("000000", userExcel.getRoleName());
					if (Func.isNotEmpty(roleId)) {
						user.setRoleId(roleId);
					}else {
						throw new ServiceException("第" + i + "行，暂无该角色名称，请根据系统已有角色名称填写");
					}
				}else {
					throw new ServiceException("第"+ i + "行，角色名称为空");
				}
				// 设置部门ID
				if (Func.isNotEmpty(userExcel.getDeptName())) {
					String deptId = deptService.getDeptIds("000000", userExcel.getDeptName());
					if (Func.isNotEmpty(deptId)) {
						user.setDeptId(deptId);
					}else {
						throw new ServiceException("第"+ i + "行，暂无该部门名称，请根据系统已有部门名称填写");
					}
				}else {
					throw new ServiceException("第"+ i + "行，部门名称为空");
				}
				// 设置岗位ID
				if (Func.isNotEmpty(userExcel.getPostName())) {
					String postId = postService.getPostIds("000000", userExcel.getPostName());
					if (Func.isNotEmpty(postId)) {
						user.setPostId(postId);
					}else {
						throw new ServiceException("第"+ i + "行，暂无该岗位名称，请根据系统已有岗位名称填写");
					}
				}else {
					throw new ServiceException("第"+ i + "行，岗位名称为空");
				}


				//手机
				Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
				String pMobile = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\\\d{8}$";
				if (Func.isNotEmpty(userExcel.getPhone()) && !pattern.matcher(userExcel.getPhone()).matches() && userExcel.getPhone().matches(pMobile)){
					throw new ServiceException("第" + i + "行，手机号不正确;");
				}else {
					user.setPhone(userExcel.getPhone());
				}
				//邮箱
				pattern = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
				if (Func.isNotEmpty(userExcel.getEmail()) && !pattern.matcher(userExcel.getEmail()).matches()){
					throw new ServiceException("第" + i + "行，邮箱不正确;");
				}else {
					user.setEmail(userExcel.getEmail());
				}
				//生日
				if (Func.isNotEmpty(userExcel.getBirthday())) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						Long time = simpleDateFormat.parse(simpleDateFormat.format(new Date())).getTime();
						Long birthday = simpleDateFormat.parse(simpleDateFormat.format(userExcel.getBirthday())).getTime();

						if (birthday >= time) {
							throw new ServiceException("第" + i + "行，生日大于当前时间;");
						} else {
							user.setBirthday(userExcel.getBirthday());
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				user.setSex(3);

				//密码
				user.setPassword(Func.isNotEmpty(userExcel.getPassword()) ? userExcel.getPassword() : CommonConstant.DEFAULT_PASSWORD);

				this.submit(user);
			}else {
				return;
			}
		}
	}

	@Override
	public List<ExportUserExcel> exportUser(Wrapper<User> queryWrapper) {
		List<ExportUserExcel> userList = baseMapper.exportUser(queryWrapper);
		userList.forEach(user -> {
			user.setRoleName(StringUtil.join(roleService.getRoleNames(user.getRoleId())));
			user.setDeptName(StringUtil.join(deptService.getDeptNames(user.getDeptId())));
			user.setPostName(StringUtil.join(postService.getPostNames(user.getPostId())));
		});
		return userList;
	}
}
