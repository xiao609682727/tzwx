
package org.springcrazy.modules.edu.wrapper;

import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.service.ICourseService;
import org.springcrazy.modules.edu.vo.CourseVO;

/**
 * 包装类,返回视图层所需的字段

 */
public class CourseWrapper extends BaseEntityWrapper<Course, CourseVO> {

	private static ICourseService courseService;


	static {
		courseService = SpringUtil.getBean(ICourseService.class);
	}

	public static CourseWrapper build() {
		return new CourseWrapper();
	}

	@Override
	public CourseVO entityVO(Course course) {
		CourseVO courseVO = BeanUtil.copy(course, CourseVO.class);
		return courseVO;
	}



}
