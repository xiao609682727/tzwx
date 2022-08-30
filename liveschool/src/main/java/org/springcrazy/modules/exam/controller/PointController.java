
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
import org.springcrazy.modules.exam.entity.Point;
import org.springcrazy.modules.exam.service.IPointService;
import org.springcrazy.modules.exam.vo.PointVO;
import org.springcrazy.modules.exam.wrapper.PointWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 考点 控制器
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exam/point")
@Api(value = "考点", tags = "考点接口")
public class PointController extends CrazyController {

	private IPointService pointService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入point")
	public R<PointVO> detail(Point point) {
		Point detail = pointService.getOne(Condition.getQueryWrapper(point));
		return R.data(PointWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 考点
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入point")
	public R<List<PointVO>> list(Point point) {
		List<Point> list = pointService.list(Condition.getQueryWrapper(point).lambda().orderByDesc(Point::getSort));
		return R.data(PointWrapper.build().listNodeVO(list));
	}


	/**
	 * 自定义分页 考点
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入point")
	public R<IPage<PointVO>> page(PointVO point, Query query) {
		IPage<PointVO> pages = pointService.selectPointPage(Condition.getPage(query), point);
		return R.data(pages);
	}

	/**
	 * 新增 考点
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入point")
	public R save(@Valid @RequestBody Point point) {
		return R.status(pointService.save(point));
	}

	/**
	 * 修改 考点
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入point")
	public R update(@Valid @RequestBody Point point) {
		return R.status(pointService.updateById(point));
	}

	/**
	 * 新增或修改 考点
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入point")
	public R submit(@Valid @RequestBody Point point) {
		return R.status(pointService.saveOrUpdate(point));
	}


	/**
	 * 获取菜单树形结构
	 */
	@GetMapping("/tree")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<PointVO>> tree() {
		List<PointVO> tree = pointService.tree();
		return R.data(tree);
	}

	/**
	 * 删除 考点
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(pointService.removeByIds(Func.toIntList(ids)));
	}


}
