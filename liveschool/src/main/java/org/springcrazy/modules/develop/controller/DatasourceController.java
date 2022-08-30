
package org.springcrazy.modules.develop.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.launch.constant.AppConstant;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.develop.entity.Datasource;
import org.springcrazy.modules.develop.service.IDatasourceService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据源配置表 控制器
 *

 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_DEVELOP_NAME + "/datasource")
@Api(value = "数据源配置表", tags = "数据源配置表接口")
public class DatasourceController extends CrazyController {

	private IDatasourceService datasourceService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入datasource")
	public R<Datasource> detail(Datasource datasource) {
		Datasource detail = datasourceService.getOne(Condition.getQueryWrapper(datasource));
		return R.data(detail);
	}

	/**
	 * 分页 数据源配置表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入datasource")
	public R<IPage<Datasource>> list(Datasource datasource, Query query) {
		IPage<Datasource> pages = datasourceService.page(Condition.getPage(query), Condition.getQueryWrapper(datasource));
		return R.data(pages);
	}

	/**
	 * 新增 数据源配置表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入datasource")
	public R save(@Valid @RequestBody Datasource datasource) {
		return R.status(datasourceService.save(datasource));
	}

	/**
	 * 修改 数据源配置表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入datasource")
	public R update(@Valid @RequestBody Datasource datasource) {
		return R.status(datasourceService.updateById(datasource));
	}

	/**
	 * 新增或修改 数据源配置表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入datasource")
	public R submit(@Valid @RequestBody Datasource datasource) {
		datasource.setUrl(datasource.getUrl().replace("&amp;", "&"));
		return R.status(datasourceService.saveOrUpdate(datasource));
	}


	/**
	 * 删除 数据源配置表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(datasourceService.deleteLogic(Func.toIntList(ids)));
	}

	/**
	 * 数据源列表
	 */
	@GetMapping("/select")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "下拉数据源", notes = "查询列表")
	public R<List<Datasource>> select() {
		List<Datasource> list = datasourceService.list();
		return R.data(list);
	}

}
