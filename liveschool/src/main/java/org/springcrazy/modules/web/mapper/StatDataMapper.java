
package org.springcrazy.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.web.entity.StatData;
import org.springcrazy.modules.web.vo.StatDataVO;

import java.util.List;
import java.util.Map;

/**
 *  Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-19
 */
public interface StatDataMapper extends BaseMapper<StatData> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param statData
	 * @return
	 */
	List<StatDataVO> selectStatDataPage(IPage page, StatDataVO statData);

	List<Map<String,String>> selectStudentByArea();


}
