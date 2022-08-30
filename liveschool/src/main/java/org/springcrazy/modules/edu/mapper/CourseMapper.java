
package org.springcrazy.modules.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.vo.CourseVO;

import java.util.List;

/**
 * 课程表 Mapper 接口
 *
 * @author TongZhou
 * @since 2020-04-25
 */
public interface CourseMapper extends BaseMapper<Course> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param course
	 * @return
	 */
	List<CourseVO> selectCoursePage(IPage page, CourseVO course);

	Course getCourseById(@Param("courseId") int courseId);
}
