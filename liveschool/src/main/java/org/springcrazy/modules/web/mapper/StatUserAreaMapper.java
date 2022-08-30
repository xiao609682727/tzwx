
package org.springcrazy.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.web.entity.StatUserArea;
import org.springcrazy.modules.web.vo.StatUserAreaVO;

import java.util.List;

/**
 * 用户ip登录所在区域记录 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-20
 */
public interface StatUserAreaMapper extends BaseMapper<StatUserArea> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param statUserArea
	 * @return
	 */
	List<StatUserAreaVO> selectStatUserAreaPage(IPage page, StatUserAreaVO statUserArea);

}
