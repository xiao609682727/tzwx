
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
import org.springcrazy.modules.exam.entity.ErrorCheck;
import org.springcrazy.modules.exam.service.IErrorCheckService;
import org.springcrazy.modules.exam.vo.ErrorCheckVO;
import org.springcrazy.modules.exam.wrapper.ErrorCheckWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 试题纠错表 控制器
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exam/errorcheck")
@Api(value = "试题纠错表", tags = "试题纠错表接口")
public class ErrorCheckController extends CrazyController {

	private IErrorCheckService errorCheckService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入errorCheck")
	public R<ErrorCheckVO> detail(ErrorCheck errorCheck) {
		ErrorCheck detail = errorCheckService.getOne(Condition.getQueryWrapper(errorCheck));
		return R.data(ErrorCheckWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 试题纠错表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入errorCheck")
	public R<IPage<ErrorCheckVO>> list(ErrorCheck errorCheck, Query query) {
		IPage<ErrorCheck> pages = errorCheckService.page(Condition.getPage(query), Condition.getQueryWrapper(errorCheck));
		return R.data(ErrorCheckWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 试题纠错表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入errorCheck")
	public R<IPage<ErrorCheckVO>> page(ErrorCheckVO errorCheck, Query query) {
		IPage<ErrorCheckVO> pages = errorCheckService.selectErrorCheckPage(Condition.getPage(query), errorCheck);
		return R.data(pages);
	}

	/**
	 * 新增 试题纠错表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入errorCheck")
	public R save(@Valid @RequestBody ErrorCheck errorCheck) {
		return R.status(errorCheckService.save(errorCheck));
	}

	/**
	 * 修改 试题纠错表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入errorCheck")
	public R update(@Valid @RequestBody ErrorCheck errorCheck) {
		return R.status(errorCheckService.updateById(errorCheck));
	}

	/**
	 * 新增或修改 试题纠错表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入errorCheck")
	public R submit(@Valid @RequestBody ErrorCheck errorCheck) {
		return R.status(errorCheckService.saveOrUpdate(errorCheck));
	}


	/**
	 * 删除 试题纠错表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(errorCheckService.removeByIds(Func.toIntList(ids)));
	}


}
