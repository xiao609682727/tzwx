
package org.springcrazy.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.exam.entity.FavoritesQst;
import org.springcrazy.modules.exam.vo.FavoritesQstVO;

import java.util.List;
import java.util.Map;

/**
 * 考试试题收藏表 服务类
 *
 * @author TongZhou
 * @since 2020-12-17
 */
public interface IFavoritesQstService extends IService<FavoritesQst> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param favoritesQst
	 * @return
	 */
	IPage<FavoritesQstVO> selectFavoritesQstPage(IPage<FavoritesQstVO> page, FavoritesQstVO favoritesQst);


	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param favoritesQst
	 * @return
	 */
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
