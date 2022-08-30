
package org.springcrazy.modules.lineclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.lineclass.entity.LineclassEnroll;

import java.time.LocalDateTime;

/**
 * 线下课报名记录表视图实体类
 *
 * @author TongZhou
 * @since 2021-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LineclassEnrollVO对象", description = "线下课报名记录表")
public class LineclassEnrollVO extends LineclassEnroll {
	private static final long serialVersionUID = 1L;
	/**
	 * 支付时间
	 */
	@ApiModelProperty(value = "支付时间")
	private LocalDateTime payTime;

	/**
	 * 有效期时间
	 */
	@ApiModelProperty(value = "有效期时间")
	private LocalDateTime authTime;


	/**
	 * 用户昵称
	 */
	@ApiModelProperty(value = "用户昵称")
	private String showName;

	/**
	 * 课程名称
	 */
	@ApiModelProperty(value = "课程名称")
	private String courseName;


}
