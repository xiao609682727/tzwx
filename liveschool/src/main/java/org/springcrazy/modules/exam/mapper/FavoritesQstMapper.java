
package org.springcrazy.modules.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.exam.entity.FavoritesQst;
import org.springcrazy.modules.exam.vo.FavoritesQstVO;

import java.util.List;
import java.util.Map;

/**
 * 考试试题收藏表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-12-17
 */
public interface FavoritesQstMapper extends BaseMapper<FavoritesQst> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param favoritesQst
	 * @return
	 */
	List<FavoritesQstVO> selectFavoritesQstPage(IPage page, @Param("ew")FavoritesQstVO favoritesQst);


	List<FavoritesQstVO> selectFavoritesQstList(FavoritesQstVO favoritesQst);

	/**
	 * 查询用户7天内收藏的试题数量
	 *
	 * @param
	 * @param
	 * @return
	 */
	int getFavoriteNums(Map<String,Object> param);
}
