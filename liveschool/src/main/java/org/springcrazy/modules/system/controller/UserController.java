
package org.springcrazy.modules.system.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.Charsets;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.secure.CrazyUser;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.constant.CrazyConstant;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.agent.entity.AgentAccount;
import org.springcrazy.modules.agent.service.IAgentAccountService;
import org.springcrazy.modules.system.entity.User;
import org.springcrazy.modules.system.excel.ExportUserExcel;
import org.springcrazy.modules.system.excel.UserExcel;
import org.springcrazy.modules.system.excel.UserImportListener;
import org.springcrazy.modules.system.service.IUserService;
import org.springcrazy.modules.system.vo.UserVO;
import org.springcrazy.modules.system.wrapper.UserWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 *

 */
@ApiIgnore
@RestController
@RequestMapping("crazy-user")
@AllArgsConstructor
public class UserController {

	private IUserService userService;

	/**
	 * 查询单条
	 */
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "查看详情", notes = "传入id")
	@GetMapping("/detail")
	public R<UserVO> detail(User user) {
		User detail = userService.getOne(Condition.getQueryWrapper(user));
		return R.data(UserWrapper.build().entityVO(detail));
	}

	/**
	 * 查询单条
	 */
	@ApiOperationSupport(order =2)
	@ApiOperation(value = "查看详情", notes = "传入id")
	@GetMapping("/info")
	public R<UserVO> info(CrazyUser user) {
		User detail = userService.getById(user.getUserId());
		return R.data(UserWrapper.build().entityVO(detail));
	}

	/**
	 * 用户列表
	 */
	@GetMapping("/list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "account", value = "账号名", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "realName", value = "姓名", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "列表", notes = "传入account和realName")
	public R<IPage<UserVO>> list(@ApiIgnore @RequestParam Map<String, Object> user, Query query, CrazyUser crazyUser) {
		QueryWrapper<User> queryWrapper = Condition.getQueryWrapper(user, User.class);
		IPage<User> pages = userService.page(Condition.getPage(query), (!crazyUser.getTenantId().equals(CrazyConstant.ADMIN_TENANT_ID)) ? queryWrapper.lambda().eq(User::getTenantId, crazyUser.getTenantId()) : queryWrapper);
		return R.data(UserWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增或修改", notes = "传入User")
	public R submit(@Valid @RequestBody User user) {
		if (Func.isNull(user.getSex())){
			user.setSex(3);
		}
		return R.status(userService.submit(user));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入User")
	public R update(@Valid @RequestBody User user) {
		return R.status(userService.updateById(user));
	}

	private IAgentAccountService agentAccountService;
	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "删除", notes = "传入userid")
	public R remove(@RequestParam String ids) {
		if(Func.isEmpty(ids)){
			return R.fail("id不能为空");
		}
		//删除如果代理商存在账户，则删除
		String[] idList = ids.split(",");
		for(String id:idList){
			QueryWrapper<AgentAccount> queryWrapper = Condition.getQueryWrapper(new AgentAccount());
			queryWrapper.lambda().eq(Func.isNotBlank(id), AgentAccount::getAgentUserId,id);
			agentAccountService.remove(queryWrapper);
		}
		return R.status(userService.deleteLogic(Func.toIntList(ids)));
	}


	/**
	 * 设置菜单权限
	 *
	 * @param userIds
	 * @param roleIds
	 * @return
	 */
	@PostMapping("/grant")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "权限设置", notes = "传入roleId集合以及menuId集合")
	public R grant(@ApiParam(value = "userId集合", required = true) @RequestParam String userIds,
				   @ApiParam(value = "roleId集合", required = true) @RequestParam String roleIds) {
		boolean temp = userService.grant(userIds, roleIds);
		return R.status(temp);
	}

	@PostMapping("/reset-password")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "初始化密码", notes = "传入userId集合")
	public R resetPassword(@ApiParam(value = "userId集合", required = true) @RequestParam String userIds) {
		boolean temp = userService.resetPassword(userIds);
		return R.status(temp);
	}

	/**
	 * 修改密码
	 *
	 * @param oldPassword
	 * @param newPassword
	 * @param newPassword1
	 * @return
	 */
	@PostMapping("/update-password")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "修改密码", notes = "传入密码")
	public R updatePassword(CrazyUser user, @ApiParam(value = "旧密码", required = true) @RequestParam String oldPassword,
							@ApiParam(value = "新密码", required = true) @RequestParam String newPassword,
							@ApiParam(value = "新密码", required = true) @RequestParam String newPassword1) {
		boolean temp = userService.updatePassword(user.getUserId(), oldPassword, newPassword, newPassword1);
		return R.status(temp);
	}

	/**
	 * 用户列表
	 *
	 * @param user
	 * @return
	 */
	@GetMapping("/user-list")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "用户列表", notes = "传入user")
	public R<List<User>> userList(User user) {
		List<User> list = userService.list(Condition.getQueryWrapper(user));
		return R.data(list);
	}

	/**
	 * 用户列表
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/getAdminUserList")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "用户列表", notes = "传入user")
	public R<List<User>> getAdminUserList() {
		User user = new User();
		user.setRoleId("12");
		List<User> list = userService.list(Condition.getQueryWrapper(user));
		return R.data(list);
	}



	/**
	 * 导入用户
	 */
	@PostMapping("import-user")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "导入用户", notes = "传入excel")
	public R importUser(MultipartFile file, Integer isCovered) {
		String filename = file.getOriginalFilename();
		if (StringUtils.isEmpty(filename)) {
			throw new RuntimeException("请上传文件!");
		}
		if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
			throw new RuntimeException("请上传正确的excel文件!");
		}
		InputStream inputStream;
		try {
			UserImportListener importListener = new UserImportListener(userService);
			inputStream = new BufferedInputStream(file.getInputStream());
			ExcelReaderBuilder builder = EasyExcel.read(inputStream, UserExcel.class, importListener);
			builder.doReadAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return R.success("操作成功");
	}

	/**
	 * 导出用户
	 */
	@SneakyThrows
	@GetMapping("export-user")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "导出用户", notes = "传入user")
	public void exportUser(@ApiIgnore @RequestParam Map<String, Object> user, CrazyUser crazyUser, HttpServletResponse response) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date=simpleDateFormat.format(new Date());

		user.remove("crazy-auth");
		QueryWrapper<User> queryWrapper = Condition.getQueryWrapper(user, User.class);
		if (!SecureUtil.isAdministrator()){
			queryWrapper.lambda().eq(User::getTenantId, crazyUser.getTenantId());
		}
		queryWrapper.lambda().like(Func.isNotEmpty(user.get("account")),User::getAccount, user.get("account"));
		queryWrapper.lambda().like(Func.isNotEmpty(user.get("realName")),User::getRealName, user.get("realName"));
		queryWrapper.lambda().eq(User::getIsDeleted, CrazyConstant.DB_NOT_DELETED);
		List<ExportUserExcel> list = userService.exportUser(queryWrapper);
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("用户数据导出", Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + date + ".xlsx");
		EasyExcel.write(response.getOutputStream(), ExportUserExcel.class).sheet("用户数据表").doWrite(list);
	}

	/**
	 * 导出模板
	 */
	@SneakyThrows
	@GetMapping("export-template")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "导出模板")
	public void exportUser(HttpServletResponse response) {
		List<UserExcel> list = new ArrayList<>();
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("用户数据模板", Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
		EasyExcel.write(response.getOutputStream(), UserExcel.class).sheet("用户数据表").doWrite(list);
	}


	/**
	 * 修改密码
	 *
	 * @param newPassword
	 * @param newPassword1
	 * @return
	 */
	@PostMapping("/updateAndUserpassword")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "修改密码", notes = "传入密码")
	public R updateAndUserPassword(
							@ApiParam(value = "用户id", required = true) @RequestParam int userId,
							@ApiParam(value = "新密码", required = true) @RequestParam String newPassword,
							@ApiParam(value = "新密码", required = true) @RequestParam String newPassword1) {
		boolean temp = userService.updatePasswordnew(userId, newPassword, newPassword1);
		return R.status(temp);
	}


	/**
	 * 修改密码
	 *
	 * @param newPassword
	 * @param newPassword1
	 * @return
	 */
	@PostMapping("/updateAndUserNamepassword")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "修改密码", notes = "传入密码")
	public R updateAndUserPaupdateAndUserNamepasswordssword(
			@ApiParam(value = "用姓名", required = true) @RequestParam String userName,
			@ApiParam(value = "新密码", required = true) @RequestParam String newPassword,
			@ApiParam(value = "新密码", required = true) @RequestParam String newPassword1) {
		boolean temp = userService.updateUserNamePasswordnew(userName, newPassword, newPassword1);
		return R.status(temp);
	}
}
