
package org.springcrazy.modules.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.exam.entity.Qstmiddle;
import org.springcrazy.modules.exam.vo.QstmiddleVO;

import java.util.List;

/**
 * 考试试卷表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-12-15
 */
public interface QstmiddleMapper extends BaseMapper<Qstmiddle> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param qstmiddle
	 * @return
	 */
	List<QstmiddleVO> selectQstmiddlePage(IPage page, QstmiddleVO qstmiddle);

	List<QstmiddleVO> selectQstmiddle(String id);

}
