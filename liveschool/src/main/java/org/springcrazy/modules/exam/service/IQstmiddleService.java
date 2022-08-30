
package org.springcrazy.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.exam.entity.Qstmiddle;
import org.springcrazy.modules.exam.vo.QstmiddleVO;

import java.util.List;

/**
 * 考试试卷表 服务类
 *
 * @author TongZhou
 * @since 2020-12-15
 */
public interface IQstmiddleService extends IService<Qstmiddle> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param qstmiddle
	 * @return
	 */
	IPage<QstmiddleVO> selectQstmiddlePage(IPage<QstmiddleVO> page, QstmiddleVO qstmiddle);

	List<QstmiddleVO> selectQstmiddle(String id);

}
