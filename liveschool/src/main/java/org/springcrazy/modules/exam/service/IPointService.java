
package org.springcrazy.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.exam.entity.Point;
import org.springcrazy.modules.exam.vo.PointVO;

import java.util.List;

/**
 * 考点 服务类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
public interface IPointService extends IService<Point> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param point
	 * @return
	 */
	IPage<PointVO> selectPointPage(IPage<PointVO> page, PointVO point);


	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<PointVO> tree();

}
