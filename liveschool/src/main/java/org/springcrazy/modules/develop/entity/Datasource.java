
package org.springcrazy.modules.develop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.mp.base.BaseEntity;

/**
 * 数据源配置表实体类
 *

 */
@Data
@TableName("crazy_datasource")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Datasource对象", description = "数据源配置表")
public class Datasource extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;
	/**
	 * 驱动类
	 */
	@ApiModelProperty(value = "驱动类")
	private String driverClass;
	/**
	 * 连接地址
	 */
	@ApiModelProperty(value = "连接地址")
	private String url;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String username;
	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	private String password;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;


}
