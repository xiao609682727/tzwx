
package org.springcrazy.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springcrazy.modules.exam.vo.QstmiddleVO;

import java.io.Serializable;
import java.util.List;

/**
 * 大题（试卷试题类型中间表）实体类
 *
 * @author TongZhou
 * @since 2020-12-15
 */
@Data
@TableName("exam_paper_middle")
@ApiModel(value = "PaperMiddle对象", description = "大题（试卷试题类型中间表）")
public class PaperMiddle implements Serializable {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 外键关联试卷
     */
    @ApiModelProperty(value = "外键关联试卷")
    @TableField("paperId")
  private Integer paperId;
    /**
     * 试题的类型
     */
    @ApiModelProperty(value = "试题的类型")
    private Integer type;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer num;
    /**
     * 每题的分数
     */
    @ApiModelProperty(value = "每题的分数")
    private Double score;
    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String title;
    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private Integer sort;
    /**
     * 试题难度
     */
    @ApiModelProperty(value = "试题难度")
    @TableField("qstLevel")
  private Integer qstLevel;
    /**
     * 自动抽题专业Id
     */
    @ApiModelProperty(value = "自动抽题专业Id")
    @TableField("subjectId")
  private Integer subjectId;
    /**
     * 自动抽题考点Id
     */
    @ApiModelProperty(value = "自动抽题考点Id")
    @TableField("pointId")
  private String pointId;
    /**
     * 是否得分 ON 是 OFF否
     */
    @ApiModelProperty(value = "是否得分 ON 是 OFF否")
    private String lose;
    /**
     * 漏选得分
     */
    @ApiModelProperty(value = "漏选得分")
    @TableField("loseScore")
  private Double loseScore;

    @TableField(exist = false)
  public List<QstmiddleVO> questionArr;
}
