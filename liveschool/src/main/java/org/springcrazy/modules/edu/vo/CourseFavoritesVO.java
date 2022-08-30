
package org.springcrazy.modules.edu.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.edu.entity.CourseFavorites;

import java.math.BigDecimal;

/**
 * 收藏视图实体类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CourseFavoritesVO对象", description = "收藏")
public class CourseFavoritesVO extends CourseFavorites {
	private static final long serialVersionUID = 1L;
	//课程名称
	private String courseName;
	//课程类型
	private String sellType;
	//简介
	private String title;
	//销售价格
	private BigDecimal currentPrice;
	//封面图
	private String logo;
}
