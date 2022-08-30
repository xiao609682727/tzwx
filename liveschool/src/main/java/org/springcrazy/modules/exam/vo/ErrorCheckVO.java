
package org.springcrazy.modules.exam.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.ErrorCheck;

/**
 * 试题纠错表视图实体类
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ErrorCheckVO对象", description = "试题纠错表")
public class ErrorCheckVO extends ErrorCheck {
	private static final long serialVersionUID = 1L;

}
