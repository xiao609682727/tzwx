
package org.springcrazy.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.exam.entity.QuestionRecord;
import org.springcrazy.modules.exam.mapper.QuestionRecordMapper;
import org.springcrazy.modules.exam.service.IQuestionRecordService;
import org.springcrazy.modules.exam.vo.QuestionRecordVO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 考试试题记录表 服务实现类
 *
 * @author TongZhou
 * @since 2021-01-05
 */
@Service
public class QuestionRecordServiceImpl extends ServiceImpl<QuestionRecordMapper, QuestionRecord> implements IQuestionRecordService {

	@Override
	public IPage<Map<String,Object>> selectQuestionRecordPage(IPage<Map<String,Object>> page, QuestionRecordVO questionRecord) {
		return page.setRecords(baseMapper.selectQuestionRecordPage(page, questionRecord));
	}

}
