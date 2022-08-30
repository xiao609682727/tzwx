
package org.springcrazy.modules.exam.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.ErrorQuestion;

/**
 * 错题记录表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorQuestionDTO extends ErrorQuestion {
	private static final long serialVersionUID = 1L;

}
