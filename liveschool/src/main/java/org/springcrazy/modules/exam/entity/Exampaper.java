
package org.springcrazy.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考试试卷表实体类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@Data
@TableName("exam_exampaper")
@ApiModel(value = "Exampaper对象", description = "考试试卷表")
public class Exampaper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 专业分类id
     */
    @ApiModelProperty(value = "专业分类id")
    @TableField("subjectId")
  private Integer subjectId;
    /**
     * 考试名称
     */
    @ApiModelProperty(value = "考试名称")
    private String name;
    /**
     * 试卷描述
     */
    @ApiModelProperty(value = "试卷描述")
    private String info;
    /**
     * 限制答题的时间 单位分钟
     */
    @ApiModelProperty(value = "限制答题的时间 单位分钟")
    @TableField("replyTime")
  private Integer replyTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "addTime",fill = FieldFill.INSERT)
  private LocalDateTime addTime;
    /**
     * 状态normal正常(上架)  frozen冻结(下架)  delete删除
     */
    @ApiModelProperty(value = "状态normal正常(上架)  frozen冻结(下架)  delete删除")
    private String status;
    /**
     * 试卷难度 simple简单  commonly中等 difficulty困难
     */
    @ApiModelProperty(value = "试卷难度 simple简单  commonly中等 difficulty困难")
    private String level;
    /**
     * 参加考试人数
     */
    @ApiModelProperty(value = "参加考试人数")
    @TableField("joinNum")
  private Integer joinNum;
    /**
     * 参加考试的平均分数
     */
    @ApiModelProperty(value = "参加考试的平均分数")
    @TableField("avgScore")
  private BigDecimal avgScore;
    /**
     * 试卷类型
     */
    @ApiModelProperty(value = "试卷类型")
    private Integer type;
    /**
     * 试题总数
     */
    @ApiModelProperty(value = "试题总数")
    @TableField("qstCount")
  private Integer qstCount;
    /**
     * 最后编辑时间
     */
    @ApiModelProperty(value = "最后编辑时间")
    @TableField(value = "updateTime",fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;
    /**
     * 试卷总分
     */
    @ApiModelProperty(value = "试卷总分")
    private BigDecimal score;
    /**
     * 及格分数比例（总分的百分之几）
     */
    @ApiModelProperty(value = "及格分数比例（总分的百分之几）")
    @TableField("passRate")
  private Double passRate;
    /**
     * 组卷方式 手动manual 智能automatic 随机random
     */
    @ApiModelProperty(value = "组卷方式 手动manual 智能automatic 随机random")
    @TableField("assemblyMode")
  private String assemblyMode;
    /**
     * 试题类型
     */
    @ApiModelProperty(value = "试题类型")
    @TableField("questionsType")
  private String questionsType;
    /**
     * 及格/完成/通关人数
     */
    @ApiModelProperty(value = "及格/完成/通关人数")
    @TableField("passNum")
  private Integer passNum;
    /**
     * 实际参加考试人数（去重）
     */
    @ApiModelProperty(value = "实际参加考试人数（去重）")
    @TableField("actualJoinNum")
  private Integer actualJoinNum;
    /**
     * 排序值
     */
    @ApiModelProperty(value = "排序值")
    @TableField("sortNum")
  private Integer sortNum;


}
