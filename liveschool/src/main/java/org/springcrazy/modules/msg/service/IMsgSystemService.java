
package org.springcrazy.modules.msg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.core.mp.base.BaseService;
import org.springcrazy.modules.msg.entity.MsgSystem;
import org.springcrazy.modules.msg.vo.MsgSystemVO;

/**
 * 系统消息 服务类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
public interface IMsgSystemService extends BaseService<MsgSystem> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param msgSystem
	 * @return
	 */
	IPage<MsgSystemVO> selectMsgSystemPage(IPage<MsgSystemVO> page, MsgSystemVO msgSystem);

	boolean saveMsgSystem(MsgSystem msgSystem);

}
