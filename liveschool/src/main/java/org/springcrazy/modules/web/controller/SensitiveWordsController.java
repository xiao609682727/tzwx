
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
import org.springcrazy.modules.web.entity.SensitiveWords;
import org.springcrazy.modules.web.service.ISensitiveWordsService;
import org.springcrazy.modules.web.vo.SensitiveWordsVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 网站敏感词 控制器
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/web/sensitivewords")
@Api(value = "网站敏感词", tags = "网站敏感词接口")
public class SensitiveWordsController extends CrazyController {

	private ISensitiveWordsService sensitiveWordsService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入sensitiveWords")
	public R<SensitiveWords> detail(SensitiveWords sensitiveWords) {
		SensitiveWords detail = sensitiveWordsService.getOne(Condition.getQueryWrapper(sensitiveWords));
		return R.data(detail);
	}

	/**
	 * 分页 网站敏感词
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入sensitiveWords")
	public R<IPage<SensitiveWords>> list(SensitiveWords sensitiveWords, Query query) {
		IPage<SensitiveWords> pages = sensitiveWordsService.selectSensitiveWordsPageList(Condition.getPage(query),sensitiveWords);
		return R.data(pages);
	}

	/**
	 * 自定义分页 网站敏感词
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入sensitiveWords")
	public R<IPage<SensitiveWordsVO>> page(SensitiveWordsVO sensitiveWords, Query query) {
		IPage<SensitiveWordsVO> pages = sensitiveWordsService.selectSensitiveWordsPage(Condition.getPage(query), sensitiveWords);
		return R.data(pages);
	}

	/**
	 * 新增 网站敏感词
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入sensitiveWords")
	public R save(@Valid @RequestBody SensitiveWords sensitiveWords) {
		return R.status(sensitiveWordsService.save(sensitiveWords));
	}

	/**
	 * 修改 网站敏感词
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入sensitiveWords")
	public R update(@Valid @RequestBody SensitiveWords sensitiveWords) {
		return R.status(sensitiveWordsService.updateById(sensitiveWords));
	}

	/**
	 * 新增或修改 网站敏感词
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入sensitiveWords")
	public R submit(@Valid @RequestBody SensitiveWords sensitiveWords) {
		return R.status(sensitiveWordsService.saveOrUpdate(sensitiveWords));
	}


	/**
	 * 删除 网站敏感词
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(sensitiveWordsService.removeByIds(Func.toIntList(ids)));
	}


}
