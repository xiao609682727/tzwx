
package org.springcrazy.modules.system.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import org.springcrazy.core.tool.utils.Func;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * UserDTO

 */
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class ExportLineClassExcel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *  未支付，2 支付成功，3 后台赠送未报名
	 */
	@ColumnWidth(22)
	@ExcelProperty("报名状态")
	private String state;

	/**
	 * 用户id
	 */
	@ColumnWidth(10)
	@ExcelProperty("用户id")
	private Integer userId;



	@ColumnWidth(22)
	@ExcelProperty("平台昵称")
	private String showName;

	/**
	 * 报名用户姓名
	 */
	@ColumnWidth(22)
	@ExcelProperty("用户姓名")
	private String userName;

	/**
	 * 报名用户手机号
	 */
	@ColumnWidth(22)
	@ExcelProperty("用户手机号")
	private String mobile;

	/**
	 * 课程名称
	 */
	@ColumnWidth(22)
	@ExcelProperty("面授课程")
	private String courseName;

	/**
	 * 讲师名称
	 */
	@ColumnWidth(22)
	@ExcelProperty("讲师名称")
	private String name;

	/**
	 * 报名时间
	 */
	@ColumnWidth(22)
	@ExcelProperty("报名时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 有效期时间
	 */
	@ColumnWidth(22)
	@ExcelProperty("有效期时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date authTime;

	/**
	 * 支付时间
	 */
	@ColumnWidth(22)
	@ExcelProperty("支付时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payTime;






	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public Date getPayTime() {
		return payTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getAuthTime() {
		return authTime;
	}

	public void setAuthTime(Date authTime) {
		this.authTime = authTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		if(Func.equals(state,"1")){
			this.state = "已报名,未支付";
		}else if(Func.equals(state,"2")){
			this.state = "已报名,已支付";
		}else if(Func.equals(state,"3")){
			this.state = "后台赠送，未报名";
		}else {
			this.state = "未知";
		}
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
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


	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
}
