
package org.springcrazy.modules.msg.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.msg.entity.MsgRange;
import org.springcrazy.modules.msg.mapper.MsgRangeMapper;
import org.springcrazy.modules.msg.service.IMsgRangeService;
import org.springcrazy.modules.msg.vo.MsgRangeVO;
import org.springframework.stereotype.Service;

/**
 * 消息范围 一对多中间表 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Service
public class MsgRangeServiceImpl extends ServiceImpl<MsgRangeMapper, MsgRange> implements IMsgRangeService {

	@Override
	public IPage<MsgRangeVO> selectMsgRangePage(IPage<MsgRangeVO> page, MsgRangeVO msgRange) {
		return page.setRecords(baseMapper.selectMsgRangePage(page, msgRange));
	}

}
