
package org.springcrazy.modules.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.msg.entity.MsgRange;
import org.springcrazy.modules.msg.vo.MsgRangeVO;

import java.util.List;

/**
 * 消息范围 一对多中间表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-18
 */
public interface MsgRangeMapper extends BaseMapper<MsgRange> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param msgRange
	 * @return
	 */
	List<MsgRangeVO> selectMsgRangePage(IPage page, MsgRangeVO msgRange);

}
