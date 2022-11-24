
package org.springcrazy.modules.exam.controller;

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
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.exam.entity.Exampaper;
import org.springcrazy.modules.exam.entity.ExampaperRecord;
import org.springcrazy.modules.exam.entity.PaperMiddle;
import org.springcrazy.modules.exam.service.IExampaperRecordService;
import org.springcrazy.modules.exam.service.IExampaperService;
import org.springcrazy.modules.exam.vo.ExampaperVO;
import org.springcrazy.modules.exam.wrapper.ExampaperWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 考试试卷表 控制器
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exam/exampaper")
@Api(value = "考试试卷表", tags = "考试试卷表接口")
public class ExampaperController extends CrazyController {

	private IExampaperService exampaperService;
	private IExampaperRecordService exampaperRecordService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入exampaper")
	public R<ExampaperVO> detail(Exampaper exampaper) {
		Exampaper detail = exampaperService.getOne(Condition.getQueryWrapper(exampaper));
		QueryWrapper<ExampaperRecord> exampaperRecordQueryWrapper = new QueryWrapper<ExampaperRecord>();
		exampaperRecordQueryWrapper.lambda().eq(ExampaperRecord::getEpId,detail.getId());
		exampaperRecordQueryWrapper.select("max(user_score) as max","min(user_score) as min","count(DISTINCT user_id) as num","avg(user_score) as avg");
		Map<String, Object> map = exampaperRecordService.getMap(exampaperRecordQueryWrapper);
		ExampaperVO exampaperVO = ExampaperWrapper.build().entityVO(detail);
		exampaperVO.setMaxScore(Func.toStr(map.get("max"),"0"));
		exampaperVO.setMinScore(Func.toStr(map.get("min"),"0"));
		exampaperVO.setActualJoinNum(Func.toInt(map.get("num"),0));
		exampaperVO.setAvgScore(new BigDecimal(Func.toStr(map.get("avg"),"0")));
		return R.data(exampaperVO);
	}

	/**
	 * 分页 考试试卷表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入exampaper")
	public R<IPage<ExampaperVO>> list(Exampaper exampaper, Query query,String sort) {
		String name = exampaper.getName();
		exampaper.setName(null);
		QueryWrapper<Exampaper> queryWrapper = Condition.getQueryWrapper(exampaper);
/*		if(Func.equals(sort,"desc")){
			queryWrapper.lambda().orderByDesc(Exampaper::getSortNum);
		}else{
			queryWrapper.lambda().orderByAsc(Exampaper::getSortNum);
		}*/
		queryWrapper.lambda().like(Func.isNotBlank(name),Exampaper::getName,name).ne(Exampaper::getStatus,"delete");
		IPage<Exampaper> pages = exampaperService.page(Condition.getPage(query),queryWrapper );
		return R.data(ExampaperWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 考试试卷表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入exampaper")
	public R<IPage<ExampaperVO>> page(ExampaperVO exampaper, Query query) {
		IPage<ExampaperVO> pages = exampaperService.selectExampaperPage(Condition.getPage(query), exampaper);
		return R.data(pages);
	}

	/**
	 * 新增 考试试卷表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入exampaper")
	public R save(@Valid @RequestBody Exampaper exampaper) {
		exampaper.setAddTime(LocalDateTime.now());
		exampaper.setUpdateTime(LocalDateTime.now());
		return R.status(exampaperService.save(exampaper));
	}

	/**
	 * 修改 考试试卷表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入exampaper")
	public R update(@Valid @RequestBody Exampaper exampaper) {
		exampaper.setUpdateTime(LocalDateTime.now());
		return R.status(exampaperService.updateById(exampaper));
	}

	/**
	 * 新增或修改 考试试卷表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入exampaper")
	public R submit(@Valid @RequestBody Exampaper exampaper) {
		return R.status(exampaperService.saveOrUpdate(exampaper));
	}


	/**
	 * 删除 考试试卷表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		List<Exampaper> exampapers = exampaperService.listByIds(Func.toIntList(ids));
		for (Exampaper exampaper : exampapers) {
			exampaper.setStatus("delete");
		}
		return R.status(exampaperService.updateBatchById(exampapers));
	}

	/**
	 * 新增保存试题
	 */
	@PostMapping("/saveQuestion")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "保存试题", notes = "保存试题")
	public R saveQuestion(@Valid @RequestBody List<PaperMiddle> exampaperList,String id) {
		exampaperService.insertQuestion(exampaperList,id);
		return R.status(true);
	}

	/**
	 * 根据试卷id查询试题
	 */
	@GetMapping("/queryQuestion")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "根据试卷id查询试题", notes = "根据试卷id查询试题")
	public R queryQuestion(String id) {
		return R.data(exampaperService.queryQuestion(id));
	}
}
