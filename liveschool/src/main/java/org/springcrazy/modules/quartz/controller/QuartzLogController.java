
package org.springcrazy.modules.quartz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.quartz.entity.QuartzLog;
import org.springcrazy.modules.quartz.service.IQuartzLogService;
import org.springcrazy.modules.quartz.vo.QuartzLogVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 定时任务日志 控制器
 *
 * @author TongZhou
 * @since 2020-05-11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/quartz/quartzlog")
@Api(value = "定时任务日志", tags = "定时任务日志接口")
public class QuartzLogController extends CrazyController {

	private IQuartzLogService quartzLogService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入quartzLog")
	public R<QuartzLog> detail(QuartzLog quartzLog) {
		QuartzLog detail = quartzLogService.getOne(Condition.getQueryWrapper(quartzLog));
		return R.data(detail);
	}

	/**
	 * 分页 定时任务日志
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入quartzLog")
	public R<IPage<QuartzLog>> list(QuartzLog quartzLog, Query query) {
		LambdaQueryWrapper<QuartzLog> quartzLogLambdaQueryWrapper = Condition.getQueryWrapper(new QuartzLog()).lambda().orderByDesc(QuartzLog::getCreateTime)
				.like(Func.isNotBlank(quartzLog.getJobName()),QuartzLog::getJobName,quartzLog.getJobName())
				.eq(Func.isNotBlank(quartzLog.getIsSuccess()),QuartzLog::getIsSuccess,quartzLog.getIsSuccess());
		IPage<QuartzLog> pages = quartzLogService.page(Condition.getPage(query), quartzLogLambdaQueryWrapper);
		return R.data(pages);
	}

	/**
	 * 自定义分页 定时任务日志
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入quartzLog")
	public R<IPage<QuartzLogVO>> page(QuartzLogVO quartzLog, Query query) {
		IPage<QuartzLogVO> pages = quartzLogService.selectQuartzLogPage(Condition.getPage(query), quartzLog);
		return R.data(pages);
	}

	/**
	 * 新增 定时任务日志
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入quartzLog")
	public R save(@Valid @RequestBody QuartzLog quartzLog) {
		return R.status(quartzLogService.save(quartzLog));
	}

	/**
	 * 修改 定时任务日志
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入quartzLog")
	public R update(@Valid @RequestBody QuartzLog quartzLog) {
		return R.status(quartzLogService.updateById(quartzLog));
	}

	/**
	 * 新增或修改 定时任务日志
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入quartzLog")
	public R submit(@Valid @RequestBody QuartzLog quartzLog) {
		return R.status(quartzLogService.saveOrUpdate(quartzLog));
	}


	/**
	 * 删除 定时任务日志
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(quartzLogService.removeByIds(Func.toIntList(ids)));
	}


}
