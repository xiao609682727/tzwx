
package org.springcrazy.modules.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.edu.entity.Videosource;
import org.springcrazy.modules.edu.vo.VideosourceVO;

import java.util.List;

/**
 * 录播视频表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-06-29
 */
public interface VideosourceMapper extends BaseMapper<Videosource> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param videosource
	 * @return
	 */
	List<VideosourceVO> selectVideosourcePage(IPage page, VideosourceVO videosource);

}
