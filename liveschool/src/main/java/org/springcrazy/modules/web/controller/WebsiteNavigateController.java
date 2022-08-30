
package org.springcrazy.modules.web.controller;

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
import org.springcrazy.modules.web.entity.WebsiteNavigate;
import org.springcrazy.modules.web.service.IWebsiteNavigateService;
import org.springcrazy.modules.web.vo.WebsiteNavigateVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 导航表 控制器
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/web/websitenavigate")
@Api(value = "导航表", tags = "导航表接口")
public class WebsiteNavigateController extends CrazyController {

	private IWebsiteNavigateService websiteNavigateService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入websiteNavigate")
	public R<WebsiteNavigate> detail(WebsiteNavigate websiteNavigate) {
		WebsiteNavigate detail = websiteNavigateService.getOne(Condition.getQueryWrapper(websiteNavigate));
		return R.data(detail);
	}

	/**
	 * 分页 导航表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入websiteNavigate")
	public R<IPage<WebsiteNavigate>> list(WebsiteNavigate websiteNavigate, Query query) {
		QueryWrapper<WebsiteNavigate> queryWrapper =  Condition.getQueryWrapper(websiteNavigate);
		queryWrapper.lambda().orderByDesc(WebsiteNavigate::getSort);
		queryWrapper.lambda().like(Func.isNotBlank(websiteNavigate.getName()), WebsiteNavigate::getName,websiteNavigate.getName());
		websiteNavigate.setName(null);
		IPage<WebsiteNavigate> pages = websiteNavigateService.page(Condition.getPage(query), queryWrapper);
		return R.data(pages);
	}

	/**
	 * 自定义分页 导航表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入websiteNavigate")
	public R<IPage<WebsiteNavigateVO>> page(WebsiteNavigateVO websiteNavigate, Query query) {
		IPage<WebsiteNavigateVO> pages = websiteNavigateService.selectWebsiteNavigatePage(Condition.getPage(query), websiteNavigate);
		return R.data(pages);
	}

	/**
	 * 新增 导航表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入websiteNavigate")
	public R save(@Valid @RequestBody WebsiteNavigate websiteNavigate) {
		return R.status(websiteNavigateService.save(websiteNavigate));
	}

	/**
	 * 修改 导航表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入websiteNavigate")
	public R update(@Valid @RequestBody WebsiteNavigate websiteNavigate) {
		return R.status(websiteNavigateService.updateById(websiteNavigate));
	}

	/**
	 * 新增或修改 导航表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入websiteNavigate")
	public R submit(@Valid @RequestBody WebsiteNavigate websiteNavigate) {
		return R.status(websiteNavigateService.saveOrUpdate(websiteNavigate));
	}


	/**
	 * 删除 导航表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(websiteNavigateService.removeByIds(Func.toIntList(ids)));
	}


}
