
package org.springcrazy.modules.quartz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.quartz.entity.QuartzLog;
import org.springcrazy.modules.quartz.mapper.QuartzLogMapper;
import org.springcrazy.modules.quartz.service.IQuartzLogService;
import org.springcrazy.modules.quartz.vo.QuartzLogVO;
import org.springframework.stereotype.Service;

/**
 * 定时任务日志 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-11
 */
@Service
public class QuartzLogServiceImpl extends ServiceImpl<QuartzLogMapper, QuartzLog> implements IQuartzLogService {

	@Override
	public IPage<QuartzLogVO> selectQuartzLogPage(IPage<QuartzLogVO> page, QuartzLogVO quartzLog) {
		return page.setRecords(baseMapper.selectQuartzLogPage(page, quartzLog));
	}

}
