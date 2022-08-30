
package org.springcrazy.modules.exam.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.exam.entity.QuestionRecord;
import org.springcrazy.modules.exam.vo.QuestionRecordVO;

import java.util.List;
import java.util.Map;

/**
 * 考试试题记录表 Mapper 接口
 *
 * @author TongZhou
 * @since 2021-01-05
 */
public interface QuestionRecordMapper extends BaseMapper<QuestionRecord> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param questionRecord
	 * @return
	 */
	@SqlParser(filter = true)
	List<Map<String,Object>> selectQuestionRecordPage(IPage page,@Param("questionRecord") QuestionRecordVO questionRecord);

}
