
package org.springcrazy.modules.exam.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.node.ForestNodeMerger;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.exam.entity.Point;
import org.springcrazy.modules.exam.vo.PointVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 考点包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-12-01
 */
public class PointWrapper extends BaseEntityWrapper<Point, PointVO>  {

    public static PointWrapper build() {
        return new PointWrapper();
    }

	@Override
	public PointVO entityVO(Point point) {
		PointVO pointVO = BeanUtil.copy(point, PointVO.class);

		return pointVO;
	}

	public List<PointVO> listNodeVO(List<Point> list) {
		List<PointVO> collect = list.stream().map(subject -> BeanUtil.copy(subject, PointVO.class)).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}
}
