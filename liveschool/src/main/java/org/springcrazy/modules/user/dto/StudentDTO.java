
package org.springcrazy.modules.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.user.entity.Student;

/**
 * 学员表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentDTO extends Student {
	private static final long serialVersionUID = 1L;

}
