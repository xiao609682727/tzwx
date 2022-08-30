
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
import org.springcrazy.modules.web.entity.HelpMenu;
import org.springcrazy.modules.web.service.IHelpMenuService;
import org.springcrazy.modules.web.vo.HelpMenuVO;
import org.springcrazy.modules.web.wrapper.HelpMenuWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 帮助菜单 控制器
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/web/helpmenu")
@Api(value = "帮助菜单", tags = "帮助菜单接口")
public class HelpMenuController extends CrazyController {

	private IHelpMenuService helpMenuService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入helpMenu")
	public R<HelpMenu> detail(HelpMenu helpMenu) {
		HelpMenu detail = helpMenuService.getOne(Condition.getQueryWrapper(helpMenu));
		return R.data(detail);
	}


	/**
	 * 分页 帮助菜单
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入helpMenu")
	public R<List<HelpMenuVO>> list(HelpMenu helpMenu) {
		List<HelpMenu> list = helpMenuService.list(Condition.getQueryWrapper(helpMenu).lambda().orderByDesc(HelpMenu::getSort));
		HelpMenuWrapper.build().listNodeVO(list);
		return R.data(HelpMenuWrapper.build().listNodeVO(list));
	}

	/**
	 * 自定义分页 帮助菜单
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入helpMenu")
	public R<IPage<HelpMenuVO>> page(HelpMenuVO helpMenu, Query query) {
		IPage<HelpMenuVO> pages = helpMenuService.selectHelpMenuPage(Condition.getPage(query), helpMenu);
		return R.data(pages);
	}

	/**
	 * 新增 帮助菜单
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入helpMenu")
	public R save(@Valid @RequestBody HelpMenu helpMenu) {
		return R.status(helpMenuService.save(helpMenu));
	}

	/**
	 * 修改 帮助菜单
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入helpMenu")
	public R update(@Valid @RequestBody HelpMenu helpMenu) {
		return R.status(helpMenuService.updateById(helpMenu));
	}

	/**
	 * 新增或修改 帮助菜单
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入helpMenu")
	public R submit(@Valid @RequestBody HelpMenu helpMenu) {
		return R.status(helpMenuService.saveOrUpdate(helpMenu));
	}


	/**
	 * 删除 帮助菜单
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(helpMenuService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * 获取菜单树形结构
	 */
	@GetMapping("/tree")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<HelpMenuVO>> tree() {
		List<HelpMenuVO> tree = helpMenuService.tree();
		return R.data(tree);
	}
}
