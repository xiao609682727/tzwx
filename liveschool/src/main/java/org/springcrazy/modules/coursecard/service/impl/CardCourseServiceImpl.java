
package org.springcrazy.modules.coursecard.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springcrazy.modules.coursecard.entity.CardCourse;
import org.springcrazy.modules.coursecard.mapper.CardCourseMapper;
import org.springcrazy.modules.coursecard.service.ICardCourseService;
import org.springcrazy.modules.coursecard.vo.CardCourseVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程卡课程表 服务实现类
 *
 * @author TongZhou
 * @since 2021-04-02
 */
@Service
public class CardCourseServiceImpl extends ServiceImpl<CardCourseMapper, CardCourse> implements ICardCourseService {

	@Override
	public IPage<CardCourseVO> selectCardCoursePage(IPage<CardCourseVO> page, CardCourseVO cardCourse) {
		return page.setRecords(baseMapper.selectCardCoursePage(page, cardCourse));
	}
	/**
	 * 查看课程卡关联的课程信息
	 */
	public List<CardCourseVO> queryCourseCardCourseList(CardCourse cardCourse){
		return baseMapper.queryCourseCardCourseList(cardCourse);
	}
}
