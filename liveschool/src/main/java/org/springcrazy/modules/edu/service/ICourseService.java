
package org.springcrazy.modules.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.Orders;
import org.springcrazy.modules.edu.vo.CourseVO;
import org.springcrazy.modules.system.excel.GiveCourseExcel;
import org.springcrazy.modules.user.entity.Student;

import java.util.Date;
import java.util.List;

/**
 * 课程表 服务类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
public interface ICourseService extends IService<Course> {

	/**
	 * 自定义分页
	 */
	IPage<CourseVO> selectCoursePage(IPage<CourseVO> page, CourseVO course);
	/**
	 * 赠送课程
	 */
	void giveCourse(String userIds,String courseIds);
	/**
	 * 开通课程
	 */
	void openCourse(String userIds,String courseIds);
	Date getAuthTime(Course course);

	/**
	 * 导入赠送课程数据
	 * @param list
	 */
    void importGiveCourse(List<GiveCourseExcel> list);
    /**
	 *赠送课程
	 * */
	void courseOrder(Student student, List<Course> courseList);
	/**
	 *赠送课程
	 * */
	Orders addCourseOrder(Student student, List<Course> courseList, String payType);
	/**
	 * 根据课程id查询课程信息
	 * */
	Course getCourseById(@Param("courseId") int courseId);
}
