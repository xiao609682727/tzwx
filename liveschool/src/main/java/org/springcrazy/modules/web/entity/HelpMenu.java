
package org.springcrazy.modules.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 帮助菜单实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@TableName("web_help_menu")
@ApiModel(value = "HelpMenu对象", description = "帮助菜单")
public class HelpMenu implements Serializable {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 父级分类id，默认0为一级分类
     */
    @ApiModelProperty(value = "父级分类id，默认0为一级分类")
    private Integer parentId;
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String name;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 菜单内容
     */
    @ApiModelProperty(value = "菜单内容")
    private String content;
    /**
     * 排序  倒叙
     */
    @ApiModelProperty(value = "排序  倒叙")
    private Integer sort;
    /**
     * 外链字段
     */
    @ApiModelProperty(value = "外链字段")
    private String linkBuilding;


}
