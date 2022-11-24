
package org.springcrazy.modules.system.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import org.springcrazy.core.tool.utils.Func;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * OrderDTO

 */
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class ExportOrdersExcel implements Serializable {
	private static final long serialVersionUID = 1L;
	@ColumnWidth(15)
	@ExcelProperty("用户Id")
	private Integer userId;

	@ColumnWidth(20)
	@ExcelProperty("账户名")
	private String userName;

	@ColumnWidth(15)
	@ExcelProperty("手机")
	private String mobile;

	@ColumnWidth(20)
	@ExcelProperty("邮箱")
	private String email;

	@ColumnWidth(20)
	@ExcelProperty("昵称")
	private String showName;

	@ColumnWidth(20)
	@ExcelProperty("姓名")
	private String realName;
	@ColumnWidth(22)
	@ExcelProperty("订单号")
	private String orderNo;

	@ColumnWidth(10)
	@ExcelProperty("订单总价")
	private BigDecimal sumMoney;

	@ColumnWidth(22)
	@ExcelProperty("订单创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@ColumnWidth(22)
	@ExcelProperty("订单支付时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payTime;

	@ColumnWidth(15)
	@ExcelProperty("支付类型")
	private String payType;

	@ColumnWidth(15)
	@ExcelProperty("订单状态")
	private String states;

	/*@ColumnWidth(10)
	@ExcelProperty("退款金额")
	private BigDecimal refundAmount;*/

	@ColumnWidth(15)
	@ExcelProperty("订单类型")
	private String orderType;

	/*@ColumnWidth(10)
	@ExcelProperty("请求渠道")
	private String reqChannel;*/


	public String getOrderNo() {
		return orderNo;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public BigDecimal getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(BigDecimal sumMoney) {
		this.sumMoney = sumMoney;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getPayType() {
		if(Func.equals(payType,"1")){
			return "支付宝";
		} else if(Func.equals(payType,"2")){
			return "微信";
		}else if(Func.equals(payType,"3")){
			return "后台赠送";
		}else {
			return "账号储值";
		}
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	/*public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
*/
	public String getOrderType() {
		if(Func.equals(orderType,"COURSE")){
			return "课程";
		} else if(Func.equals(orderType,"MEMBER")){
			return "会员";
		}else if(Func.equals(orderType,"LIVE")){
			return "直播";
		}else if(Func.equals(orderType,"PACKAGE")){
			return "班级";
		}else if(Func.equals(orderType,"LINECLASS")){
			return "面授";
		}else {
			return "账户充值";
		}
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		if (Func.equals(userName , "undefined")){
			this.userName = "";
		}else {
			this.userName = userName;
		}
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getStates() {
		if(Func.equals(states,"1")){
			return "未支付";
		} else if(Func.equals(states,"2")){
			return "已支付";
		}else if(Func.equals(states,"3")){
			return "已取消";
		}else {
			return "退款";
		}
	}

	public void setStates(String states) {
		this.states = states;
	}


}
