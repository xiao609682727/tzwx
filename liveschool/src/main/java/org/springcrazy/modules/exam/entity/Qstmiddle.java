
package org.springcrazy.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 考试试卷表实体类
 *
 * @author TongZhou
 * @since 2020-12-15
 */
@Data
@TableName("exam_qstmiddle")
@ApiModel(value = "Qstmiddle对象", description = "考试试卷表")
public class Qstmiddle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 试卷id
     */
    @ApiModelProperty(value = "试卷id")
    @TableField("paperId")
  private Integer paperId;
    /**
     * 试题id
     */
    @ApiModelProperty(value = "试题id")
    @TableField("qstId")
  private Integer qstId;
    /**
     * 试题类型 1单选题 2多选题 3判断题 4综合题 5不定项题 6论述题 7填空题
     */
    @ApiModelProperty(value = "试题类型 1单选题 2多选题 3判断题 4综合题 5不定项题 6论述题 7填空题")
    @TableField("qstType")
  private Integer qstType;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private LocalDateTime addtime;
    /**
     * 组卷试题排序
     */
    @ApiModelProperty(value = "组卷试题排序")
    private Integer sort;
    /**
     * papermiddle的关联id
     */
    @ApiModelProperty(value = "papermiddle的关联id")
    @TableField("papermiddleId")
  private Integer papermiddleId;
    /**
     * 材料题id
     */
    @ApiModelProperty(value = "材料题id")
    @TableField("complexId")
  private Integer complexId;
    /**
     * 考试记录id（随机试卷时用）
     */
    @ApiModelProperty(value = "考试记录id（随机试卷时用）")
    @TableField("paperRecordId")
  private Integer paperRecordId;


}
