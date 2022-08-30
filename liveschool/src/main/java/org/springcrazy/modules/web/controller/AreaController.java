
package org.springcrazy.modules.web.controller;

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
import org.springcrazy.modules.web.entity.Area;
import org.springcrazy.modules.web.service.IAreaService;
import org.springcrazy.modules.web.vo.AreaVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *  控制器
 *
 * @author TongZhou
 * @since 2020-05-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/web/area")
@Api(value = "城市区域信息", tags = "城市区域信息")
public class AreaController extends CrazyController {

	private IAreaService areaService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入area")
	public R<Area> detail(Area area) {
		Area detail = areaService.getOne(Condition.getQueryWrapper(area));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入area")
	public R<List<Area>> list(Area area) {
		List<Area> pages = areaService.list(Condition.getQueryWrapper(area));
		return R.data(pages);
	}

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入area")
	public R<IPage<AreaVO>> page(AreaVO area, Query query) {
		IPage<AreaVO> pages = areaService.selectAreaPage(Condition.getPage(query), area);
		return R.data(pages);
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入area")
	public R save(@Valid @RequestBody Area area) {
		return R.status(areaService.save(area));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入area")
	public R update(@Valid @RequestBody Area area) {
		return R.status(areaService.updateById(area));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入area")
	public R submit(@Valid @RequestBody Area area) {
		return R.status(areaService.saveOrUpdate(area));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(areaService.removeByIds(Func.toIntList(ids)));
	}


}
