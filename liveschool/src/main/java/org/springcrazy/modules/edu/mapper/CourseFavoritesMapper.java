
package org.springcrazy.modules.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.edu.entity.CourseFavorites;
import org.springcrazy.modules.edu.vo.CourseFavoritesVO;

import java.util.List;

/**
 * 收藏 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-04-25
 */
public interface CourseFavoritesMapper extends BaseMapper<CourseFavorites> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param courseFavorites
	 * @return
	 */
	List<CourseFavoritesVO> selectCourseFavoritesPage(IPage page,@Param("courseFavorites") CourseFavoritesVO courseFavorites);

}
