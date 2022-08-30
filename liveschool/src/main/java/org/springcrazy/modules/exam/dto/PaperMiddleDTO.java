
package org.springcrazy.modules.exam.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.PaperMiddle;

/**
 * 大题（试卷试题类型中间表）数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PaperMiddleDTO extends PaperMiddle {
	private static final long serialVersionUID = 1L;


}
