
package org.springcrazy.modules.edu.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.json.SerializerBigDecimal;
import org.springcrazy.modules.edu.entity.CoursePackage;

import java.math.BigDecimal;

/**
 * 视图实体类
 *
 * @author TongZhou
 * @since 2020-05-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CoursePackageVO对象", description = "CoursePackageVO对象")
public class CoursePackageVO extends CoursePackage {
	private static final long serialVersionUID = 1L;
	//课程名称
	private String courseName;
	//课程类型
	private String sellType;
	//销售价格
	@JsonSerialize(using = SerializerBigDecimal.class)
	private BigDecimal currentPrice;

	private String logo;

	private String title;

}
