
package org.springcrazy.modules.coursecard.controller;

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
import org.springcrazy.modules.coursecard.entity.CardCourse;
import org.springcrazy.modules.coursecard.service.ICardCourseService;
import org.springcrazy.modules.coursecard.vo.CardCourseVO;
import org.springcrazy.modules.coursecard.wrapper.CardCourseWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 课程卡课程表 控制器
 *
 * @author TongZhou
 * @since 2021-04-02
 */
@RestController
@AllArgsConstructor
@RequestMapping("/coursecard/cardcourse")
@Api(value = "课程卡课程表", tags = "课程卡课程表接口")
public class CardCourseController extends CrazyController {

	private ICardCourseService cardCourseService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入cardCourse")
	public R<CardCourseVO> detail(CardCourse cardCourse) {
		CardCourse detail = cardCourseService.getOne(Condition.getQueryWrapper(cardCourse));
		return R.data(CardCourseWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 课程卡课程表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入cardCourse")
	public R<IPage<CardCourseVO>> list(CardCourse cardCourse, Query query) {
		IPage<CardCourse> pages = cardCourseService.page(Condition.getPage(query), Condition.getQueryWrapper(cardCourse));
		return R.data(CardCourseWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 课程卡课程表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入cardCourse")
	public R<IPage<CardCourseVO>> page(CardCourseVO cardCourse, Query query) {
		IPage<CardCourseVO> pages = cardCourseService.selectCardCoursePage(Condition.getPage(query), cardCourse);
		return R.data(pages);
	}

	/**
	 * 新增 课程卡课程表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入cardCourse")
	public R save(@Valid @RequestBody CardCourse cardCourse) {
		return R.status(cardCourseService.save(cardCourse));
	}

	/**
	 * 修改 课程卡课程表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入cardCourse")
	public R update(@Valid @RequestBody CardCourse cardCourse) {
		return R.status(cardCourseService.updateById(cardCourse));
	}

	/**
	 * 新增或修改 课程卡课程表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入cardCourse")
	public R submit(@Valid @RequestBody CardCourse cardCourse) {
		return R.status(cardCourseService.saveOrUpdate(cardCourse));
	}


	/**
	 * 删除 课程卡课程表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(cardCourseService.removeByIds(Func.toIntList(ids)));
	}


}
