
package org.springcrazy.modules.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.exam.entity.Point;
import org.springcrazy.modules.exam.vo.PointVO;

import java.util.List;

/**
 * 考点 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-12-01
 */
public interface PointMapper extends BaseMapper<Point> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param point
	 * @return
	 */
	List<PointVO> selectPointPage(IPage page, PointVO point);


	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<PointVO> tree();

}
