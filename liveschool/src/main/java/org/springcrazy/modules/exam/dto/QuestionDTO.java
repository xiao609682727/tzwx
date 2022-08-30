
package org.springcrazy.modules.exam.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.Question;

/**
 * 考试试题表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionDTO extends Question {
	private static final long serialVersionUID = 1L;

}
