
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
import org.springcrazy.modules.exam.entity.PaperType;
import org.springcrazy.modules.exam.service.IPaperTypeService;
import org.springcrazy.modules.exam.vo.PaperTypeVO;
import org.springcrazy.modules.exam.wrapper.PaperTypeWrapper;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 试卷类型表 控制器
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exam/papertype")
@Api(value = "试卷类型表", tags = "试卷类型表接口")
public class PaperTypeController extends CrazyController {

	private IPaperTypeService paperTypeService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入paperType")
	public R<PaperTypeVO> detail(PaperType paperType) {
		PaperType detail = paperTypeService.getOne(Condition.getQueryWrapper(paperType).lambda().orderByDesc(PaperType::getSort));
		return R.data(PaperTypeWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 试卷类型表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入paperType")
	public R<IPage<PaperTypeVO>> list(PaperType paperType, Query query) {
		IPage<PaperType> pages = paperTypeService.page(Condition.getPage(query), Condition.getQueryWrapper(paperType).lambda().orderByDesc(PaperType::getSort));
		return R.data(PaperTypeWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 试卷类型表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入paperType")
	public R<IPage<PaperTypeVO>> page(PaperTypeVO paperType, Query query) {
		IPage<PaperTypeVO> pages = paperTypeService.selectPaperTypePage(Condition.getPage(query), paperType);
		return R.data(pages);
	}

	/**
	 * 新增 试卷类型表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入paperType")
	public R save(@Valid @RequestBody PaperType paperType) {
		paperType.setAddtime(LocalDateTime.now());
		return R.status(paperTypeService.save(paperType));
	}

	/**
	 * 修改 试卷类型表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入paperType")
	public R update(@Valid @RequestBody PaperType paperType) {
		return R.status(paperTypeService.updateById(paperType));
	}

	/**
	 * 新增或修改 试卷类型表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入paperType")
	public R submit(@Valid @RequestBody PaperType paperType) {
		return R.status(paperTypeService.saveOrUpdate(paperType));
	}


	/**
	 * 删除 试卷类型表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(paperTypeService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 分页 专业信息
	 */
	@GetMapping("/dictionary")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "字典", notes = "传入subject")
	public R<List<PaperType>> list(@ApiIgnore @RequestParam Map<String, Object> paperType) {
		return R.data(paperTypeService.list(Condition.getQueryWrapper(paperType, PaperType.class).lambda().orderByDesc(PaperType::getSort)));
	}


}
