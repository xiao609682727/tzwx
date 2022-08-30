
package org.springcrazy.modules.system.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import org.springcrazy.core.tool.utils.Func;

import java.io.Serializable;
import java.util.Date;

/**
 * UserDTO

 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class ExportUserExcel implements Serializable {
	private static final long serialVersionUID = 1L;

/*	@ColumnWidth(15)
	@ExcelProperty("租户编号")
	private String tenantId;*/

	@ColumnWidth(15)
	@ExcelProperty("登录账户")
	private String account;

	@ColumnWidth(15)
	@ExcelProperty("用户昵称")
	private String name;

	@ColumnWidth(15)
	@ExcelProperty("用户姓名")
	private String realName;

	@ExcelIgnore
	@ExcelProperty("角色ID")
	private String roleId;

	@ExcelIgnore
	@ExcelProperty("部门ID")
	private String deptId;

	@ExcelIgnore
	@ExcelProperty("岗位ID")
	private String postId;

	@ColumnWidth(15)
	@ExcelProperty("角色名称")
	private String roleName;

	@ColumnWidth(15)
	@ExcelProperty("部门名称")
	private String deptName;

	@ColumnWidth(15)
	@ExcelProperty("岗位名称")
	private String postName;

	@ColumnWidth(20)
	@ExcelProperty("用户编号")
	private String code;

	@ColumnWidth(20)
	@ExcelProperty("电子邮箱")
	private String email;

	@ColumnWidth(15)
	@ExcelProperty("手机")
	private String phone;


	@ColumnWidth(20)
	@ExcelProperty("用户生日")
	private Date birthday;

	@ColumnWidth(15)
	@ExcelProperty("用户性别")
	private String sex;

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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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
