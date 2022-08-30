
package org.springcrazy.modules.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.exam.entity.ExampaperRecordJson;
import org.springcrazy.modules.exam.vo.ExampaperRecordJsonVO;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author TongZhou
 * @since 2021-01-05
 */
public interface ExampaperRecordJsonMapper extends BaseMapper<ExampaperRecordJson> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param exampaperRecordJson
	 * @return
	 */
	List<ExampaperRecordJsonVO> selectExampaperRecordJsonPage(IPage page, ExampaperRecordJsonVO exampaperRecordJson);

}
