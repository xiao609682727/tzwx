
package org.springcrazy.modules.coursecard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程卡课程表实体类
 *
 * @author TongZhou
 * @since 2021-04-02
 */
@Data
@TableName("edu_card_course")
@ApiModel(value = "CardCourse对象", description = "课程卡课程表")
public class CardCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * card的主键
     */
    @ApiModelProperty(value = "card的主键")
    private Integer cardId;
    /**
     * 课程id
     */
    @ApiModelProperty(value = "课程id")
    private Integer courseId;
    /**
     * 类型：课程course 试卷exam
     */
    @ApiModelProperty(value = "类型：课程course 试卷exam")
    private String type;


}
