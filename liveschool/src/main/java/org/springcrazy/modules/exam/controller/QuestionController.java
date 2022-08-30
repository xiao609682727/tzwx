
package org.springcrazy.modules.exam.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
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
import org.springcrazy.core.secure.CrazyUser;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.exam.entity.Question;
import org.springcrazy.modules.exam.service.IQuestionService;
import org.springcrazy.modules.exam.vo.QuestionVO;
import org.springcrazy.modules.exam.wrapper.QuestionWrapper;
import org.springcrazy.modules.system.excel.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * 考试试题表 控制器
 *
 * @author TongZhou
 * @since 2020-12-02
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exam/question")
@Api(value = "考试试题表", tags = "考试试题表接口")
public class QuestionController extends CrazyController {

	private IQuestionService questionService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入question")
	public R<QuestionVO> detail(Question question) {
		Question detail = questionService.getOne(Condition.getQueryWrapper(question));
		return R.data(QuestionWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 考试试题表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入question")
	public R<IPage<QuestionVO>> list(Question question, Query query) {
       	String qstContent = question.getQstContent();
       	question.setQstContent(null);
		QueryWrapper<Question> queryWrapper = Condition.getQueryWrapper(question);
		queryWrapper.lambda().like(Func.isNotBlank(qstContent),Question::getQstContent,qstContent);
		queryWrapper.lambda().orderByDesc(Question::getSort);
		queryWrapper.lambda().orderByDesc(Question::getCreateTime);
		IPage<Question> pages = questionService.page(Condition.getPage(query),queryWrapper);
		return R.data(QuestionWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 考试试题表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入question")
	public R<IPage<QuestionVO>> page(QuestionVO question, Query query) {
		IPage<QuestionVO> pages = questionService.selectQuestionPage(Condition.getPage(query), question);
		return R.data(pages);
	}

	/**
	 * 新增 考试试题表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入question")
	public R save(@Valid @RequestBody Question question) {
		question.setCreateTime(LocalDateTime.now());
		question.setUpdateTime(LocalDateTime.now());
		return R.status(questionService.save(question));
	}

	/**
	 * 修改 考试试题表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入question")
	public R update(@Valid @RequestBody Question question) {
		question.setUpdateTime(LocalDateTime.now());
		return R.status(questionService.updateById(question));
	}

	/**
	 * 新增或修改 考试试题表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入question")
	public R submit(@Valid @RequestBody Question question) {
		CrazyUser crazyUser= SecureUtil.getUser();
		question.setAuthor(crazyUser.getUserName());
		question.setUpdateTime(LocalDateTime.now());
		return R.status(questionService.saveOrUpdate(question));
	}


	/**
	 * 删除 考试试题表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(questionService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 导入试题
	 */
	@PostMapping("import-exam")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "导入试题", notes = "传入excel")
	public R importExam(MultipartFile file) {
		String filename = file.getOriginalFilename();
		if (StringUtils.isEmpty(filename)) {
			throw new RuntimeException("请上传文件!");
		}
		if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
			throw new RuntimeException("请上传正确的excel文件!");
		}
		InputStream inputStream;
		try {
			//单选监听事件
			ExamImportListener1 importListener1 = new ExamImportListener1(questionService);
			//多选监听事件
			ExamImportListener2 importListener2 = new ExamImportListener2(questionService);
			//判断监听事件
			ExamImportListener3 importListener3 = new ExamImportListener3(questionService);
			//填空监听事件
			ExamImportListener4 importListener4 = new ExamImportListener4(questionService);
			//接收文件流
			inputStream = new BufferedInputStream(file.getInputStream());
			//初始化文件流
			ExcelReader builder = EasyExcel.read(inputStream).build();
			//单选sheet读取规则
			ReadSheet readSheet1 = EasyExcel.readSheet(0).head(ExamExcel.class).registerReadListener(importListener1).build();
			//多选sheet读取规则
			ReadSheet readSheet2 = EasyExcel.readSheet(1).head(ExamExcel.class).registerReadListener(importListener2).build();
			//判断sheet读取规则
			ReadSheet readSheet3 = EasyExcel.readSheet(2).head(ExamExcel.class).registerReadListener(importListener3).build();
			//填空sheet读取规则
			ReadSheet readSheet4 = EasyExcel.readSheet(3).head(ExamExcel.class).registerReadListener(importListener4).build();
			//按照读取规则挨个读取
			builder.read(readSheet1,readSheet2,readSheet3,readSheet4);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return R.success("操作成功");
	}

	/**
	 * 导出模板
	 *//*
	@SneakyThrows
	@GetMapping("export-template")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "导出模板")
	public void exportUser(HttpServletResponse response) {
		List<StudentExcel> list = new ArrayList<>();
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(Charsets.UTF_8.name());
		String fileName = URLEncoder.encode("用户数据模板", Charsets.UTF_8.name());
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
		EasyExcel.write(response.getOutputStream(), StudentExcel.class).sheet("用户数据表").doWrite(list);
	}*/

}
