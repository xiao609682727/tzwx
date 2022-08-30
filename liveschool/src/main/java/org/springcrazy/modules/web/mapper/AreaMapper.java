
package org.springcrazy.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.web.entity.Area;
import org.springcrazy.modules.web.vo.AreaVO;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-19
 */
public interface AreaMapper extends BaseMapper<Area> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param area
	 * @return
	 */
	List<AreaVO> selectAreaPage(IPage page, AreaVO area);

}
