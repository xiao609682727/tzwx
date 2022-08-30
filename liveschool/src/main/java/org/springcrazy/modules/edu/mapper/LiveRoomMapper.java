
package org.springcrazy.modules.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.edu.entity.LiveRoom;
import org.springcrazy.modules.edu.vo.LiveRoomVO;

import java.util.List;

/**
 * 直播房间 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-11-09
 */
public interface LiveRoomMapper extends BaseMapper<LiveRoom> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param liveRoom
	 * @return
	 */
	List<LiveRoomVO> selectLiveRoomPage(IPage page, LiveRoomVO liveRoom);

}
