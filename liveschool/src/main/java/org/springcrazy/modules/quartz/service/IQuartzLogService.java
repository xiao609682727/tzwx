
package org.springcrazy.modules.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.quartz.entity.QuartzLog;
import org.springcrazy.modules.quartz.vo.QuartzLogVO;

/**
 * 定时任务日志 服务类
 *
 * @author TongZhou
 * @since 2020-05-11
 */
public interface IQuartzLogService extends IService<QuartzLog> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param quartzLog
	 * @return
	 */
	IPage<QuartzLogVO> selectQuartzLogPage(IPage<QuartzLogVO> page, QuartzLogVO quartzLog);

}
