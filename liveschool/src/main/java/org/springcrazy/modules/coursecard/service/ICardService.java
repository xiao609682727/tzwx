
package org.springcrazy.modules.coursecard.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.coursecard.entity.Card;
import org.springcrazy.modules.coursecard.entity.CardCode;
import org.springcrazy.modules.coursecard.entity.CardCourse;
import org.springcrazy.modules.coursecard.vo.CardCourseVO;
import org.springcrazy.modules.coursecard.vo.CardVO;

import java.util.List;

/**
 * 课程卡主表 服务类
 *
 * @author TongZhou
 * @since 2021-03-31
 */
public interface ICardService extends IService<Card> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param card
	 * @return
	 */
	IPage<CardVO> selectCardPage(IPage<CardVO> page, CardVO card);
	/**
	 * 课程卡添加
	 */
	void addCard(Card card,String courseIds);
	/**
	 * 课程卡更新
	 */
	void updateCard(Card card,String courseIds);
	/**
	 * 查看课程卡关联的课程信息
	 */
	List<CardCourseVO> queryCourseCardCourseList(CardCourse cardCourse);
	/**
	 * 课程卡激活方法
	 */
	String queryCourseCardCourseActivation(CardCode cardCode);
}
