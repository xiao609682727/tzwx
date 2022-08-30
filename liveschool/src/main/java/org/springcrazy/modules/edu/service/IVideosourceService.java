
package org.springcrazy.modules.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.core.mp.base.BaseService;
import org.springcrazy.modules.edu.entity.Videosource;
import org.springcrazy.modules.edu.vo.VideosourceVO;

/**
 * 录播视频表 服务类
 *
 * @author TongZhou
 * @since 2020-06-29
 */
public interface IVideosourceService extends BaseService<Videosource> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param videosource
	 * @return
	 */
	IPage<VideosourceVO> selectVideosourcePage(IPage<VideosourceVO> page, VideosourceVO videosource);

	void syncVideoSource();

	public String convert(String status);

	void updateVideoSourseStatus();

}
