
package org.springcrazy.modules.system.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * UserDTO

 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class UserExcel implements Serializable {
	private static final long serialVersionUID = 1L;

/*	@ColumnWidth(15)
	@ExcelProperty("租户编号")
	private String tenantId;*/

	@ColumnWidth(15)
	@ExcelProperty("账户（必填）")
	private String account;

	@ColumnWidth(10)
	@ExcelProperty("昵称（必填）")
	private String name;

	@ColumnWidth(10)
	@ExcelProperty("姓名（必填）")
	private String realName;

	@ExcelProperty("邮箱（选填）")
	private String email;

	@ColumnWidth(15)
	@ExcelProperty("手机（选填）")
	private String phone;

	@ExcelIgnore
	@ExcelProperty("角色ID")
	private String roleId;

	@ExcelIgnore
	@ExcelProperty("部门ID")
	private String deptId;

	@ExcelIgnore
	@ExcelProperty("岗位ID")
	private String postId;

	@ExcelProperty("角色名称（必填）")
	private String roleName;

	@ExcelProperty("部门名称（必填）")
	private String deptName;

	@ExcelProperty("岗位名称（必填）")
	private String postName;

	@ExcelProperty("密码（选填）")
	private String password;

	@ColumnWidth(20)
	@ExcelProperty("生日（选填）")
	private Date birthday;

}
