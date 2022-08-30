
package org.springcrazy.modules.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.core.mp.base.BaseService;
import org.springcrazy.modules.quartz.entity.QuartzJob;
import org.springcrazy.modules.quartz.vo.QuartzJobVO;

import java.util.List;

/**
 * 定时任务 服务类
 *
 * @author TongZhou
 * @since 2020-05-11
 */
public interface IQuartzJobService extends BaseService<QuartzJob> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param quartzJob
	 * @return
	 */
	IPage<QuartzJobVO> selectQuartzJobPage(IPage<QuartzJobVO> page, QuartzJobVO quartzJob);

	/**
	 * 立即执行定时任务
	 * @param quartzJob /
	 */
	void execution(QuartzJob quartzJob);

	/**
	 * 更改定时任务状态
	 * @param id /
	 */
	void updateIsPause(Integer id);

	/**
	 * 删除任务
	 * @param ids /
	 */
	void delete(List<Integer> ids);

	/**
	 * 创建
	 * @param resources /
	 * @return /
	 */
	QuartzJob create(QuartzJob resources);

	/**
	 * 编辑
	 * @param resources /
	 */
	void update(QuartzJob resources);

}
