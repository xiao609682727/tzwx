
package org.springcrazy.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.core.secure.CrazyUser;
import org.springcrazy.core.tool.node.TreeNode;
import org.springcrazy.core.tool.support.Kv;
import org.springcrazy.modules.system.entity.Menu;
import org.springcrazy.modules.system.vo.MenuVO;

import java.util.List;

/**
 * 服务类
 *

 */
public interface IMenuService extends IService<Menu> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param menu
	 * @return
	 */
	IPage<MenuVO> selectMenuPage(IPage<MenuVO> page, MenuVO menu);

	/**
	 * 菜单树形结构
	 *
	 * @param roleId
	 * @return
	 */
	List<MenuVO> routes(String roleId,String parentId);

	/**
	 * 按钮树形结构
	 *
	 * @param roleId
	 * @return
	 */
	List<MenuVO> buttons(String roleId);

	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<TreeNode> tree();

	/**
	 * 授权树形结构
	 *
	 * @param user
	 * @return
	 */
	List<MenuVO> grantTree(CrazyUser user);

	/**
	 * 默认选中节点
	 *
	 * @param roleIds
	 * @return
	 */
	List<String> roleTreeKeys(String roleIds);

	/**
	 * 获取配置的角色权限
	 *
	 * @param user
	 * @return
	 */
	List<Kv> authRoutes(CrazyUser user);

	boolean deleteIds(String ids);

}
