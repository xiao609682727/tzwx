
package org.springcrazy.modules.quartz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.quartz.CronExpression;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.mp.base.BaseServiceImpl;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.quartz.entity.QuartzJob;
import org.springcrazy.modules.quartz.mapper.QuartzJobMapper;
import org.springcrazy.modules.quartz.service.IQuartzJobService;
import org.springcrazy.modules.quartz.utils.QuartzManage;
import org.springcrazy.modules.quartz.vo.QuartzJobVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务 服务实现类
 *
 * @author TongZhou
 * @since 2020-05-11
 */
@Service
@RequiredArgsConstructor
public class QuartzJobServiceImpl extends BaseServiceImpl<QuartzJobMapper, QuartzJob> implements IQuartzJobService {

	private final QuartzManage quartzManage;

	@Override
	public IPage<QuartzJobVO> selectQuartzJobPage(IPage<QuartzJobVO> page, QuartzJobVO quartzJob) {
		return page.setRecords(baseMapper.selectQuartzJobPage(page, quartzJob));
	}
	@Override
	public void execution(QuartzJob quartzJob) {
		quartzManage.runJobNow(quartzJob);
	}

	@Override
	public void updateIsPause(Integer id) {
		QuartzJob quartzJob = baseMapper.selectById(id);
		if (Func.equals(quartzJob.getIsPause(),QuartzJob.PAUSE)) {
			quartzManage.resumeJob(quartzJob);
			quartzJob.setIsPause(QuartzJob.START);
		} else {
			quartzManage.pauseJob(quartzJob);
			quartzJob.setIsPause(QuartzJob.PAUSE);
		}
		this.updateById(quartzJob);
	}

	@Override
	public void delete(List<Integer> ids) {
		for (Integer id : ids) {
			QuartzJob quartzJob = baseMapper.selectById(id);
			quartzManage.deleteJob(quartzJob);
		}
		baseMapper.deleteBatchIds(ids);
	}

	@Override
	public QuartzJob create(QuartzJob resources) {
		if (!CronExpression.isValidExpression(resources.getCronExpression())){
			throw new ServiceException("cron表达式格式错误");
		}
		this.save(resources);
		quartzManage.addJob(resources);
		return resources;
	}

	@Override
	public void update(QuartzJob resources) {
		if (!CronExpression.isValidExpression(resources.getCronExpression())){
			throw new ServiceException("cron表达式格式错误");
		}
		this.updateById(resources);
		quartzManage.updateJobCron(resources);
	}
}
