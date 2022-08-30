
package org.springcrazy.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.exam.entity.ExampaperRecordJson;
import org.springcrazy.modules.exam.mapper.ExampaperRecordJsonMapper;
import org.springcrazy.modules.exam.service.IExampaperRecordJsonService;
import org.springcrazy.modules.exam.vo.ExampaperRecordJsonVO;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 *
 * @author TongZhou
 * @since 2021-01-05
 */
@Service
public class ExampaperRecordJsonServiceImpl extends ServiceImpl<ExampaperRecordJsonMapper, ExampaperRecordJson> implements IExampaperRecordJsonService {

	@Override
	public IPage<ExampaperRecordJsonVO> selectExampaperRecordJsonPage(IPage<ExampaperRecordJsonVO> page, ExampaperRecordJsonVO exampaperRecordJson) {
		return page.setRecords(baseMapper.selectExampaperRecordJsonPage(page, exampaperRecordJson));
	}

}
