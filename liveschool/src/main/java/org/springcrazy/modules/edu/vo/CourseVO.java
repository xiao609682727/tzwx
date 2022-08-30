
package org.springcrazy.modules.edu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.CourseKpoint;
import org.springcrazy.modules.edu.entity.Subject;
import org.springcrazy.modules.user.entity.Teacher;

import java.util.List;

/**
 * 课程表视图实体类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CourseVO对象", description = "课程表")
public class CourseVO extends Course {
	private static final long serialVersionUID = 1L;

	List list;
	@ApiModelProperty(value = "课程老师")
	Teacher teacher;

	Subject subSubject;
	Subject subject;
	Integer studyCount;
	/**
	 * 用户id用来区分当前用户是否是登录状态
	 */
	Integer userId;
	String authTime;
	@ApiModelProperty(value = "直播课程当前时间距离下一场最近的直播")
	CourseKpoint nextLiveCourseKpoint;


	String faceTeachingSubjectAddress;


	String teacherSubjectName;
}
