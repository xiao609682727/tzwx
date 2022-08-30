
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
public class OrderExcel implements Serializable {
	private static final long serialVersionUID = 1L;

	@ColumnWidth(15)
	@ExcelProperty("订单号")
	private String orderNo;

	@ColumnWidth(10)
	@ExcelProperty("手机")
	private String mobile;

	@ColumnWidth(10)
	@ExcelProperty("邮箱")
	private String email;

	@ExcelProperty("账号")
	private String userName;

	@ExcelProperty("订单总金额")
	private String sumMoney;

	@ExcelProperty("订单状态")
	private String states;

	@ExcelProperty("创建时间")
	private String createTime;

	@ExcelProperty("支付时间")
	private String payTime;

	@ExcelProperty("支付类型")
	private String payType;

	@ExcelProperty("订单类型")
	private String orderType;


}
