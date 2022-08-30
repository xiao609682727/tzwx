
package org.springcrazy.modules.exam.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.exam.entity.ExampaperRecord;
import org.springcrazy.modules.exam.vo.ExampaperRecordVO;
import org.springcrazy.modules.system.excel.ExportExampaperRecordExcel;

import java.util.List;
import java.util.Map;

/**
 * 考试记录表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-12-02
 */
public interface ExampaperRecordMapper extends BaseMapper<ExampaperRecord> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param exampaperRecord
	 * @return
	 */
	List<ExampaperRecordVO> selectExampaperRecordPage(IPage page,@Param("exampaperRecord") ExampaperRecordVO exampaperRecord);

	/**
	 * 根据条件获取单个考试记录
	 */
	ExampaperRecordVO getExamPaper(ExampaperRecord exampaperRecord);

	/**
	 * 获取7天内用户考试试卷记录数量
	 */
	int getExamPaperNums(Map<String,Object> param);

	/**
	 * 获取7天内用户做题数量
	 */
	int getQuestionNums(Map<String,Object> param);

	/**
	 * 获取7天内用户做题错误数量
	 */
	int getquestionErrorNums(Map<String,Object> param);

	@SqlParser(filter = true)
	List<Map<String,Object>> getQuesData(Map<String,Object> param);
	/**
	 * 获取导出考试记录数据
	 */
	List<ExportExampaperRecordExcel> exportExampaperRecord(IPage page,@Param("exampaperRecord") ExampaperRecordVO exampaperRecord);
}
