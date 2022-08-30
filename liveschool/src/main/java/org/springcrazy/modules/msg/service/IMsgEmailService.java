
package org.springcrazy.modules.msg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.core.mp.base.BaseService;
import org.springcrazy.modules.msg.entity.MsgEmail;
import org.springcrazy.modules.msg.vo.MsgEmailVO;

/**
 * 邮件发送记录 服务类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
public interface IMsgEmailService extends BaseService<MsgEmail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param msgEmail
	 * @return
	 */
	IPage<MsgEmailVO> selectMsgEmailPage(IPage<MsgEmailVO> page, MsgEmailVO msgEmail);

	void sendMsg(MsgEmail msgEmail);

	boolean saveMsgEmail(MsgEmail msgEmail);
}
