
package org.springcrazy.modules.user.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.user.entity.Student;

/**
 * 学员表视图实体类
 *
 * @author TongZhou
 * @since 2020-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StudentVO对象", description = "学员表")
public class StudentVO extends Student {
	private static final long serialVersionUID = 1L;


}
