
package org.springcrazy.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.mp.base.BaseEntity;

/**
 * 实体类

 */
@Data
@TableName("crazy_param")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Param对象", description = "Param对象")
public class Param extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value = "主键id")
	private Integer id;

	/**
	 * 参数名
	 */
	@ApiModelProperty(value = "参数名")
	private String paramName;

	/**
	 * 参数键
	 */
	@ApiModelProperty(value = "参数键")
	private String paramKey;

	/**
	 * 参数值
	 */
	@ApiModelProperty(value = "参数值")
	private String paramValue;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;


}
