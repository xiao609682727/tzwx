
package org.springcrazy.modules.exam.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.exam.entity.FavoritesQst;
import org.springcrazy.modules.exam.vo.FavoritesQstVO;

/**
 * 考试试题收藏表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-12-17
 */
public class FavoritesQstWrapper extends BaseEntityWrapper<FavoritesQst, FavoritesQstVO>  {

    public static FavoritesQstWrapper build() {
        return new FavoritesQstWrapper();
    }

	@Override
	public FavoritesQstVO entityVO(FavoritesQst favoritesQst) {
		FavoritesQstVO favoritesQstVO = BeanUtil.copy(favoritesQst, FavoritesQstVO.class);

		return favoritesQstVO;
	}

}
