
package org.springcrazy.modules.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.mp.base.BaseEntity;

/**
 * 专业信息实体类
 *
 * @author TongZhou
 * @since 2020-02-28
 */
@Data
@TableName("cms_subject")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Subject对象", description = "专业信息")
public class CmsSubject extends BaseEntity {

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
     * 专业类型 article网校资讯
     */
    @ApiModelProperty(value = "专业类型 article网校资讯")
    private String type;
    /**
     * 课程专业ID(一级分类)
     */
    @ApiModelProperty(value = "课程专业ID(一级分类)")
    @TableField("courseSubjectId")
  private Integer courseSubjectId;


}
