
package org.springcrazy.modules.edu.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.CourseKpoint;

import java.util.List;

/**
 * 课程表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseDTO extends Course {
	private static final long serialVersionUID = 1L;

	List<CourseKpoint> list;

}
