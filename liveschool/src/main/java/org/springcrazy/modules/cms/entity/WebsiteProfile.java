
package org.springcrazy.modules.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统配置表实体类
 *
 * @author TongZhou
 * @since 2020-03-04
 */
@Data
@TableName("cms_website_profile")
@ApiModel(value = "WebsiteProfile对象", description = "系统配置表")
public class WebsiteProfile implements Serializable {

    private static final long serialVersionUID = 1L;

  public static final String ALIYUN = "1";
  public static final String TENCENTYUN = "2";
  public static final String BAIJIAYUN = "3";

    public static final String EMAIL = "email";
  public static final String SMS = "sms";
  public static final String ALIYUN_VOD = "aliyun_vod";
  public static final String BAIJIA_VOD = "baijiayun_vod";
  public static final String BAIJIA_VIDEO = "baijiayun_video";
  public static final String ALIYUN_LIVE = "aliyun_vod";
  public static final String ALIPAY = "alipay";
  public static final String APPANDROID = "appAndroid";
  public static final String WECHATPAY = "wechatpay";
  public static final String WECHATAPPPAY = "wechatapppay";
  public static final String WECHATMP = "wechatmp";
  public static final String WEBREGISTER = "webregister";


  public static final String POLYV = "polyv";

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String configType;
    /**
     * 表单类型
     */
    @ApiModelProperty(value = "表单类型 ")
    private String formType;
    /**
     * key值
     */
    @ApiModelProperty(value = "key值")
    private String dataKey;
    /**
     * 配置内容
     */
    @ApiModelProperty(value = "配置内容")
    private String dataValue;
    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String dataExplain;

  @ApiModelProperty(value = "提示")
  private String tip;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;


}
