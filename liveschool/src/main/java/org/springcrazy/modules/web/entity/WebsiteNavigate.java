
package org.springcrazy.modules.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 导航表实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@TableName("web_website_navigate")
@ApiModel(value = "WebsiteNavigate对象", description = "导航表")
public class WebsiteNavigate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * url地址
     */
    @ApiModelProperty(value = "url地址")
    private String url;
    /**
     * 是否在新页面打开0是1否
     */
    @ApiModelProperty(value = "是否在新页面打开0是1否")
    private Integer newpage;
    /**
     * 类型：index首页、user个人中心、friendlink尾部友链、tab尾部标签
     */
    @ApiModelProperty(value = "类型：index首页、user个人中心、friendlink尾部友链、tab尾部标签")
    private String type;
    /**
     *  手机端脚步导航图片
     */
    @ApiModelProperty(value = " 手机端脚步导航图片")
    private String image;
    /**
     * 显示排序
     */
    @ApiModelProperty(value = "显示排序")
    private Integer sort;
    /**
     * 0正常1冻结
     */
    @ApiModelProperty(value = "0正常1冻结")
    private String status;


}
