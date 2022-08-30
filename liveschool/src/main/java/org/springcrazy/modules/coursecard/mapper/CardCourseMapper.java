
package org.springcrazy.modules.coursecard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springcrazy.modules.coursecard.entity.CardCourse;
import org.springcrazy.modules.coursecard.vo.CardCourseVO;

import java.util.List;

/**
 * 课程卡课程表 Mapper 接口
 *
 * @author TongZhou
 * @since 2021-04-02
 */
public interface CardCourseMapper extends BaseMapper<CardCourse> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param cardCourse
	 * @return
	 */
	List<CardCourseVO> selectCardCoursePage(IPage page, CardCourseVO cardCourse);
	/**
	 * 查看课程卡关联的课程信息
	 */
	List<CardCourseVO> queryCourseCardCourseList(CardCourse cardCourse);
}
