
package org.springcrazy.modules.edu.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.edu.entity.CourseStudyhistory;

/**
 * 课程播放记录(学习记录)数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseStudyhistoryDTO extends CourseStudyhistory {
	private static final long serialVersionUID = 1L;

}
