
package org.springcrazy.modules.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资料的基本信息实体类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Data
@TableName("edu_course_material")
@ApiModel(value = "CourseMaterial对象", description = "资料基本信息")
public class CourseMaterial implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  /**
   * 资料名称
   */
  @ApiModelProperty(value = "资料名称")
  private String name;

  /**
   * 课程id
   */
  @ApiModelProperty(value = "课程id")
  private Integer courseId;

  /**
   * 章节id
   */
  @ApiModelProperty(value = "章节id")
  private Integer kpointId;

  /**
   * 播放次数
   */
  @ApiModelProperty(value = "文件名称")
  private String fileName;

  /**
   * 播放次数
   */
  @ApiModelProperty(value = "文件路径")
  private String fileUrl;

  /**
   * 显示排序
   */
  @ApiModelProperty(value = "显示排序")
  private Integer sort;

  /**
   * 添加时间
   */
  @ApiModelProperty(value = "添加时间")
  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;


}
