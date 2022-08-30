
package org.springcrazy.modules.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收藏实体类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Data
@TableName("edu_course_favorites")
@ApiModel(value = "CourseFavorites对象", description = "收藏")
public class CourseFavorites implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 课程名称
     */
    @ApiModelProperty(value = "课程名称")
    private String courseName;
    /**
     * 课程id
     */
    @ApiModelProperty(value = "课程id")
    private Integer courseId;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 类型COURSE1EXAM2CLASS3
     */
    @ApiModelProperty(value = "类型COURSE1EXAM2CLASS3")
    private String type;


}
