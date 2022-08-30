
package org.springcrazy.modules.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.quartz.entity.QuartzJob;
import org.springcrazy.modules.quartz.vo.QuartzJobVO;

import java.util.List;

/**
 * 定时任务 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-11
 */
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param quartzJob
	 * @return
	 */
	List<QuartzJobVO> selectQuartzJobPage(IPage page, QuartzJobVO quartzJob);

}
