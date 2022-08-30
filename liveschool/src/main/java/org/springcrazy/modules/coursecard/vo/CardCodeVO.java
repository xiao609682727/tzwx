
package org.springcrazy.modules.coursecard.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.coursecard.entity.CardCode;

import java.io.UnsupportedEncodingException;

/**
 * 课程卡编码表视图实体类
 *
 * @author TongZhou
 * @since 2021-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CardCodeVO对象", description = "课程卡编码表")
public class CardCodeVO extends CardCode {
	private static final long serialVersionUID = 1L;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	protected String mobile;
	/**
	 * 邮箱号
	 */
	@ApiModelProperty(value = "邮箱号")
	protected String email;

	/**
	 * 账户名
	 */
	@ApiModelProperty(value = "账户名")
	protected String userName;
	/**
	 * 昵称
	 */
	@ApiModelProperty(value = "昵称")
	protected String showName;

	/**前后台展示用户名*/
	@ApiModelProperty(value = "前后台展示用户名")
	private String displayName;

	/**
	 * 真实姓名
	 */
	@ApiModelProperty(value = "真实姓名")
	protected String realName;

	public void setUserName(String userName) throws UnsupportedEncodingException {
		this.userName = userName;
		initDisplayName();
	}
	public void setShowName(String showName) throws UnsupportedEncodingException {
		this.showName = showName;
		initDisplayName();
	}

	public void setEmail(String email){
		this.email = email;
		initDisplayName();
	}
	public void setMobile(String mobile){
		this.mobile = mobile;
		initDisplayName();
	}
	public void setRealName(String realName){
		this.realName = realName;
		initDisplayName();
	}

	/**
	 * 根据显示规则显示 昵称-》账户-》手机-》邮箱
	 */
	public void initDisplayName(){
		if(Func.isNotEmpty(this.realName)){
			this.displayName = realName;
			return;
		}
		if(Func.isNotEmpty(this.showName)){
			this.displayName = showName;
			return;
		}
		if(Func.isNotEmpty(this.userName)){
			this.displayName = userName;
			return;
		}
		if(Func.isNotEmpty(this.mobile)){
			this.displayName = mobile;
			return;
		}
		if(Func.isNotEmpty(this.email)){
			this.displayName = email;
			return;
		}
	}
}
