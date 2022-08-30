
package org.springcrazy.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考试记录表实体类
 *
 * @author TongZhou
 * @since 2020-12-02
 */
@Data
@TableName("exam_exampaper_record")
@ApiModel(value = "ExampaperRecord对象", description = "考试记录表")
public class ExampaperRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 用户得分
     */
    @ApiModelProperty(value = "用户得分")
    private BigDecimal userScore;
    /**
     * 正确率
     */
    @ApiModelProperty(value = "正确率")
    private Float accuracy;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 试卷id
     */
    @ApiModelProperty(value = "试卷id")
    private Integer epId;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private LocalDateTime addTime;
    /**
     * 该用户考试所用时间单位是秒
     */
    @ApiModelProperty(value = "该用户考试所用时间单位是秒")
    private Integer testTime;
  /**
   * 考试总分
   */
  @ApiModelProperty(value = "考试总分")
  private BigDecimal qstScore;
    /**
     * 考试题数
     */
    @ApiModelProperty(value = "考试题数")
    private Integer qstCount;
    /**
     * 已做题数
     */
    @ApiModelProperty(value = "已做题数")
    @TableField("doneCount")
  private Integer doneCount;
    /**
     * 正确题数
     */
    @ApiModelProperty(value = "正确题数")
    private Integer correctNum;
    /**
     * 专业id
     */
    @ApiModelProperty(value = "专业id")
    private Integer subjectId;
  /**
   * 试卷类型
   */
  @ApiModelProperty(value = "试卷类型")
  private Integer examType;
    /**
     * 1随机2系统生成试卷
     */
    @ApiModelProperty(value = "1随机2系统生成试卷")
    private Integer type;
    /**
     * 0为默认完成，1为未考完
     */
    @ApiModelProperty(value = "0为默认完成，1为未考完")
    private Integer status;
    /**
     * 试卷名字
     */
    @ApiModelProperty(value = "试卷名字")
    private String paperName;
    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "最后更新时间")
    private LocalDateTime updateTime;
    /**
     * 是否阅卷 只要交卷false 只要阅卷true
     */
    @ApiModelProperty(value = "是否阅卷 只要交卷false 只要阅卷true")
    private String readStatus;


}
