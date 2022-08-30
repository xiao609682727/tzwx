
package org.springcrazy.modules.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 专业分类实体类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Data
@TableName("edu_subject")
@ApiModel(value = "Subject对象", description = "专业分类")
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 专业名称
     */
    @ApiModelProperty(value = "专业名称")
    private String subjectName;
    /**
     * 状态 1:正常 2:删除
     */
    @TableLogic
    @ApiModelProperty(value = "状态 1:正常 2:删除")
    private Boolean status;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private Integer parentId;
    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private Integer sort;
    /**
     * 专业类型 course 课程
     */
    @ApiModelProperty(value = "course 课程 exam 考试 lineclass 面授课 ")
    private String type;


}
