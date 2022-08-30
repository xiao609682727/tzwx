
package org.springcrazy.modules.web.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.json.SerializerBigDecimal;
import org.springcrazy.modules.web.entity.WebsiteRecommendDetail;

import java.math.BigDecimal;

/**
 * 推荐详情表视图实体类
 *
 * @author TongZhou
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WebsiteRecommendDetailVO对象", description = "推荐详情表")
public class WebsiteRecommendDetailVO extends WebsiteRecommendDetail {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "课程名称")
	private String courseName;
	@ApiModelProperty(value = "课程类型 ：1(课程) 2(直播) 3(套餐)")
	private String sellType;
	@ApiModelProperty(value = "课程销售价格（实际支付价格）设置为0则可免费观看")
	@JsonSerialize(using = SerializerBigDecimal.class)
	private BigDecimal currentPrice;
	/**
	 * 图片路径
	 */
	@ApiModelProperty(value = "图片路径")
	private String logo;
	/**
	 * 浏览数量
	 */
	@ApiModelProperty(value = "浏览数量")
	private Integer pageViewcount;
	/**
	 * 假浏览量
	 */
	@ApiModelProperty(value = "假浏览量")
	private Integer bogusViewcount;
	/**
	 * 销售数量
	 */
	@ApiModelProperty(value = "销售数量")
	private Integer pageBuycount;
	/**
	 * 假销售数
	 */
	@ApiModelProperty(value = "假销售数")
	private Integer bogusBuycount;

	/**
	 * 课程原价格（只显示）
	 */
	@JsonSerialize(using = SerializerBigDecimal.class)
	@ApiModelProperty(value = "课程原价格（只显示）")
	private BigDecimal sourcePrice;

}
