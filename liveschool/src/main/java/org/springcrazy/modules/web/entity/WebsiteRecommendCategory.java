
package org.springcrazy.modules.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 网站推荐分类实体类
 *
 * @author TongZhou
 * @since 2020-05-13
 */
@Data
@TableName("web_website_recommend_category")
@ApiModel(value = "WebsiteRecommendCategory对象", description = "网站推荐分类")
public class WebsiteRecommendCategory implements Serializable {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 推荐模块名称
     */
    @ApiModelProperty(value = "推荐模块名称")
    private String name;
    /**
     * 点击更多链接
     */
    @ApiModelProperty(value = "点击更多链接")
    private String link;
    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String description;
    /**
     * 推荐课程限制数量
     */
    @ApiModelProperty(value = "推荐课程限制数量")
    private Integer coursenum;
    /**
     * 1 课程
     */
    @ApiModelProperty(value = "1 课程")
    private String type;
    /**
     * 1 首页推荐
     */
    @ApiModelProperty(value = "1 首页推荐")
  private Integer frontType;


}
