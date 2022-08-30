
package org.springcrazy.modules.exam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.Qstmiddle;

import java.time.LocalDateTime;

/**
 * 考试试卷表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QstmiddleDTO extends Qstmiddle {
	private static final long serialVersionUID = 1L;


	/**
	 * 试题题干
	 */
	@ApiModelProperty(value = "试题题干")
	private String qstContent;
	/**
	 * 正确选项/答案
	 */
	@ApiModelProperty(value = "正确选项/答案")
	private String isAsr;
	/**
	 * 试题类型(1单选题2多选题3判断题4填空题)
	 */
	@ApiModelProperty(value = "试题类型(1单选题2多选题3判断题4填空题)")
	private Integer qstType;
	/**
	 * 试题难度(1简单2普通3困难)
	 */
	@ApiModelProperty(value = "试题难度(1简单2普通3困难)")
	private Integer level;
	/**
	 * 试题作者
	 */
	@ApiModelProperty(value = "试题作者")
	private String author;
	/**
	 * 试题解析
	 */
	@ApiModelProperty(value = "试题解析")
	private String qstAnalyze;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updatetime;
	/**
	 * 专业id
	 */
	@ApiModelProperty(value = "专业id")
	private Integer subjectId;
	/**
	 * 1为显示2为删除
	 */
	@ApiModelProperty(value = "1为显示2为删除")
	private Integer status;
	/**
	 * 考点id
	 */
	@ApiModelProperty(value = "考点id")
	private Integer pointId;
	/**
	 * 0为非材料1为材料题 （暂时不用）
	 */
	@ApiModelProperty(value = "0为非材料1为材料题 （暂时不用）")
	private Integer complexFalg;
	/**
	 * 做过的次数
	 */
	@ApiModelProperty(value = "做过的次数")
	private Integer time;
	/**
	 * 该试题被做正确过多少道
	 */
	@ApiModelProperty(value = "该试题被做正确过多少道")
	private Integer rightTime;
	/**
	 * 该试题被做错过多少次
	 */
	@ApiModelProperty(value = "该试题被做错过多少次")
	private Integer errorTime;
	/**
	 * 正确率
	 */
	@ApiModelProperty(value = "正确率")
	private Float accuracy;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer sort;
	/**
	 * 填空题类型 1有序 2无序
	 */
	@ApiModelProperty(value = "填空题类型 1有序 2无序")
	private Integer vacancyType;
	/**
	 * 选择题选项及选项内容，json格式
	 */
	@ApiModelProperty(value = "选择题选项及选项内容，json格式")
	private String optionList;
}
