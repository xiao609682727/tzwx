
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
import org.springcrazy.modules.exam.entity.Qstmiddle;
import org.springcrazy.modules.exam.service.IQstmiddleService;
import org.springcrazy.modules.exam.vo.QstmiddleVO;
import org.springcrazy.modules.exam.wrapper.QstmiddleWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 考试试卷表 控制器
 *
 * @author TongZhou
 * @since 2020-12-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exam/qstmiddle")
@Api(value = "考试试卷表", tags = "考试试卷表接口")
public class QstmiddleController extends CrazyController {

	private IQstmiddleService qstmiddleService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入qstmiddle")
	public R<QstmiddleVO> detail(Qstmiddle qstmiddle) {
		Qstmiddle detail = qstmiddleService.getOne(Condition.getQueryWrapper(qstmiddle));
		return R.data(QstmiddleWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 考试试卷表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入qstmiddle")
	public R<IPage<QstmiddleVO>> list(Qstmiddle qstmiddle, Query query) {
		IPage<Qstmiddle> pages = qstmiddleService.page(Condition.getPage(query), Condition.getQueryWrapper(qstmiddle));
		return R.data(QstmiddleWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 考试试卷表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入qstmiddle")
	public R<IPage<QstmiddleVO>> page(QstmiddleVO qstmiddle, Query query) {
		IPage<QstmiddleVO> pages = qstmiddleService.selectQstmiddlePage(Condition.getPage(query), qstmiddle);
		return R.data(pages);
	}

	/**
	 * 新增 考试试卷表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入qstmiddle")
	public R save(@Valid @RequestBody Qstmiddle qstmiddle) {
		return R.status(qstmiddleService.save(qstmiddle));
	}

	/**
	 * 修改 考试试卷表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入qstmiddle")
	public R update(@Valid @RequestBody Qstmiddle qstmiddle) {
		return R.status(qstmiddleService.updateById(qstmiddle));
	}

	/**
	 * 新增或修改 考试试卷表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入qstmiddle")
	public R submit(@Valid @RequestBody Qstmiddle qstmiddle) {
		return R.status(qstmiddleService.saveOrUpdate(qstmiddle));
	}


	/**
	 * 删除 考试试卷表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(qstmiddleService.removeByIds(Func.toIntList(ids)));
	}


}
