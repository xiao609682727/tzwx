
package org.springcrazy.modules.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.core.mp.base.BaseService;
import org.springcrazy.modules.edu.entity.LiveRoom;
import org.springcrazy.modules.edu.vo.LiveRoomVO;

/**
 * 直播房间 服务类
 *
 * @author TongZhou
 * @since 2020-11-09
 */
public interface ILiveRoomService extends BaseService<LiveRoom> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param liveRoom
	 * @return
	 */
	IPage<LiveRoomVO> selectLiveRoomPage(IPage<LiveRoomVO> page, LiveRoomVO liveRoom);

}
