
package org.springcrazy.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.exam.entity.FavoritesQst;
import org.springcrazy.modules.exam.mapper.FavoritesQstMapper;
import org.springcrazy.modules.exam.service.IFavoritesQstService;
import org.springcrazy.modules.exam.vo.FavoritesQstVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 考试试题收藏表 服务实现类
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@Service
public class FavoritesQstServiceImpl extends ServiceImpl<FavoritesQstMapper, FavoritesQst> implements IFavoritesQstService {

	@Override
	public IPage<FavoritesQstVO> selectFavoritesQstPage(IPage<FavoritesQstVO> page, FavoritesQstVO favoritesQst) {
		return page.setRecords(baseMapper.selectFavoritesQstPage(page, favoritesQst));
	}

	@Override
	public List<FavoritesQstVO> selectFavoritesQstList(FavoritesQstVO favoritesQst) {
		return baseMapper.selectFavoritesQstList(favoritesQst);
	}

	@Override
	public int getFavoriteNums(Map<String,Object> param) {
		return baseMapper.getFavoriteNums(param);
	}

}
