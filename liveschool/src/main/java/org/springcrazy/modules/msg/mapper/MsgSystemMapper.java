
package org.springcrazy.modules.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.msg.entity.MsgSystem;
import org.springcrazy.modules.msg.vo.MsgSystemVO;

import java.util.List;

/**
 * 系统消息 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-18
 */
public interface MsgSystemMapper extends BaseMapper<MsgSystem> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param msgSystem
	 * @return
	 */
	List<MsgSystemVO> selectMsgSystemPage(IPage page, MsgSystemVO msgSystem);

}
