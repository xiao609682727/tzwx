
package org.springcrazy.modules.system.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import java.io.Serializable;

/**
 * OrderDTO

 */
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class ExportOrderExcel implements Serializable {
	private static final long serialVersionUID = 1L;
	@ColumnWidth(15)
	@ExcelProperty("时间")
	private String createTimeStatis;

	@ColumnWidth(10)
	@ExcelProperty("成交量")
	private String volume;

	@ColumnWidth(10)
	@ExcelProperty("交易学币金额")
	private String actualIncome;

	public String getCreateTimeStatis() {
		return createTimeStatis;
	}

	public void setCreateTimeStatis(String createTimeStatis) {
		this.createTimeStatis = createTimeStatis;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getActualIncome() {
		return actualIncome;
	}

	public void setActualIncome(String actualIncome) {
		this.actualIncome = actualIncome;
	}
}
