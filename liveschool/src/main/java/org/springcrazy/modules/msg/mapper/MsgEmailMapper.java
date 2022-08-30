
package org.springcrazy.modules.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.msg.entity.MsgEmail;
import org.springcrazy.modules.msg.vo.MsgEmailVO;

import java.util.List;

/**
 * 邮件发送记录 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-18
 */
public interface MsgEmailMapper extends BaseMapper<MsgEmail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param msgEmail
	 * @return
	 */
	List<MsgEmailVO> selectMsgEmailPage(IPage page, MsgEmailVO msgEmail);

}
