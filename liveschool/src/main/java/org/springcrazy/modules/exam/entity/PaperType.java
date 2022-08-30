
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
 * 试卷类型表实体类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@Data
@TableName("exam_paper_type")
@ApiModel(value = "PaperType对象", description = "试卷类型表")
public class PaperType implements Serializable {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String describtion;
  /**
   * 图片
   */
  @ApiModelProperty(value = "图片")
  private String img;
    /**
     * 按钮title
     */
    @ApiModelProperty(value = "按钮title")
    @TableField("buttonTitle")
  private String buttonTitle;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime addtime;
    /**
     * 状态 normal正常 frozen冻结
     */
    @ApiModelProperty(value = "状态 normal正常 frozen冻结")
    private String status;


}
