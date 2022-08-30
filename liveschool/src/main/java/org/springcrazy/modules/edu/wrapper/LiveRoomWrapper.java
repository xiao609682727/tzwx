
package org.springcrazy.modules.edu.wrapper;

import com.google.common.collect.Maps;
import org.springcrazy.common.tool.BajiayunUtil;
import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.modules.edu.entity.LiveRoom;
import org.springcrazy.modules.edu.vo.LiveRoomVO;

import java.util.Map;

/**
 * 直播房间包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-11-09
 */
public class LiveRoomWrapper extends BaseEntityWrapper<LiveRoom, LiveRoomVO>  {

    public static LiveRoomWrapper build() {
        return new LiveRoomWrapper();
    }

	@Override
	public LiveRoomVO entityVO(LiveRoom liveRoom) {
		LiveRoomVO liveRoomVO = BeanUtil.copy(liveRoom, LiveRoomVO.class);
		Map<String,String> userMap = Maps.newHashMap();
		userMap.put("userId", SecureUtil.getUser().getUserId()+"");
		userMap.put("userName",SecureUtil.getUser().getUserName());
		String teacherWebUrl = BajiayunUtil.roomUrl(liveRoom.getClassroomId(),"WEB","teacher",userMap);
		String teacherAppUrl = BajiayunUtil.roomUrl(liveRoom.getClassroomId(),"APP","teacher",userMap);
		String adminWebUrl = BajiayunUtil.roomUrl(liveRoom.getClassroomId(),"WEB","admin",userMap);
		String adminAppUrl = BajiayunUtil.roomUrl(liveRoom.getClassroomId(),"APP","admin",userMap);
		Map<String,Object> params = Maps.newHashMap();
		params.put("teacherWebUrl",teacherWebUrl);
		params.put("teacherAppUrl",teacherAppUrl);
		params.put("adminWebUrl",adminWebUrl);
		params.put("adminAppUrl",adminAppUrl);
		liveRoomVO.setParams(params);
		return liveRoomVO;
	}

}
