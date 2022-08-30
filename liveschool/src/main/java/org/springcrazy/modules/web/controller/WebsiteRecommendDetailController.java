
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
import org.springcrazy.modules.web.entity.WebsiteRecommendDetail;
import org.springcrazy.modules.web.service.IWebsiteRecommendDetailService;
import org.springcrazy.modules.web.vo.WebsiteRecommendDetailVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推荐详情表 控制器
 *
 * @author TongZhou
 * @since 2020-05-13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/web/websiterecommenddetail")
@Api(value = "推荐详情表", tags = "推荐详情表接口")
public class WebsiteRecommendDetailController extends CrazyController {

	private IWebsiteRecommendDetailService websiteRecommendDetailService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入websiteRecommendDetail")
	public R<WebsiteRecommendDetail> detail(WebsiteRecommendDetail websiteRecommendDetail) {
		WebsiteRecommendDetail detail = websiteRecommendDetailService.getOne(Condition.getQueryWrapper(websiteRecommendDetail));
		return R.data(detail);
	}

	/**
	 * 分页 推荐详情表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入websiteRecommendDetail")
	public R<IPage<WebsiteRecommendDetail>> list(WebsiteRecommendDetail websiteRecommendDetail, Query query) {
		IPage<WebsiteRecommendDetail> pages = websiteRecommendDetailService.page(Condition.getPage(query), Condition.getQueryWrapper(websiteRecommendDetail));
		return R.data(pages);
	}

	/**
	 * 自定义分页 推荐详情表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入websiteRecommendDetail")
	public R<IPage<WebsiteRecommendDetailVO>> page(WebsiteRecommendDetailVO websiteRecommendDetail, Query query) {
		IPage<WebsiteRecommendDetailVO> pages = websiteRecommendDetailService.selectWebsiteRecommendDetailPage(Condition.getPage(query), websiteRecommendDetail);
		return R.data(pages);
	}

	/**
	 * 新增 推荐详情表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入websiteRecommendDetail")
	public R save(@Valid @RequestBody WebsiteRecommendDetail websiteRecommendDetail) {
		return R.status(websiteRecommendDetailService.save(websiteRecommendDetail));
	}

	/**
	 * 修改 推荐详情表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入websiteRecommendDetail")
	public R update(@Valid @RequestBody WebsiteRecommendDetail websiteRecommendDetail) {
		return R.status(websiteRecommendDetailService.updateById(websiteRecommendDetail));
	}

	/**
	 * 新增或修改 推荐详情表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入websiteRecommendDetail")
	public R submit(@Valid @RequestBody WebsiteRecommendDetail websiteRecommendDetail) {
		return R.status(websiteRecommendDetailService.saveOrUpdate(websiteRecommendDetail));
	}


	/**
	 * 删除 推荐详情表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(websiteRecommendDetailService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 新增 推荐详情表
	 */
	@PostMapping("/saveBatch")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入websiteRecommendDetail")
	public R save(@Valid @RequestBody List<WebsiteRecommendDetail> websiteRecommendDetailList,Integer recommendId ) {
		//查询当前推荐下的课程
		QueryWrapper<WebsiteRecommendDetail> websiteRecommendDetailQueryWrapper = new QueryWrapper<>();
		websiteRecommendDetailQueryWrapper.lambda().eq(Func.isNotEmpty(recommendId),WebsiteRecommendDetail::getRecommendId,recommendId);
		List<WebsiteRecommendDetail> list = websiteRecommendDetailService.list(websiteRecommendDetailQueryWrapper);
		List<Integer> ids = list.stream().map(detail -> detail.getBusId()).collect(Collectors.toList());
		//筛选推荐课程下的未被添加的课程
		List<WebsiteRecommendDetail> addList = websiteRecommendDetailList.stream().filter(detail -> !ids.contains(detail.getBusId())).collect(Collectors.toList());
		if(addList.size() > 0 ){
			websiteRecommendDetailService.saveBatch(addList);
			return R.status(true);
		}else{
			return R.fail("课程已添加");
		}
	}

}
