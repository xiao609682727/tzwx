
package org.springcrazy.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.core.tool.node.ForestNodeMerger;
import org.springcrazy.modules.exam.entity.Point;
import org.springcrazy.modules.exam.mapper.PointMapper;
import org.springcrazy.modules.exam.service.IPointService;
import org.springcrazy.modules.exam.vo.PointVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 考点 服务实现类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@Service
public class PointServiceImpl extends ServiceImpl<PointMapper, Point> implements IPointService {

	@Override
	public IPage<PointVO> selectPointPage(IPage<PointVO> page, PointVO point) {
		return page.setRecords(baseMapper.selectPointPage(page, point));
	}

	@Override
	public List<PointVO> tree() {
		return ForestNodeMerger.merge(baseMapper.tree());
	}

}
