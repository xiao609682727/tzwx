
package org.springcrazy.modules.cms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.common.tool.CacheUtils;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.modules.cms.entity.CmsSubject;
import org.springcrazy.modules.cms.service.ICmsSubjectService;
import org.springcrazy.modules.cms.vo.CmsSubjectVO;
import org.springcrazy.modules.cms.wrapper.CmsSubjectWrapper;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 专业信息 控制器
 *
 * @author TongZhou
 * @since 2020-02-28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cms/subject")
@Api(value = "专业信息", tags = "专业信息接口")
public class CmsSubjectController extends CrazyController {

	private ICmsSubjectService subjectService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入subject")
	public R<CmsSubjectVO> detail(CmsSubject subject) {
		CmsSubject detail = subjectService.getOne(Condition.getQueryWrapper(subject));
		return R.data(CmsSubjectWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 专业信息
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入subject")
	public R<IPage<CmsSubjectVO>> list(@ApiIgnore @RequestParam Map<String, Object> subject, Query query) {
		IPage<CmsSubject> pages = subjectService.page(Condition.getPage(query), Condition.getQueryWrapper(subject, CmsSubject.class).lambda().orderByDesc(CmsSubject::getSort));
		return R.data(CmsSubjectWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 专业信息
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入subject")
	public R<IPage<CmsSubjectVO>> page(CmsSubjectVO subject, Query query) {
		IPage<CmsSubjectVO> pages = subjectService.selectSubjectPage(Condition.getPage(query), subject);
		return R.data(pages);
	}

	/**
	 * 新增 专业信息
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入subject")
	public R save(@Valid @RequestBody CmsSubject subject) {
		CacheUtils.remove("subjectMap");
		return R.status(subjectService.save(subject));
	}

	/**
	 * 修改 专业信息
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入subject")
	public R update(@Valid @RequestBody CmsSubject subject) {
		CacheUtils.remove("subjectMap");
		return R.status(subjectService.updateById(subject));
	}

	/**
	 * 新增或修改 专业信息
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入subject")
	public R submit(@Valid @RequestBody CmsSubject subject) {
		CacheUtils.remove("subjectMap");
		return R.status(subjectService.saveOrUpdate(subject));
	}


	/**
	 * 删除 专业信息
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtils.remove("subjectMap");
		return R.status(subjectService.deleteIds(ids));
	}

	/**
	 * 分页 专业信息
	 */
	@GetMapping("/dictionary")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "字典", notes = "传入subject")
	public R<List<CmsSubject>> list(@ApiIgnore @RequestParam Map<String, Object> subject) {
		return R.data(subjectService.list(Condition.getQueryWrapper(subject, CmsSubject.class).lambda().orderByDesc(CmsSubject::getSort)));
	}
}
