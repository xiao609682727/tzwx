
package org.springcrazy.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考试试题记录表实体类
 *
 * @author TongZhou
 * @since 2021-01-05
 */
@Data
@TableName("exam_question_record")
@ApiModel(value = "QuestionRecord对象", description = "考试试题记录表")
public class QuestionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 试卷id
     */
    @ApiModelProperty(value = "试卷id")
    private Integer paperId;
    /**
     * 试题id
     */
    @ApiModelProperty(value = "试题id")
    private Integer qstId;
    /**
     * 用户答案
     */
    @ApiModelProperty(value = "用户答案")
    private String useranswer;
    /**
     * 0为正确1为错误
     */
    @ApiModelProperty(value = "0为正确1为错误")
    private Integer status;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer cusId;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private LocalDateTime addtime;
    /**
     * 试卷记录表id
     */
    @ApiModelProperty(value = "试卷记录表id")
    private Integer paperRecordId;
    /**
     * papermiddle的关联id
     */
    @ApiModelProperty(value = "papermiddle的关联id")
    private Integer papermiddleId;
    /**
     * 试题类型（冗余）
     */
    @ApiModelProperty(value = "试题类型（冗余）")
    private Integer qstType;
    /**
     * 试卷考点（冗余）
     */
    @ApiModelProperty(value = "试卷考点（冗余）")
    private Integer pointId;
    /**
     * 得分
     */
    @ApiModelProperty(value = "得分")
    private BigDecimal score;
    /**
     * 0未审阅1已审阅
     */
    @ApiModelProperty(value = "0未审阅1已审阅")
    private Integer state;


}
