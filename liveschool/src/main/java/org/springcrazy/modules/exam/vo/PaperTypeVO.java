
package org.springcrazy.modules.exam.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.PaperType;

/**
 * 试卷类型表视图实体类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PaperTypeVO对象", description = "试卷类型表")
public class PaperTypeVO extends PaperType {
	private static final long serialVersionUID = 1L;

}
