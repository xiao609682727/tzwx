
package org.springcrazy.modules.quartz.controller;

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
import org.springcrazy.modules.quartz.entity.QuartzJob;
import org.springcrazy.modules.quartz.service.IQuartzJobService;
import org.springcrazy.modules.quartz.vo.QuartzJobVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 定时任务 控制器
 *
 * @author TongZhou
 * @since 2020-05-11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/quartz/quartzjob")
@Api(value = "定时任务", tags = "定时任务接口")
public class QuartzJobController extends CrazyController {

	private IQuartzJobService quartzJobService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入quartzJob")
	public R<QuartzJob> detail(QuartzJob quartzJob) {
		QuartzJob detail = quartzJobService.getOne(Condition.getQueryWrapper(quartzJob));
		return R.data(detail);
	}

	/**
	 * 分页 定时任务
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入quartzJob")
	public R<IPage<QuartzJob>> list(QuartzJob quartzJob, Query query) {
		IPage<QuartzJob> pages = quartzJobService.page(Condition.getPage(query), Condition.getQueryWrapper(quartzJob).lambda().orderByDesc(QuartzJob::getCreateTime));
		return R.data(pages);
	}

	/**
	 * 自定义分页 定时任务
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入quartzJob")
	public R<IPage<QuartzJobVO>> page(QuartzJobVO quartzJob, Query query) {
		IPage<QuartzJobVO> pages = quartzJobService.selectQuartzJobPage(Condition.getPage(query), quartzJob);
		return R.data(pages);
	}

	/**
	 * 新增 定时任务
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入quartzJob")
	public R save(@Valid @RequestBody QuartzJob quartzJob) {
		quartzJobService.create(quartzJob);
		return R.status(true);
	}

	/**
	 * 修改 定时任务
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入quartzJob")
	public R update(@Valid @RequestBody QuartzJob quartzJob) {
		quartzJobService.update(quartzJob);
		return R.status(true);
	}

	/**
	 * 新增或修改 定时任务
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入quartzJob")
	public R submit(@Valid @RequestBody QuartzJob quartzJob) {
		return R.status(quartzJobService.saveOrUpdate(quartzJob));
	}


	/**
	 * 删除 定时任务
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		quartzJobService.delete(Func.toIntList(ids));
		return R.success("操作成功");
	}

	/**
	 * 执行 定时任务
	 */
	@GetMapping("/exec/{id}")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "执行定时", notes = "传入id")
	public R execution(@ApiParam(value = "主键集合", required = true) @PathVariable Integer id) {
		quartzJobService.execution(quartzJobService.getById(id));
		return R.success("执行成功");
	}

	/**
	 * 执行 定时任务
	 */
	@GetMapping("/updateIsPause/{id}")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "更改定时任务状态", notes = "传入id")
	public R updateIsPause(@ApiParam(value = "主键集合", required = true) @PathVariable Integer id) {
		quartzJobService.updateIsPause(id);
		return R.success("成功");
	}
}
