
package org.springcrazy.modules.system.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.io.Serializable;

/**
 * UserDTO

 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class StudentExcel implements Serializable {
	private static final long serialVersionUID = 1L;

	@ColumnWidth(20)
	@ExcelProperty("账号(选填，唯一)")
	private String userName;

	@ColumnWidth(15)
	@ExcelProperty("手机(选填，唯一)")
	private String mobile;

	@ColumnWidth(20)
	@ExcelProperty("邮箱(选填，唯一)")
	private String email;

	@ColumnWidth(20)
	@ExcelProperty("昵称(选填)")
	private String showName;

	@ColumnWidth(25)
	@ExcelProperty("真实姓名(选填)")
	private String realName;

	@ColumnWidth(25)
	@ExcelProperty("身份证号(选填)")
	private String idCardNo;

	@ColumnWidth(20)
	@ExcelProperty("性别(选填)")
	private String sex;

	@ColumnWidth(20)
	@ExcelProperty("年龄(选填)")
	private Integer age;

	@ColumnWidth(35)
	@ExcelProperty("密码(选填，默认：123456)")
	private String password;

	@ColumnWidth(35)
	@ExcelProperty("赠送课程(选填)")
	private String courseIdList;
}
