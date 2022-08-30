
package org.springcrazy.modules.msg.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.msg.entity.MsgReceive;
import org.springcrazy.modules.msg.mapper.MsgReceiveMapper;
import org.springcrazy.modules.msg.service.IMsgReceiveService;
import org.springcrazy.modules.msg.vo.MsgReceiveVO;
import org.springframework.stereotype.Service;

/**
 * 站内信 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Service
public class MsgReceiveServiceImpl extends ServiceImpl<MsgReceiveMapper, MsgReceive> implements IMsgReceiveService {

	@Override
	public IPage<MsgReceiveVO> selectMsgReceivePage(IPage<MsgReceiveVO> page, MsgReceiveVO msgReceive) {
		return page.setRecords(baseMapper.selectMsgReceivePage(page, msgReceive));
	}

}
