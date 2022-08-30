
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
import org.springcrazy.modules.web.entity.WebsiteRecommendCategory;
import org.springcrazy.modules.web.service.IWebsiteRecommendCategoryService;
import org.springcrazy.modules.web.vo.WebsiteRecommendCategoryVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 网站推荐分类 控制器
 *
 * @author TongZhou
 * @since 2020-05-13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/web/websiterecommendcategory")
@Api(value = "网站推荐分类", tags = "网站推荐分类接口")
public class WebsiteRecommendCategoryController extends CrazyController {

	private IWebsiteRecommendCategoryService websiteRecommendCategoryService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入websiteRecommendCategory")
	public R<WebsiteRecommendCategory> detail(WebsiteRecommendCategory websiteRecommendCategory) {
		WebsiteRecommendCategory detail = websiteRecommendCategoryService.getOne(Condition.getQueryWrapper(websiteRecommendCategory));
		return R.data(detail);
	}

	/**
	 * 分页 网站推荐分类
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入websiteRecommendCategory")
	public R<IPage<WebsiteRecommendCategory>> list(WebsiteRecommendCategory websiteRecommendCategory, Query query) {
		QueryWrapper<WebsiteRecommendCategory> queryWrapper =  Condition.getQueryWrapper(websiteRecommendCategory);
		queryWrapper.lambda().like(Func.isNotBlank(websiteRecommendCategory.getName()), WebsiteRecommendCategory::getName,websiteRecommendCategory.getName());
		websiteRecommendCategory.setName(null);
		IPage<WebsiteRecommendCategory> pages = websiteRecommendCategoryService.page(Condition.getPage(query), queryWrapper);
		return R.data(pages);
	}

	/**
	 * 自定义分页 网站推荐分类
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入websiteRecommendCategory")
	public R<IPage<WebsiteRecommendCategoryVO>> page(WebsiteRecommendCategoryVO websiteRecommendCategory, Query query) {
		IPage<WebsiteRecommendCategoryVO> pages = websiteRecommendCategoryService.selectWebsiteRecommendCategoryPage(Condition.getPage(query), websiteRecommendCategory);
		return R.data(pages);
	}

	/**
	 * 新增 网站推荐分类
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入websiteRecommendCategory")
	public R save(@Valid @RequestBody WebsiteRecommendCategory websiteRecommendCategory) {
		return R.status(websiteRecommendCategoryService.save(websiteRecommendCategory));
	}

	/**
	 * 修改 网站推荐分类
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入websiteRecommendCategory")
	public R update(@Valid @RequestBody WebsiteRecommendCategory websiteRecommendCategory) {
		return R.status(websiteRecommendCategoryService.updateById(websiteRecommendCategory));
	}

	/**
	 * 新增或修改 网站推荐分类
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入websiteRecommendCategory")
	public R submit(@Valid @RequestBody WebsiteRecommendCategory websiteRecommendCategory) {
		return R.status(websiteRecommendCategoryService.saveOrUpdate(websiteRecommendCategory));
	}


	/**
	 * 删除 网站推荐分类
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(websiteRecommendCategoryService.removeByIds(Func.toIntList(ids)));
	}


}
