
package org.springcrazy.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.system.entity.Role;
import org.springcrazy.modules.system.vo.RoleVO;

import java.util.List;

/**
 * Mapper 接口

 */
public interface RoleMapper extends BaseMapper<Role> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param role
	 * @return
	 */
	List<RoleVO> selectRolePage(IPage page, RoleVO role);

	/**
	 * 获取树形节点
	 *
	 * @param tenantId
	 * @param excludeRole
	 * @return
	 */
	List<RoleVO> tree(String tenantId, String excludeRole);

	/**
	 * 获取角色名
	 *
	 * @param ids
	 * @return
	 */
	List<String> getRoleNames(Long[] ids);

}
