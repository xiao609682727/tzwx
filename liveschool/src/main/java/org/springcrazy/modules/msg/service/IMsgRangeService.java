
package org.springcrazy.modules.msg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.msg.entity.MsgRange;
import org.springcrazy.modules.msg.vo.MsgRangeVO;

/**
 * 消息范围 一对多中间表 服务类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
public interface IMsgRangeService extends IService<MsgRange> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param msgRange
	 * @return
	 */
	IPage<MsgRangeVO> selectMsgRangePage(IPage<MsgRangeVO> page, MsgRangeVO msgRange);

}
