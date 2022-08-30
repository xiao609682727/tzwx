
package org.springcrazy.modules.cms.controller;

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
import org.springcrazy.modules.cms.entity.Article;
import org.springcrazy.modules.cms.service.IArticleService;
import org.springcrazy.modules.cms.vo.ArticleVO;
import org.springcrazy.modules.cms.wrapper.ArticleWrapper;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Map;

/**
 * 文章信息表 控制器
 *
 * @author TongZhou
 * @since 2020-02-28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cms/article")
@Api(value = "文章信息表", tags = "文章信息表接口")
public class ArticleController extends CrazyController {

	private IArticleService articleService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入article")
	public R<ArticleVO> detail(Article article) {
		Article detail = articleService.getOne(Condition.getQueryWrapper(article));
		return R.data(ArticleWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 文章信息表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入article")
	public R<IPage<ArticleVO>> list(@ApiIgnore @RequestParam Map<String, Object> article, Query query) {
		IPage<Article> pages = articleService.page(Condition.getPage(query), Condition.getQueryWrapper(article, Article.class).lambda().orderByDesc(Article::getSort));
		return R.data(ArticleWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 文章信息表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入article")
	public R<IPage<ArticleVO>> page(ArticleVO article, Query query) {
		IPage<ArticleVO> pages = articleService.selectArticlePage(Condition.getPage(query), article);
		return R.data(pages);
	}

	/**
	 * 新增 文章信息表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入article")
	public R save(@Valid @RequestBody Article article) {
		return R.status(articleService.save(article));
	}

	/**
	 * 修改 文章信息表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入article")
	public R update(@Valid @RequestBody Article article) {
		return R.status(articleService.updateById(article));
	}

	/**
	 * 新增或修改 文章信息表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入article")
	public R submit(@Valid @RequestBody Article article) {
		return R.status(articleService.saveOrUpdate(article));
	}


	/**
	 * 删除 文章信息表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(articleService.deleteLogic(Func.toIntList(ids)));
	}


}
