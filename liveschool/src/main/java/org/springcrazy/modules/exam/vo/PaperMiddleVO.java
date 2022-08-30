
package org.springcrazy.modules.exam.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.PaperMiddle;

/**
 * 大题（试卷试题类型中间表）视图实体类
 *
 * @author TongZhou
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PaperMiddleVO对象", description = "大题（试卷试题类型中间表）")
public class PaperMiddleVO extends PaperMiddle {
	private static final long serialVersionUID = 1L;

}
