
package org.springcrazy.modules.edu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.edu.entity.CourseFavorites;
import org.springcrazy.modules.edu.mapper.CourseFavoritesMapper;
import org.springcrazy.modules.edu.service.ICourseFavoritesService;
import org.springcrazy.modules.edu.vo.CourseFavoritesVO;
import org.springframework.stereotype.Service;

/**
 * 收藏 服务实现类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Service
public class CourseFavoritesServiceImpl extends ServiceImpl<CourseFavoritesMapper, CourseFavorites> implements ICourseFavoritesService {

	@Override
	public IPage<CourseFavoritesVO> selectCourseFavoritesPage(IPage<CourseFavoritesVO> page, CourseFavoritesVO courseFavorites) {
		return page.setRecords(baseMapper.selectCourseFavoritesPage(page, courseFavorites));
	}

}
