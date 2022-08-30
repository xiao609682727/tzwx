
package org.springcrazy.modules.exam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.Charsets;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.exam.entity.ExampaperRecord;
import org.springcrazy.modules.exam.service.IExampaperRecordService;
import org.springcrazy.modules.exam.vo.ExampaperRecordVO;
import org.springcrazy.modules.exam.wrapper.ExampaperRecordWrapper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 考试记录表 控制器
 *
 * @author TongZhou
 * @since 2020-12-02
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exam/exampaperrecord")
@Api(value = "考试记录表", tags = "考试记录表接口")
public class ExampaperRecordController extends CrazyController {

	private IExampaperRecordService exampaperRecordService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入exampaperRecord")
	public R<ExampaperRecordVO> detail(ExampaperRecord exampaperRecord) {
		ExampaperRecord detail = exampaperRecordService.getOne(Condition.getQueryWrapper(exampaperRecord));
		return R.data(ExampaperRecordWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 考试记录表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入exampaperRecord")
	public R<IPage<ExampaperRecordVO>> list(ExampaperRecord exampaperRecord, Query query) {
		IPage<ExampaperRecord> pages = exampaperRecordService.page(Condition.getPage(query), Condition.getQueryWrapper(exampaperRecord).lambda().orderByDesc(ExampaperRecord::getAddTime));
		return R.data(ExampaperRecordWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 考试记录表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入exampaperRecord")
	public R<IPage<ExampaperRecordVO>> page(ExampaperRecordVO exampaperRecord, Query query) {
		IPage<ExampaperRecordVO> pages = exampaperRecordService.selectExampaperRecordPage(Condition.getPage(query), exampaperRecord);
		return R.data(pages);
	}

	/**
	 * 新增 考试记录表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入exampaperRecord")
	public R save(@Valid @RequestBody ExampaperRecord exampaperRecord) {
		return R.status(exampaperRecordService.save(exampaperRecord));
	}

	/**
	 * 修改 考试记录表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入exampaperRecord")
	public R update(@Valid @RequestBody ExampaperRecord exampaperRecord) {
		return R.status(exampaperRecordService.updateById(exampaperRecord));
	}

	/**
	 * 新增或修改 考试记录表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入exampaperRecord")
	public R submit(@Valid @RequestBody ExampaperRecord exampaperRecord) {
		return R.status(exampaperRecordService.saveOrUpdate(exampaperRecord));
	}


	/**
	 * 删除 考试记录表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(exampaperRecordService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 导出考试记录
	 */
	@SneakyThrows
	@GetMapping("export-exampaperRecord")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "导出考试记录", notes = "传入user")
	public void exportExampaperRecord(ExampaperRecordVO exampaperRecord, HttpServletResponse response) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date=simpleDateFormat.format(new Date());
//		exampaperRecord.remove("crazy-auth");

		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("考试记录数据导出"+date, Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
		exampaperRecordService.exportExampaperRecord(response , exampaperRecord);

	}

}
