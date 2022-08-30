
package org.springcrazy.modules.system.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import org.springcrazy.core.tool.utils.Func;

import java.io.Serializable;

/**
 * UserDTO

 */
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class AgentUserAccountHistoryExcel implements Serializable {
	private static final long serialVersionUID = 1L;

	@ColumnWidth(10)
	@ExcelProperty("id")
	private String id;

	@ColumnWidth(15)
	@ExcelProperty("代理商账号")
	private String account;

	@ColumnWidth(15)
	@ExcelProperty("代理商昵称")
	private String name;

	@ColumnWidth(15)
	@ExcelProperty("代理商真实姓名")
	private String realName;

	@ColumnWidth(15)
	@ExcelProperty("代理商手机号")
	private String phone;

	@ColumnWidth(15)
	@ExcelProperty("代理商用户id")
	private String agentUserId;

	@ColumnWidth(15)
	@ExcelProperty("订单ID")
	private String orderId;

	@ColumnWidth(20)
	@ExcelProperty("订单号")
	private String orderNo;

	@ColumnWidth(15)
	@ExcelProperty("当前余额")
	private String balance;

	@ColumnWidth(15)
	@ExcelProperty("交易金额")
	private String trxAmount;

	@ColumnWidth(15)
	@ExcelProperty("描述")
	private String description;

	@ColumnWidth(15)
	@ExcelProperty("交易类型")
	private String actHistoryType;

	@ColumnWidth(15)
	@ExcelProperty("业务类型")
	private String bizType;

	@ColumnWidth(25)
	@ExcelProperty("创建时间")
	private String createTime;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(String agentUserId) {
		this.agentUserId = agentUserId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getTrxAmount() {
		return trxAmount;
	}

	public void setTrxAmount(String trxAmount) {
		this.trxAmount = trxAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getActHistoryType() {
		//充值(add),扣款(subtraction),消费(consumption)
		if(Func.equals(actHistoryType,"add")){
			return "充值";
		} else if(Func.equals(actHistoryType,"subtraction")){
			return "扣款";
		} else if(Func.equals(actHistoryType,"consumption")){
			return "消费";
		}else {
			return "消费";
		}
	}

	public void setActHistoryType(String actHistoryType) {
		this.actHistoryType = actHistoryType;
	}

	public String getBizType() {
		//系统订单 system,课程订单 course
		if(Func.equals(bizType,"system")){
			return "系统订单";
		} else if(Func.equals(bizType,"course")){
			return "课程订单";
		}else {
			return "课程订单";
		}
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
