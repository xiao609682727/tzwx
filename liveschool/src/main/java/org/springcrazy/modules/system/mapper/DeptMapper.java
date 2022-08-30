
package org.springcrazy.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.system.entity.Dept;
import org.springcrazy.modules.system.vo.DeptVO;

import java.util.List;

/**
 * Mapper 接口

 */
public interface DeptMapper extends BaseMapper<Dept> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param dept
	 * @return
	 */
	List<DeptVO> selectDeptPage(IPage page, DeptVO dept);

	/**
	 * 获取树形节点
	 *
	 * @param tenantId
	 * @return
	 */
	List<DeptVO> tree(String tenantId);


	/**
	 * 获取部门名
	 *
	 * @param ids
	 * @return
	 */
	List<String> getDeptNames(Long[] ids);
	/**
	 * 删除部门信息
	 * */
	void delectDeptIds(@Param("id") int id);

	/**
	 * 修改部门父集id
	 * */
	void updateDeptParentid(@Param("id") int id);
}
