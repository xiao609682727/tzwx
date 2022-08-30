
package org.springcrazy.modules.coursecard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.coursecard.entity.Card;
import org.springcrazy.modules.coursecard.vo.CardVO;

import java.util.List;

/**
 * 课程卡主表 Mapper 接口
 *
 * @author TongZhou
 * @since 2021-03-31
 */
public interface CardMapper extends BaseMapper<Card> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param card
	 * @return
	 */
	List<CardVO> selectCardPage(IPage page, CardVO card);

}
