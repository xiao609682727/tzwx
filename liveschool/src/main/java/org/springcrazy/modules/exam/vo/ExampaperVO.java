
package org.springcrazy.modules.exam.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.Exampaper;
import org.springcrazy.modules.exam.entity.PaperMiddle;

import java.util.List;

/**
 * 考试试卷表视图实体类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExampaperVO对象", description = "考试试卷表")
public class ExampaperVO extends Exampaper {
	private static final long serialVersionUID = 1L;

	private String typeName;
	private String subjectName;
	private Integer userId;
	/**
	 * 该用户考试所用时间单位是秒
	 */
	private Integer testTime;
	private String maxScore;
	private String minScore;

	private List<PaperMiddle> list;
}
