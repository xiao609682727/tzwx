
package org.springcrazy.modules.coursecard.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springcrazy.modules.coursecard.entity.CardCourse;
import org.springcrazy.modules.coursecard.vo.CardCourseVO;

import java.util.List;

/**
 * 课程卡课程表 服务类
 *
 * @author TongZhou
 * @since 2021-04-02
 */
public interface ICardCourseService extends IService<CardCourse> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param cardCourse
	 * @return
	 */
	IPage<CardCourseVO> selectCardCoursePage(IPage<CardCourseVO> page, CardCourseVO cardCourse);
	/**
	 * 查看课程卡关联的课程信息
	 */
	List<CardCourseVO> queryCourseCardCourseList(CardCourse cardCourse);
}
