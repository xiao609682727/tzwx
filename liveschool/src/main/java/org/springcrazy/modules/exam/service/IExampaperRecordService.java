
package org.springcrazy.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.exam.entity.ExampaperRecord;
import org.springcrazy.modules.exam.entity.PaperMiddle;
import org.springcrazy.modules.exam.vo.ExampaperRecordVO;
import org.springcrazy.modules.exam.vo.ExampaperVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 考试记录表 服务类
 *
 * @author TongZhou
 * @since 2020-12-02
 */
public interface IExampaperRecordService extends IService<ExampaperRecord> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param exampaperRecord
	 * @return
	 */
	IPage<ExampaperRecordVO> selectExampaperRecordPage(IPage<ExampaperRecordVO> page, ExampaperRecordVO exampaperRecord);

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


	List<PaperMiddle> queryQuestionRecord(String paperId);

	ExampaperRecord addExampaperRecordSave(ExampaperVO exampaperVO);

	Map<String, Object> getQuesData(Map<String,Object> param);
	/**
	 * 获取导出考试记录数据
	 */
	void exportExampaperRecord(HttpServletResponse response, ExampaperRecordVO exampaperRecord);
}
