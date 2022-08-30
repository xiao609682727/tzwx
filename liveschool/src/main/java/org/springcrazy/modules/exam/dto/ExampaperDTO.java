
package org.springcrazy.modules.exam.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.Exampaper;

/**
 * 考试试卷表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExampaperDTO extends Exampaper {
	private static final long serialVersionUID = 1L;

}
