
package org.springcrazy.modules.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.web.entity.MsgConfig;
import org.springcrazy.modules.web.vo.MsgConfigVO;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-09
 */
public interface MsgConfigMapper extends BaseMapper<MsgConfig> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param msgConfig
	 * @return
	 */
	List<MsgConfigVO> selectMsgConfigPage(IPage page, MsgConfigVO msgConfig);

}
