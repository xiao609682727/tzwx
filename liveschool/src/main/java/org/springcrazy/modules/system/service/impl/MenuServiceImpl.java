
package org.springcrazy.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.Lists;
import lombok.AllArgsConstructor;
import org.springcrazy.core.secure.CrazyUser;
import org.springcrazy.core.tool.constant.CrazyConstant;
import org.springcrazy.core.tool.node.ForestNodeMerger;
import org.springcrazy.core.tool.node.TreeNode;
import org.springcrazy.core.tool.support.Kv;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.system.dto.MenuDTO;
import org.springcrazy.modules.system.entity.Menu;
import org.springcrazy.modules.system.entity.RoleMenu;
import org.springcrazy.modules.system.mapper.MenuMapper;
import org.springcrazy.modules.system.service.IMenuService;
import org.springcrazy.modules.system.service.IRoleMenuService;
import org.springcrazy.modules.system.vo.MenuVO;
import org.springcrazy.modules.system.wrapper.MenuWrapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *

 */
@Service
@AllArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

	IRoleMenuService roleMenuService;

	@Override
	public IPage<MenuVO> selectMenuPage(IPage<MenuVO> page, MenuVO menu) {
		return page.setRecords(baseMapper.selectMenuPage(page, menu));
	}

	@Override
	public List<MenuVO> routes(String roleId,String parentId) {
		List<Menu> allMenus = baseMapper.allMenu();
		List<Menu> roleMenus = baseMapper.roleMenu(Func.toIntList(roleId));
		List<Menu> routes = new LinkedList<>(roleMenus);
		roleMenus.forEach(roleMenu -> recursion(allMenus, routes, roleMenu));
		MenuWrapper menuWrapper = new MenuWrapper();
		List<Menu> collect = routes.stream().filter(x -> Func.equals(x.getCategory(), 1)).sorted(Comparator.comparing(Menu::getSort).reversed().thenComparing(Menu::getId)).collect(Collectors.toList());
		List<MenuVO> menuVOS = menuWrapper.listNodeVO(collect);
		if(!(Func.equals(parentId,"undefined")||Func.equals(parentId,"0"))){
			List<MenuVO> menuList = Lists.newArrayList();
			for (MenuVO menuVO : menuVOS) {
				if(Func.equals(Func.toStr(menuVO.getId()), parentId)){
					menuVO.getChildren().forEach(iNode -> menuList.add((MenuVO) iNode));
				}
			}
			menuVOS = menuList;
		}

		return menuVOS;
	}

	public void recursion(List<Menu> allMenus, List<Menu> routes, Menu roleMenu) {
		Optional<Menu> menu = allMenus.stream().filter(x -> Func.equals(x.getId(), roleMenu.getParentId())).findFirst();
		if (menu.isPresent() && !routes.contains(menu.get())) {
			routes.add(menu.get());
			recursion(allMenus, routes, menu.get());
		}
	}

	@Override
	public List<MenuVO> buttons(String roleId) {
		List<Menu> buttons = baseMapper.buttons(Func.toIntList(roleId));
		MenuWrapper menuWrapper = new MenuWrapper();
		return menuWrapper.listNodeVO(buttons);
	}

	@Override
	public List<TreeNode> tree() {
		List<TreeNode> merge = ForestNodeMerger.merge(baseMapper.tree());
		List<TreeNode> m = Lists.newArrayList();
		for (TreeNode treeNode : merge) {
			if(treeNode.getParentId() == 0){
				m.add(treeNode);
			}
		}
		return m;
	}

	@Override
	public List<MenuVO> grantTree(CrazyUser user) {
		return ForestNodeMerger.merge(user.getTenantId().equals(CrazyConstant.ADMIN_TENANT_ID) ? baseMapper.grantTree() : baseMapper.grantTreeByRole(Func.toIntList(user.getRoleId())));
	}

	@Override
	public List<String> roleTreeKeys(String roleIds) {
		List<RoleMenu> roleMenus = roleMenuService.list(Wrappers.<RoleMenu>query().lambda().in(RoleMenu::getRoleId, Func.toIntList(roleIds)));
		return roleMenus.stream().map(roleMenu -> Func.toStr(roleMenu.getMenuId())).collect(Collectors.toList());
	}

	@Override
	public List<Kv> authRoutes(CrazyUser user) {
		List<MenuDTO> routes = baseMapper.authRoutes(Func.toIntList(user.getRoleId()));
		List<Kv> list = new ArrayList<>();
		routes.forEach(route -> list.add(Kv.init().set(route.getPath(), Kv.init().set("authority", Func.toStrArray(route.getAlias())))));
		return list;
	}

	@Override
	public boolean deleteIds(String ids) {
		Func.toIntList(ids).forEach(id ->deleteId(id));
		return true;
	}

	/**
	 * 递归删除文件
	 * @param id
	 */
	public void deleteId(Integer id){
		this.removeById(id);
		List<Menu> list = this.list(new QueryWrapper<Menu>().eq("parent_id", id));
		list.forEach(resourceFile -> {
			deleteId(resourceFile.getId());
		});
	}

}
