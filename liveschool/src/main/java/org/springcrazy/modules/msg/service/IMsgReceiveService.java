
package org.springcrazy.modules.msg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.msg.entity.MsgReceive;
import org.springcrazy.modules.msg.vo.MsgReceiveVO;

/**
 * 站内信 服务类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
public interface IMsgReceiveService extends IService<MsgReceive> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param msgReceive
	 * @return
	 */
	IPage<MsgReceiveVO> selectMsgReceivePage(IPage<MsgReceiveVO> page, MsgReceiveVO msgReceive);

}
