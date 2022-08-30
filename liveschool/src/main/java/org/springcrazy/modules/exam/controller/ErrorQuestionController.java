
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
import org.springcrazy.modules.exam.entity.ErrorQuestion;
import org.springcrazy.modules.exam.service.IErrorQuestionService;
import org.springcrazy.modules.exam.vo.ErrorQuestionVO;
import org.springcrazy.modules.exam.wrapper.ErrorQuestionWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 错题记录表 控制器
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exam/errorquestion")
@Api(value = "错题记录表", tags = "错题记录表接口")
public class ErrorQuestionController extends CrazyController {

	private IErrorQuestionService errorQuestionService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入errorQuestion")
	public R<ErrorQuestionVO> detail(ErrorQuestion errorQuestion) {
		ErrorQuestion detail = errorQuestionService.getOne(Condition.getQueryWrapper(errorQuestion));
		return R.data(ErrorQuestionWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 错题记录表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入errorQuestion")
	public R<IPage<ErrorQuestionVO>> list(ErrorQuestion errorQuestion, Query query) {
		IPage<ErrorQuestion> pages = errorQuestionService.page(Condition.getPage(query), Condition.getQueryWrapper(errorQuestion).lambda().orderByDesc(ErrorQuestion::getAddtime));
		return R.data(ErrorQuestionWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 错题记录表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入errorQuestion")
	public R<IPage<ErrorQuestionVO>> page(ErrorQuestionVO errorQuestion, Query query) {
		IPage<ErrorQuestionVO> pages = errorQuestionService.selectErrorQuestionPage(Condition.getPage(query), errorQuestion);
		return R.data(pages);
	}

	/**
	 * 新增 错题记录表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入errorQuestion")
	public R save(@Valid @RequestBody ErrorQuestion errorQuestion) {
		return R.status(errorQuestionService.save(errorQuestion));
	}

	/**
	 * 修改 错题记录表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入errorQuestion")
	public R update(@Valid @RequestBody ErrorQuestion errorQuestion) {
		return R.status(errorQuestionService.updateById(errorQuestion));
	}

	/**
	 * 新增或修改 错题记录表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入errorQuestion")
	public R submit(@Valid @RequestBody ErrorQuestion errorQuestion) {
		return R.status(errorQuestionService.saveOrUpdate(errorQuestion));
	}


	/**
	 * 删除 错题记录表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(errorQuestionService.removeByIds(Func.toIntList(ids)));
	}


}
