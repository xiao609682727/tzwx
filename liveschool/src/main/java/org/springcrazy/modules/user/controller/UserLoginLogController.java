
package org.springcrazy.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.user.entity.UserLoginLog;
import org.springcrazy.modules.user.service.IUserLoginLogService;
import org.springcrazy.modules.user.vo.UserLoginLogVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *  控制器
 *
 * @author TongZhou
 * @since 2020-05-06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user/userloginlog")
@Api(value = "用户登录日志", tags = "用户登录日志")
public class UserLoginLogController extends CrazyController {

	private IUserLoginLogService userLoginLogService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入userLoginLog")
	public R<UserLoginLog> detail(UserLoginLog userLoginLog) {
		UserLoginLog detail = userLoginLogService.getOne(Condition.getQueryWrapper(userLoginLog));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入userLoginLog")
	public R<IPage<UserLoginLog>> list(UserLoginLog userLoginLog, Query query) {
		LambdaQueryWrapper<UserLoginLog> userLoginLogLambdaQueryWrapper = new QueryWrapper<UserLoginLog>().lambda().orderByDesc(UserLoginLog::getLoginTime).eq(UserLoginLog::getUserId,userLoginLog.getUserId());
		if(Func.isNotEmpty(userLoginLog.getLoginTime())){
			userLoginLogLambdaQueryWrapper.apply("date_format(login_time,'%Y-%m-%d') = {0}", DateUtil.format(userLoginLog.getLoginTime(),DateUtil.PATTERN_DATE));
		}
		IPage<UserLoginLog> pages = userLoginLogService.page(Condition.getPage(query), userLoginLogLambdaQueryWrapper);
		return R.data(pages);
	}

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入userLoginLog")
	public R<IPage<UserLoginLogVO>> page(UserLoginLogVO userLoginLog, Query query) {
		IPage<UserLoginLogVO> pages = userLoginLogService.selectUserLoginLogPage(Condition.getPage(query), userLoginLog);
		return R.data(pages);
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入userLoginLog")
	public R save(@Valid @RequestBody UserLoginLog userLoginLog) {
		return R.status(userLoginLogService.save(userLoginLog));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入userLoginLog")
	public R update(@Valid @RequestBody UserLoginLog userLoginLog) {
		return R.status(userLoginLogService.updateById(userLoginLog));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入userLoginLog")
	public R submit(@Valid @RequestBody UserLoginLog userLoginLog) {
		return R.status(userLoginLogService.saveOrUpdate(userLoginLog));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(userLoginLogService.removeByIds(Func.toIntList(ids)));
	}


}
