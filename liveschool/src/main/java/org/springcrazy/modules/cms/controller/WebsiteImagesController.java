
package org.springcrazy.modules.cms.controller;

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
import org.springcrazy.modules.cms.entity.WebsiteImages;
import org.springcrazy.modules.cms.service.IWebsiteImagesService;
import org.springcrazy.modules.cms.vo.WebsiteImagesVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * banner图管理 控制器
 *
 * @author TongZhou
 * @since 2020-03-06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cms/websiteimages")
@Api(value = "banner图管理", tags = "banner图管理接口")
public class WebsiteImagesController extends CrazyController {

	private IWebsiteImagesService websiteImagesService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入websiteImages")
	public R<WebsiteImages> detail(WebsiteImages websiteImages) {
		WebsiteImages detail = websiteImagesService.getOne(Condition.getQueryWrapper(websiteImages));
		return R.data(detail);
	}

	/**
	 * 分页 banner图管理
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入websiteImages")
	public R<IPage<WebsiteImages>> list(WebsiteImages websiteImages, Query query) {
		QueryWrapper<WebsiteImages> queryWrapper =  Condition.getQueryWrapper(websiteImages);
		queryWrapper.lambda().orderByDesc(WebsiteImages::getSeriesNumber);
		queryWrapper.lambda().like(Func.isNotBlank(websiteImages.getTitle()), WebsiteImages::getTitle,websiteImages.getTitle());
		websiteImages.setTitle(null);
		IPage<WebsiteImages> pages = websiteImagesService.page(Condition.getPage(query), queryWrapper);
		return R.data(pages);
	}

	/**
	 * 自定义分页 banner图管理
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入websiteImages")
	public R<IPage<WebsiteImagesVO>> page(WebsiteImagesVO websiteImages, Query query) {
		IPage<WebsiteImagesVO> pages = websiteImagesService.selectWebsiteImagesPage(Condition.getPage(query), websiteImages);
		return R.data(pages);
	}

	/**
	 * 新增 banner图管理
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入websiteImages")
	public R save(@Valid @RequestBody WebsiteImages websiteImages) {
		return R.status(websiteImagesService.save(websiteImages));
	}

	/**
	 * 修改 banner图管理
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入websiteImages")
	public R update(@Valid @RequestBody WebsiteImages websiteImages) {
		return R.status(websiteImagesService.updateById(websiteImages));
	}

	/**
	 * 新增或修改 banner图管理
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入websiteImages")
	public R submit(@Valid @RequestBody WebsiteImages websiteImages) {
		return R.status(websiteImagesService.saveOrUpdate(websiteImages));
	}


	/**
	 * 删除 banner图管理
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(websiteImagesService.removeByIds(Func.toIntList(ids)));
	}


}
