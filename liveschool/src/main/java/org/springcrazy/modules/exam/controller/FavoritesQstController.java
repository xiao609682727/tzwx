
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
import org.springcrazy.modules.exam.entity.FavoritesQst;
import org.springcrazy.modules.exam.service.IFavoritesQstService;
import org.springcrazy.modules.exam.vo.FavoritesQstVO;
import org.springcrazy.modules.exam.wrapper.FavoritesQstWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 考试试题收藏表 控制器
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exam/favoritesqst")
@Api(value = "考试试题收藏表", tags = "考试试题收藏表接口")
public class FavoritesQstController extends CrazyController {

	private IFavoritesQstService favoritesQstService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入favoritesQst")
	public R<FavoritesQstVO> detail(FavoritesQst favoritesQst) {
		FavoritesQst detail = favoritesQstService.getOne(Condition.getQueryWrapper(favoritesQst));
		return R.data(FavoritesQstWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 考试试题收藏表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入favoritesQst")
	public R<IPage<FavoritesQstVO>> list(FavoritesQst favoritesQst, Query query) {
		IPage<FavoritesQst> pages = favoritesQstService.page(Condition.getPage(query), Condition.getQueryWrapper(favoritesQst));
		return R.data(FavoritesQstWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 考试试题收藏表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入favoritesQst")
	public R<IPage<FavoritesQstVO>> page(FavoritesQstVO favoritesQst, Query query) {
		IPage<FavoritesQstVO> pages = favoritesQstService.selectFavoritesQstPage(Condition.getPage(query), favoritesQst);
		return R.data(pages);
	}

	/**
	 * 新增 考试试题收藏表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入favoritesQst")
	public R save(@Valid @RequestBody FavoritesQst favoritesQst) {
		return R.status(favoritesQstService.save(favoritesQst));
	}

	/**
	 * 修改 考试试题收藏表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入favoritesQst")
	public R update(@Valid @RequestBody FavoritesQst favoritesQst) {
		return R.status(favoritesQstService.updateById(favoritesQst));
	}

	/**
	 * 新增或修改 考试试题收藏表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入favoritesQst")
	public R submit(@Valid @RequestBody FavoritesQst favoritesQst) {
		return R.status(favoritesQstService.saveOrUpdate(favoritesQst));
	}


	/**
	 * 删除 考试试题收藏表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(favoritesQstService.removeByIds(Func.toIntList(ids)));
	}


}
