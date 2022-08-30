
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
import org.springcrazy.modules.web.entity.StatUserArea;
import org.springcrazy.modules.web.service.IStatUserAreaService;
import org.springcrazy.modules.web.vo.StatUserAreaVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户ip登录所在区域记录 控制器
 *
 * @author TongZhou
 * @since 2020-05-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/web/statuserarea")
@Api(value = "用户ip登录所在区域记录", tags = "用户ip登录所在区域记录接口")
public class StatUserAreaController extends CrazyController {

	private IStatUserAreaService statUserAreaService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入statUserArea")
	public R<StatUserArea> detail(StatUserArea statUserArea) {
		StatUserArea detail = statUserAreaService.getOne(Condition.getQueryWrapper(statUserArea));
		return R.data(detail);
	}

	/**
	 * 分页 用户ip登录所在区域记录
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入statUserArea")
	public R<IPage<StatUserArea>> list(StatUserArea statUserArea, Query query) {
		IPage<StatUserArea> pages = statUserAreaService.page(Condition.getPage(query), Condition.getQueryWrapper(statUserArea));
		return R.data(pages);
	}

	/**
	 * 自定义分页 用户ip登录所在区域记录
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入statUserArea")
	public R<IPage<StatUserAreaVO>> page(StatUserAreaVO statUserArea, Query query) {
		IPage<StatUserAreaVO> pages = statUserAreaService.selectStatUserAreaPage(Condition.getPage(query), statUserArea);
		return R.data(pages);
	}

	/**
	 * 新增 用户ip登录所在区域记录
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入statUserArea")
	public R save(@Valid @RequestBody StatUserArea statUserArea) {
		return R.status(statUserAreaService.save(statUserArea));
	}

	/**
	 * 修改 用户ip登录所在区域记录
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入statUserArea")
	public R update(@Valid @RequestBody StatUserArea statUserArea) {
		return R.status(statUserAreaService.updateById(statUserArea));
	}

	/**
	 * 新增或修改 用户ip登录所在区域记录
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入statUserArea")
	public R submit(@Valid @RequestBody StatUserArea statUserArea) {
		return R.status(statUserAreaService.saveOrUpdate(statUserArea));
	}


	/**
	 * 删除 用户ip登录所在区域记录
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(statUserAreaService.removeByIds(Func.toIntList(ids)));
	}


}
