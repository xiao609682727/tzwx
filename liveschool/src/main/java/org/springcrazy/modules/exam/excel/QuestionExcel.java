
package org.springcrazy.modules.exam.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * UserDTO

 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class QuestionExcel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 试题题干
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private String qstContent;
	/**
		 * 正确选项/答案
	 */
	@ColumnWidth(20)
	@ExcelProperty("正确答案")
	private String isAsr;
	/**
	 * 试题类型(1单选题2多选题3判断题4填空题)
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题类型")
	private Integer qstType;
	/**
	 * 试题难度(1简单2普通3困难)
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题难度")
	private Integer level;
	/**
	 * 试题解析
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题解析")
	private String qstAnalyze;
	/**
	 * 更新时间
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private LocalDateTime updateTime;
	/**
	 * 专业id
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private Integer subjectId;
	/**
	 * 1为显示2为删除
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private Integer status;
	/**
	 * 考点id
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private Integer pointId;
	/**
	 * 0为非材料1为材料题 （暂时不用）
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private Integer complexFalg;
	/**
	 * 做过的次数
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private Integer time;
	/**
	 * 该试题被做正确过多少道
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private Integer rightTime;
	/**
	 * 该试题被做错过多少次
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private Integer errorTime;
	/**
	 * 正确率
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private Float accuracy;
	/**
	 * 排序
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private Integer sort;
	/**
	 * 填空题类型 1有序 2无序
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private Integer vacancyType;
	/**
	 * 选择题选项及选项内容，json格式
	 */
	@ColumnWidth(20)
	@ExcelProperty("试题题干")
	private String optionList;



	private String userName;

}
