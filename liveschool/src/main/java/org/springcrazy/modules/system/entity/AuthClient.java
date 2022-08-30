
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
 *
 * @author TongZhouX
 * @since 2019-03-24
 */
@Data
@TableName("crazy_client")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Client对象", description = "Client对象")
public class AuthClient extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value = "主键id")
	private Integer id;

	/**
	 * 客户端id
	 */
	@ApiModelProperty(value = "客户端id")
	private String clientId;
	/**
	 * 客户端密钥
	 */
	@ApiModelProperty(value = "客户端密钥")
	private String clientSecret;
	/**
	 * 资源集合
	 */
	@ApiModelProperty(value = "资源集合")
	private String resourceIds;
	/**
	 * 授权范围
	 */
	@ApiModelProperty(value = "授权范围")
	private String scope;
	/**
	 * 授权类型
	 */
	@ApiModelProperty(value = "授权类型")
	private String authorizedGrantTypes;
	/**
	 * 回调地址
	 */
	@ApiModelProperty(value = "回调地址")
	private String webServerRedirectUri;
	/**
	 * 权限
	 */
	@ApiModelProperty(value = "权限")
	private String authorities;
	/**
	 * 令牌过期秒数
	 */
	@ApiModelProperty(value = "令牌过期秒数")
	private Integer accessTokenValidity;
	/**
	 * 刷新令牌过期秒数
	 */
	@ApiModelProperty(value = "刷新令牌过期秒数")
	private Integer refreshTokenValidity;
	/**
	 * 附加说明
	 */
	@ApiModelProperty(value = "附加说明")
	private String additionalInformation;
	/**
	 * 自动授权
	 */
	@ApiModelProperty(value = "自动授权")
	private String autoapprove;


}
