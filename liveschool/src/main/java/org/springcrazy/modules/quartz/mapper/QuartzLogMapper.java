
package org.springcrazy.modules.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.quartz.entity.QuartzLog;
import org.springcrazy.modules.quartz.vo.QuartzLogVO;

import java.util.List;

/**
 * 定时任务日志 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-05-11
 */
public interface QuartzLogMapper extends BaseMapper<QuartzLog> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param quartzLog
	 * @return
	 */
	List<QuartzLogVO> selectQuartzLogPage(IPage page, QuartzLogVO quartzLog);

}
