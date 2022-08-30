
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
public class UserAccountHistoryExcel implements Serializable {
	private static final long serialVersionUID = 1L;

	@ColumnWidth(10)
	@ExcelProperty("id")
	private String id;

	@ColumnWidth(15)
	@ExcelProperty("账号")
	private String userName;

	@ColumnWidth(15)
	@ExcelProperty("手机")
	private String mobile;

	@ColumnWidth(15)
	@ExcelProperty("邮箱")
	private String email;

	@ColumnWidth(15)
	@ExcelProperty("当前学币")
	private String balance;

	@ColumnWidth(15)
	@ExcelProperty("交易学币金额")
	private String trxAmount;

	@ColumnWidth(20)
	@ExcelProperty("描述")
	private String description;

	@ColumnWidth(15)
	@ExcelProperty("交易类型")
	private String actHistoryType;

	@ColumnWidth(15)
	@ExcelProperty("业务类型")
	private String bizType;

	@ColumnWidth(20)
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
		if(Func.equals(actHistoryType,"1")){
			return "充值";
		} else {
			return "消费";
		}
	}

	public void setActHistoryType(String actHistoryType) {
		this.actHistoryType = actHistoryType;
	}

	public String getBizType() {
		if(Func.equals(bizType,"1")){
			return "后台操作";
		} else if(Func.equals(bizType,"2")){
			return "课程订单";
		}else {
			return "储值充值";
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
