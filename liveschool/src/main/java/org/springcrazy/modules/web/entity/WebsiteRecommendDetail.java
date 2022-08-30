
package org.springcrazy.modules.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 推荐详情表实体类
 *
 * @author TongZhou
 * @since 2020-05-13
 */
@Data
@TableName("web_website_recommend_detail")
@ApiModel(value = "WebsiteRecommendDetail对象", description = "推荐详情表")
public class WebsiteRecommendDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 推荐分类的id
     */
    @ApiModelProperty(value = "推荐分类的id")
    private Integer recommendId;
    /**
     * 业务id  根据推荐类型判断
     */
    @ApiModelProperty(value = "业务id  根据推荐类型判断")
    private Integer busId;
    /**
     * 课程显示排序
     */
    @ApiModelProperty(value = "课程显示排序")
    private Integer sort;


}
