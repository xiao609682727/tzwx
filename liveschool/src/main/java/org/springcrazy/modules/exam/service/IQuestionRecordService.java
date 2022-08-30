
package org.springcrazy.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.exam.entity.QuestionRecord;
import org.springcrazy.modules.exam.vo.QuestionRecordVO;

import java.util.Map;

/**
 * 考试试题记录表 服务类
 *
 * @author TongZhou
 * @since 2021-01-05
 */
public interface IQuestionRecordService extends IService<QuestionRecord> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param questionRecord
	 * @return
	 */
	IPage<Map<String,Object>> selectQuestionRecordPage(IPage<Map<String,Object>> page, QuestionRecordVO questionRecord);

}
