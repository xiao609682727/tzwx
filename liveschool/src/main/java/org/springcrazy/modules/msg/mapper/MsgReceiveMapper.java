
package org.springcrazy.modules.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.msg.entity.MsgReceive;
import org.springcrazy.modules.msg.vo.MsgReceiveVO;

import java.util.List;

/**
 * 站内信 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-18
 */
public interface MsgReceiveMapper extends BaseMapper<MsgReceive> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param msgReceive
	 * @return
	 */
	List<MsgReceiveVO> selectMsgReceivePage(IPage page, MsgReceiveVO msgReceive);

}
