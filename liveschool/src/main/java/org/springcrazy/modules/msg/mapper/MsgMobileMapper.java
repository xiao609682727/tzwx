
package org.springcrazy.modules.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.msg.entity.MsgMobile;
import org.springcrazy.modules.msg.vo.MsgMobileVO;

import java.util.List;

/**
 * 手机短信发送记录 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-18
 */
public interface MsgMobileMapper extends BaseMapper<MsgMobile> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param msgMobile
	 * @return
	 */
	List<MsgMobileVO> selectMsgMobilePage(IPage page, MsgMobileVO msgMobile);

}
