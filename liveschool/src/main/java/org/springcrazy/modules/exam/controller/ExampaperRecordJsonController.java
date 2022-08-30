
package org.springcrazy.modules.exam.controller;

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
import org.springcrazy.modules.exam.entity.ExampaperRecordJson;
import org.springcrazy.modules.exam.service.IExampaperRecordJsonService;
import org.springcrazy.modules.exam.vo.ExampaperRecordJsonVO;
import org.springcrazy.modules.exam.wrapper.ExampaperRecordJsonWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *  控制器
 *
 * @author TongZhou
 * @since 2021-01-05
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exam/exampaperrecordjson")
@Api(value = "", tags = "接口")
public class ExampaperRecordJsonController extends CrazyController {

	private IExampaperRecordJsonService exampaperRecordJsonService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入exampaperRecordJson")
	public R<ExampaperRecordJsonVO> detail(ExampaperRecordJson exampaperRecordJson) {
		ExampaperRecordJson detail = exampaperRecordJsonService.getOne(Condition.getQueryWrapper(exampaperRecordJson));
		return R.data(ExampaperRecordJsonWrapper.build().entityVO(detail));
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入exampaperRecordJson")
	public R<IPage<ExampaperRecordJsonVO>> list(ExampaperRecordJson exampaperRecordJson, Query query) {
		IPage<ExampaperRecordJson> pages = exampaperRecordJsonService.page(Condition.getPage(query), Condition.getQueryWrapper(exampaperRecordJson));
		return R.data(ExampaperRecordJsonWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入exampaperRecordJson")
	public R<IPage<ExampaperRecordJsonVO>> page(ExampaperRecordJsonVO exampaperRecordJson, Query query) {
		IPage<ExampaperRecordJsonVO> pages = exampaperRecordJsonService.selectExampaperRecordJsonPage(Condition.getPage(query), exampaperRecordJson);
		return R.data(pages);
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入exampaperRecordJson")
	public R save(@Valid @RequestBody ExampaperRecordJson exampaperRecordJson) {
		return R.status(exampaperRecordJsonService.save(exampaperRecordJson));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入exampaperRecordJson")
	public R update(@Valid @RequestBody ExampaperRecordJson exampaperRecordJson) {
		return R.status(exampaperRecordJsonService.updateById(exampaperRecordJson));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入exampaperRecordJson")
	public R submit(@Valid @RequestBody ExampaperRecordJson exampaperRecordJson) {
		return R.status(exampaperRecordJsonService.saveOrUpdate(exampaperRecordJson));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(exampaperRecordJsonService.removeByIds(Func.toIntList(ids)));
	}


}
