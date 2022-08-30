
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
import org.springcrazy.modules.exam.entity.QuestionRecord;
import org.springcrazy.modules.exam.service.IQuestionRecordService;
import org.springcrazy.modules.exam.vo.QuestionRecordVO;
import org.springcrazy.modules.exam.wrapper.QuestionRecordWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 考试试题记录表 控制器
 *
 * @author TongZhou
 * @since 2021-01-05
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exam/questionrecord")
@Api(value = "考试试题记录表", tags = "考试试题记录表接口")
public class QuestionRecordController extends CrazyController {

	private IQuestionRecordService questionRecordService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入questionRecord")
	public R<QuestionRecordVO> detail(QuestionRecord questionRecord) {
		QuestionRecord detail = questionRecordService.getOne(Condition.getQueryWrapper(questionRecord));
		return R.data(QuestionRecordWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 考试试题记录表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入questionRecord")
	public R<IPage<QuestionRecordVO>> list(QuestionRecord questionRecord, Query query) {
		IPage<QuestionRecord> pages = questionRecordService.page(Condition.getPage(query), Condition.getQueryWrapper(questionRecord));
		return R.data(QuestionRecordWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 考试试题记录表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入questionRecord")
	public R<IPage<Map<String,Object>>> page(QuestionRecordVO questionRecord, Query query) {
		IPage<Map<String,Object>> pages = questionRecordService.selectQuestionRecordPage(Condition.getPage(query), questionRecord);
		return R.data(pages);
	}

	/**
	 * 新增 考试试题记录表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入questionRecord")
	public R save(@Valid @RequestBody QuestionRecord questionRecord) {
		return R.status(questionRecordService.save(questionRecord));
	}

	/**
	 * 修改 考试试题记录表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入questionRecord")
	public R update(@Valid @RequestBody QuestionRecord questionRecord) {
		return R.status(questionRecordService.updateById(questionRecord));
	}

	/**
	 * 新增或修改 考试试题记录表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入questionRecord")
	public R submit(@Valid @RequestBody QuestionRecord questionRecord) {
		return R.status(questionRecordService.saveOrUpdate(questionRecord));
	}


	/**
	 * 删除 考试试题记录表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(questionRecordService.removeByIds(Func.toIntList(ids)));
	}


}
