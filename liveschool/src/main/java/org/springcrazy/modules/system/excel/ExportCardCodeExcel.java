
package org.springcrazy.modules.system.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import org.springcrazy.core.tool.utils.Func;

import java.io.Serializable;

/**
 * CardCode
 */
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
@Data
public class ExportCardCodeExcel implements Serializable {
	private static final long serialVersionUID = 1L;

	@ColumnWidth(15)
	@ExcelProperty("卡号")
	private String cardCode;

	@ColumnWidth(15)
	@ExcelProperty("密码")
	private String cardCodePassword;

	@ColumnWidth(15)
	@ExcelProperty("状态")
	private String status;

	@ColumnWidth(10)
	@ExcelProperty("用户id")
	private Integer userId;

	@ColumnWidth(15)
	@ExcelProperty("手机")
	private String mobile;

	@ColumnWidth(15)
	@ExcelProperty("账号")
	private String userName;

	@ColumnWidth(15)
	@ExcelProperty("昵称")
	private String showName;


	@ColumnWidth(15)
	@ExcelProperty("订单id")
	private String trxorderId;

	@ColumnWidth(20)
	@ExcelProperty("订单号")
	private String requestId;

	@ColumnWidth(25)
	@ExcelProperty("创建时间")
	private String createTime;

	@ColumnWidth(25)
	@ExcelProperty("使用时间")
	private String useTime;

	public String getStatus() {
		if(Func.equals(status,"init")){
			return "未使用";
		}else if(Func.equals(status,"used")){
			return "已使用";
		}else if(Func.equals(status,"invalid")){
			return "作废";
		}else {
			return "";
		}
	}

	public void setStatus(String status) {
		this.status = status;
	}
//	public String getSex() {
//		if(Func.equals(sex,"1")){
//			return "男";
//		}else if(Func.equals(sex,"2")){
//			return "女";
//		}else {
//			return "保密";
//		}
//	}
//
//	public void setSex(String sex) {
//		this.sex = sex;
//	}
}
