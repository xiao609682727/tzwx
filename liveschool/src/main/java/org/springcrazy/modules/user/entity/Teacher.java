
package org.springcrazy.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 讲师实体类
 *
 * @author TongZhou
 * @since 2020-04-28
 */
@Data
@TableName("edu_teacher")
@ApiModel(value = "Teacher对象", description = "讲师")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 教师id
     */
    @ApiModelProperty(value = "教师id")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 教师名称
     */
    @ApiModelProperty(value = "教师名称")
    private String name;
    /**
     * 教师资历
     */
    @ApiModelProperty(value = "教师资历")
    private String education;
    /**
     * 教师简介
     */
    @ApiModelProperty(value = "教师简介")
    private String profile;
    /**
     * 教师头衔
     */
    @ApiModelProperty(value = "教师头衔")
    private Integer teaTitle;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String headImg;
    /**
     * web 网校 line 线下课
     */
    @ApiModelProperty(value = "web 网校 line 线下课")
    private String type;
    /**
     * 专业id
     */
    @ApiModelProperty(value = "专业id")
    private Integer subjectId;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 状态:0正常1删除
     */
    @TableLogic
    @ApiModelProperty(value = "状态:0正常1删除")
    private Integer isDeleted;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
