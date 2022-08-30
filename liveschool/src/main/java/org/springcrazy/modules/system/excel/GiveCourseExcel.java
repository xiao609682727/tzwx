
package org.springcrazy.modules.system.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.io.Serializable;

/**
 * UserDTO
 *
 * @author NCX
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class GiveCourseExcel implements Serializable {
	private static final long serialVersionUID = 1L;

	@ColumnWidth(20)
	@ExcelProperty("账号（选填）")
	private String userName;

	@ColumnWidth(15)
	@ExcelProperty("手机（选填）")
	private String mobile;

	@ColumnWidth(20)
	@ExcelProperty("邮箱（选填）")
	private String email;

	@ColumnWidth(20)
	@ExcelProperty("用户id（选填）")
	private Integer userId;

	@ColumnWidth(25)
	@ExcelProperty("赠送(套餐)课程id（必填）")
	private String courseIds;

}
