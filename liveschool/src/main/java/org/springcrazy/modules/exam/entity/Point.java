
package org.springcrazy.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 考点实体类
 *
 * @author TongZhou
 * @since 2020-12-01
 */
@Data
@TableName("exam_point")
@ApiModel(value = "Point对象", description = "考点")
public class Point implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考点Id
     */
    @ApiModelProperty(value = "考点Id")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id")
    private Integer parentId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private LocalDateTime addTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
    /**
     * 专业id
     */
    @ApiModelProperty(value = "专业id")
    private Integer subjectId;
    /**
     * 考频
     */
    @ApiModelProperty(value = "考频")
    private Integer examFrequency;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String info;
    /**
     * 难度 1.简单 2.普通 3.困难
     */
    @ApiModelProperty(value = "难度 1.简单 2.普通 3.困难")
    private Integer level;
    /**
     * 状态 0.正常 1.删除
     */
    @ApiModelProperty(value = "状态 0.正常 1.删除")
    private Integer state;
    /**
     * 排序值
     */
    @ApiModelProperty(value = "排序值")
    private Integer sort;


}
