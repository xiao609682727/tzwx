
package org.springcrazy.modules.edu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.edu.entity.LivePlayback;
import org.springcrazy.modules.edu.mapper.LivePlaybackMapper;
import org.springcrazy.modules.edu.service.ILivePlaybackService;
import org.springcrazy.modules.edu.vo.LivePlaybackVO;
import org.springframework.stereotype.Service;

/**
 * 直播回放管理 服务实现类
 *
 * @author TongZhou
 * @since 2020-11-10
 */
@Service
public class LivePlaybackServiceImpl extends ServiceImpl<LivePlaybackMapper, LivePlayback> implements ILivePlaybackService {

	@Override
	public IPage<LivePlaybackVO> selectLivePlaybackPage(IPage<LivePlaybackVO> page, LivePlaybackVO livePlayback) {
		return page.setRecords(baseMapper.selectLivePlaybackPage(page, livePlayback));
	}

}
