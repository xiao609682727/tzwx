
package org.springcrazy.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.system.entity.Param;
import org.springcrazy.modules.system.vo.ParamVO;

import java.util.List;

/**
 * Mapper 接口

 */
public interface ParamMapper extends BaseMapper<Param> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param param
	 * @return
	 */
	List<ParamVO> selectParamPage(IPage page, ParamVO param);

}
