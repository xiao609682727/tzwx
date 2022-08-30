
package org.springcrazy.modules.edu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.modules.edu.entity.LiveRoom;
import org.springcrazy.modules.edu.mapper.LiveRoomMapper;
import org.springcrazy.modules.edu.service.ILiveRoomService;
import org.springcrazy.modules.edu.vo.LiveRoomVO;
import org.springframework.stereotype.Service;

/**
 * 直播房间 服务实现类
 *
 * @author TongZhou
 * @since 2020-11-09
 */
@Service
public class LiveRoomServiceImpl extends BaseServiceImpl<LiveRoomMapper, LiveRoom> implements ILiveRoomService {

	@Override
	public IPage<LiveRoomVO> selectLiveRoomPage(IPage<LiveRoomVO> page, LiveRoomVO liveRoom) {
		return page.setRecords(baseMapper.selectLiveRoomPage(page, liveRoom));
	}

}
