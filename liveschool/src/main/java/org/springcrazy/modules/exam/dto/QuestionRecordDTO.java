
package org.springcrazy.modules.exam.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.QuestionRecord;

/**
 * 考试试题记录表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2021-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionRecordDTO extends QuestionRecord {
	private static final long serialVersionUID = 1L;

}
