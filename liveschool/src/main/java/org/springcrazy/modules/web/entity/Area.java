
package org.springcrazy.modules.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 *
 * @author TongZhou
 * @since 2020-05-19
 */
@Data
@TableName("web_area")
@ApiModel(value = "Area对象", description = "Area对象")
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 区域编号
     */
    @ApiModelProperty(value = "区域编号")
    private Integer id;
    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String name;
    /**
     * 父级编号
     */
    @ApiModelProperty(value = "父级编号")
    private Integer parent;
    /**
     * 区域等级(1省/2市/3区县)
     */
    @ApiModelProperty(value = "区域等级(1省/2市/3区县)")
    private Integer arealevel;
    /**
     * 状态（1可用/0不可用）
     */
    @ApiModelProperty(value = "状态（1可用/0不可用）")
    private Integer status;


}
