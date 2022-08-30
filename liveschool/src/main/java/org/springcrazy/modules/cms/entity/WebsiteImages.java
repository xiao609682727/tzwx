
package org.springcrazy.modules.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * banner图管理实体类
 *
 * @author TongZhou
 * @since 2020-03-06
 */
@Data
@TableName("cms_website_images")
@ApiModel(value = "WebsiteImages对象", description = "banner图管理")
public class WebsiteImages implements Serializable {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;
    /**
     * 图链接地址
     */
    @ApiModelProperty(value = "图链接地址")
    private String linkAddress;
    /**
     * 图标题
     */
    @ApiModelProperty(value = "图标题")
    private String title;
    /**
     * 图片类型
     */
    @ApiModelProperty(value = "图片类型")
    private Integer typeId;
    /**
     * 序列号
     */
    @ApiModelProperty(value = "序列号")
    private Integer seriesNumber;
    /**
     * 略缩图片地址
     */
    @ApiModelProperty(value = "略缩图片地址")
    private String previewUrl;
    /**
     * 背景色
     */
    @ApiModelProperty(value = "背景色")
    private String color;
    /**
     * 图片描述
     */
    @ApiModelProperty(value = "图片描述")
    private String details;


}
