
package org.springcrazy.modules.system.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.secure.CrazyUser;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.node.TreeNode;
import org.springcrazy.core.tool.support.Kv;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.system.entity.Menu;
import org.springcrazy.modules.system.service.IMenuService;
import org.springcrazy.modules.system.vo.MenuVO;
import org.springcrazy.modules.system.wrapper.MenuWrapper;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 *

 */
@ApiIgnore
@RestController
@AllArgsConstructor
@RequestMapping("/crazy-system/menu")
@Api(value = "菜单", tags = "菜单")
@Slf4j
public class MenuController extends CrazyController {

	private IMenuService menuService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入menu")
	public R<MenuVO> detail(Menu menu) {
		Menu detail = menuService.getOne(Condition.getQueryWrapper(menu));
		return R.data(MenuWrapper.build().entityVO(detail));
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "code", value = "菜单编号", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "name", value = "菜单名称", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "列表", notes = "传入menu")
	public R<List<MenuVO>> list(@ApiIgnore @RequestParam Map<String, Object> menu) {
		List<Menu> list = menuService.list(Condition.getQueryWrapper(menu, Menu.class).lambda().orderByDesc(Menu::getSort));
		return R.data(MenuWrapper.build().listNodeVO(list));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "新增或修改", notes = "传入menu")
	public R submit(@Valid @RequestBody Menu menu) {
		return R.status(menuService.saveOrUpdate(menu));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(menuService.deleteIds(ids));
	}

	/**
	 * 前端菜单数据
	 */
	@GetMapping("/routes")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "前端菜单数据", notes = "前端菜单数据")
	public R<List<MenuVO>> routes(CrazyUser user,@RequestParam(defaultValue = "") String parentId) {
		List<MenuVO> list = menuService.routes(user.getRoleId(),parentId);
		return R.data(list);
	}

	/**
	 * 前端菜单数据
	 */
	@GetMapping("/getTopMenu")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "前端菜单数据", notes = "前端菜单数据")
	public R<List<Map<String,Object>>> getTopMenu() {
		Menu menu = new Menu();
		menu.setParentId(0);
		List<Menu> list = menuService.list(Condition.getQueryWrapper(menu).lambda().orderByDesc(Menu::getSort));
		List<Map<String,Object>> resultMap = Lists.newArrayList();
		list.stream().forEach(menuVO -> {
			Map<String,Object> map = Maps.newHashMap();
			map.put("label",menuVO.getName());
			map.put("path",menuVO.getPath());
			map.put("icon",menuVO.getSource());
			map.put("parentId",menuVO.getId());
			resultMap.add(map);
		});
		return R.data(resultMap);
	}

	/**
	 * 前端按钮数据
	 */
	@GetMapping("/buttons")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "前端按钮数据", notes = "前端按钮数据")
	public R<List<MenuVO>> buttons(CrazyUser user) {
		List<MenuVO> list = menuService.buttons(user.getRoleId());
		return R.data(list);
	}

	/**
	 * 获取菜单树形结构
	 */
	@GetMapping("/tree")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<TreeNode>> tree() {
		List<TreeNode> tree = menuService.tree();
		return R.data(tree);
	}

	/**
	 * 获取权限分配树形结构
	 */
	@GetMapping("/grant-tree")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "权限分配树形结构", notes = "权限分配树形结构")
	public R<List<MenuVO>> grantTree(CrazyUser user) {
		return R.data(menuService.grantTree(user));
	}

	/**
	 * 获取权限分配树形结构
	 */
	@GetMapping("/role-tree-keys")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "角色所分配的树", notes = "角色所分配的树")
	public R<List<String>> roleTreeKeys(String roleIds) {
		return R.data(menuService.roleTreeKeys(roleIds));
	}

	/**
	 * 获取配置的角色权限
	 */
	@GetMapping("auth-routes")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "菜单的角色权限")
	public R<List<Kv>> authRoutes(CrazyUser user) {
		if (Func.isEmpty(user)) {
			return null;
		}
		return R.data(menuService.authRoutes(user));
	}

}
