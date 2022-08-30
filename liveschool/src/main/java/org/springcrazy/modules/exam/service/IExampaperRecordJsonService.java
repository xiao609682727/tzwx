
package org.springcrazy.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.exam.entity.ExampaperRecordJson;
import org.springcrazy.modules.exam.vo.ExampaperRecordJsonVO;

/**
 *  服务类
 *
 * @author TongZhou
 * @since 2021-01-05
 */
public interface IExampaperRecordJsonService extends IService<ExampaperRecordJson> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param exampaperRecordJson
	 * @return
	 */
	IPage<ExampaperRecordJsonVO> selectExampaperRecordJsonPage(IPage<ExampaperRecordJsonVO> page, ExampaperRecordJsonVO exampaperRecordJson);

}
