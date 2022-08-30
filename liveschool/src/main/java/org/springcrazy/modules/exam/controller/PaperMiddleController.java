
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
import org.springcrazy.modules.exam.entity.PaperMiddle;
import org.springcrazy.modules.exam.service.IPaperMiddleService;
import org.springcrazy.modules.exam.vo.PaperMiddleVO;
import org.springcrazy.modules.exam.wrapper.PaperMiddleWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 大题（试卷试题类型中间表） 控制器
 *
 * @author TongZhou
 * @since 2020-12-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exam/papermiddle")
@Api(value = "大题（试卷试题类型中间表）", tags = "大题（试卷试题类型中间表）接口")
public class PaperMiddleController extends CrazyController {

	private IPaperMiddleService paperMiddleService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入paperMiddle")
	public R<PaperMiddleVO> detail(PaperMiddle paperMiddle) {
		PaperMiddle detail = paperMiddleService.getOne(Condition.getQueryWrapper(paperMiddle));
		return R.data(PaperMiddleWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 大题（试卷试题类型中间表）
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入paperMiddle")
	public R<IPage<PaperMiddleVO>> list(PaperMiddle paperMiddle, Query query) {
		IPage<PaperMiddle> pages = paperMiddleService.page(Condition.getPage(query), Condition.getQueryWrapper(paperMiddle));
		return R.data(PaperMiddleWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 大题（试卷试题类型中间表）
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入paperMiddle")
	public R<IPage<PaperMiddleVO>> page(PaperMiddleVO paperMiddle, Query query) {
		IPage<PaperMiddleVO> pages = paperMiddleService.selectPaperMiddlePage(Condition.getPage(query), paperMiddle);
		return R.data(pages);
	}

	/**
	 * 新增 大题（试卷试题类型中间表）
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入paperMiddle")
	public R save(@Valid @RequestBody PaperMiddle paperMiddle) {
		return R.status(paperMiddleService.save(paperMiddle));
	}

	/**
	 * 修改 大题（试卷试题类型中间表）
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入paperMiddle")
	public R update(@Valid @RequestBody PaperMiddle paperMiddle) {
		return R.status(paperMiddleService.updateById(paperMiddle));
	}

	/**
	 * 新增或修改 大题（试卷试题类型中间表）
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入paperMiddle")
	public R submit(@Valid @RequestBody PaperMiddle paperMiddle) {
		return R.status(paperMiddleService.saveOrUpdate(paperMiddle));
	}


	/**
	 * 删除 大题（试卷试题类型中间表）
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(paperMiddleService.removeByIds(Func.toIntList(ids)));
	}


}
