
package org.springcrazy.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.system.entity.RoleMenu;
import org.springcrazy.modules.system.vo.RoleMenuVO;

import java.util.List;

/**
 * Mapper 接口

 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

	/**
	 * 自定义分页
	 * @param page
	 * @param roleMenu
	 * @return
	 */
	List<RoleMenuVO> selectRoleMenuPage(IPage page, RoleMenuVO roleMenu);

}
