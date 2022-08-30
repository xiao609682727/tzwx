
package org.springcrazy.modules.exam.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.Question;

/**
 * 考试试题表视图实体类
 *
 * @author TongZhou
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "QuestionVO对象", description = "考试试题表")
public class QuestionVO extends Question {
	private static final long serialVersionUID = 1L;



}
