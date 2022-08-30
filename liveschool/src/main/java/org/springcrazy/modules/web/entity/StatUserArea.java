
package org.springcrazy.modules.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户ip登录所在区域记录实体类
 *
 * @author TongZhou
 * @since 2020-05-20
 */
@Data
@TableName("web_stat_user_area")
@ApiModel(value = "StatUserArea对象", description = "用户ip登录所在区域记录")
public class StatUserArea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 用户ip
     */
    @ApiModelProperty(value = "用户ip")
    private String ip;
    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private Integer province;
    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private Integer city;
    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private Integer area;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
