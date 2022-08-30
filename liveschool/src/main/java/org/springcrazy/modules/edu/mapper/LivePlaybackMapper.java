
package org.springcrazy.modules.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.edu.entity.LivePlayback;
import org.springcrazy.modules.edu.vo.LivePlaybackVO;

import java.util.List;

/**
 * 直播回放管理 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-11-10
 */
public interface LivePlaybackMapper extends BaseMapper<LivePlayback> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param livePlayback
	 * @return
	 */
	List<LivePlaybackVO> selectLivePlaybackPage(IPage page, LivePlaybackVO livePlayback);

}
