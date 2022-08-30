
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
public class ExportStudentExcel implements Serializable {
	private static final long serialVersionUID = 1L;

	@ColumnWidth(10)
	@ExcelProperty("账户名")
	private String userName;

	@ColumnWidth(15)
	@ExcelProperty("手机")
	private String mobile;

	@ColumnWidth(15)
	@ExcelProperty("邮箱")
	private String email;

	@ColumnWidth(10)
	@ExcelProperty("昵称")
	private String showName;

	@ColumnWidth(10)
	@ExcelProperty("姓名")
	private String realName;

	@ColumnWidth(10)
	@ExcelProperty("年龄")
	private Integer age;

	@ColumnWidth(10)
	@ExcelProperty("性别")
	private String sex;


	@ColumnWidth(10)
	@ExcelProperty("代理商名称")
	private String agentId;

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSex() {
		if(Func.equals(sex,"1")){
			return "男";
		}else if(Func.equals(sex,"2")){
			return "女";
		}else {
			return "保密";
		}
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}
