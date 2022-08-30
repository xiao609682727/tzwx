
package org.springcrazy.modules.edu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 *
 * @author TongZhou
 * @since 2020-05-05
 */
@Data
@TableName("edu_course_package")
@ApiModel(value = "CoursePackage对象", description = "CoursePackage对象")
public class CoursePackage implements Serializable {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  protected Integer id;
    /**
     * 主课程id
     */
    @ApiModelProperty(value = "主课程id")
    protected Integer parentCourseId;
    /**
     * 包含的课程id
     */
    @ApiModelProperty(value = "包含的课程id")
    protected Integer courseId;
    /**
     * 课程套餐排序值
     */
    @ApiModelProperty(value = "课程套餐排序值")
    protected Integer sort;


}
