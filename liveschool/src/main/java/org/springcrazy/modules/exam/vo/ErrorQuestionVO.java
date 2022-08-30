
package org.springcrazy.modules.exam.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.ErrorQuestion;

/**
 * 错题记录表视图实体类
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ErrorQuestionVO对象", description = "错题记录表")
public class ErrorQuestionVO extends ErrorQuestion {
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
	@TableField("isAsr")
	private String isAsr;
	/**
	 * 试题类型(1单选题2多选题3判断题4填空题)
	 */
	@ApiModelProperty(value = "试题类型(1单选题2多选题3判断题4填空题)")
	@TableField("qstType")
	private Integer qstType;


	/**
	 * 选择题选项及选项内容，json格式
	 */
	@ApiModelProperty(value = "选择题选项及选项内容，json格式")
	private String optionList;

	/**
	 * 试题解析
	 */
	@ApiModelProperty(value = "试题解析")
	private String qstAnalyze;

	/**
	 * 是否收藏
	 */
	@ApiModelProperty(value = "是否收藏")
	private String favFlag;

}
