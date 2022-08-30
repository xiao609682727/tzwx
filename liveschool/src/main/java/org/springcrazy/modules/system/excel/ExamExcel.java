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
public class ExamExcel implements Serializable {
    private static final long serialVersionUID = 1L;


    @ColumnWidth(15)
    @ExcelProperty("题干（必填）")
    private String qstContent;

    @ColumnWidth(15)
    @ExcelProperty("选项 A（必填）")
    private String optionA;

    @ColumnWidth(15)
    @ExcelProperty("选项 B（必填）")
    private String optionB;

    @ColumnWidth(15)
    @ExcelProperty("选项 C（选填）")
    private String optionC;

    @ColumnWidth(15)
    @ExcelProperty("选项 D（选填）")
    private String optionD;

    @ColumnWidth(15)
    @ExcelProperty("选项E（选填）")
    private String optionE;

    @ColumnWidth(15)
    @ExcelProperty("选项F（选填）")
    private String optionF;

    @ColumnWidth(15)
    @ExcelProperty("选项G（选填）")
    private String optionG;

    @ColumnWidth(15)
    @ExcelProperty("选项H（选填）")
    private String optionH;

    @ColumnWidth(15)
    @ExcelProperty("正确答案（必填）")
    private String isAsr;

    @ColumnWidth(15)
    @ExcelProperty("答案1（必填）")
    private String isAsr1;

    @ColumnWidth(15)
    @ExcelProperty("答案2（选填）")
    private String isAsr2;

    @ColumnWidth(15)
    @ExcelProperty("答案3（选填）")
    private String isAsr3;

    @ColumnWidth(15)
    @ExcelProperty("答案4（选填）")
    private String isAsr4;

    @ColumnWidth(15)
    @ExcelProperty("答案5（选填）")
    private String isAsr5;

    @ColumnWidth(15)
    @ExcelProperty("答案6（选填）")
    private String isAsr6;

    @ColumnWidth(15)
    @ExcelProperty("解析（选填）")
    private String qstAnalyze;

    @ColumnWidth(15)
    @ExcelProperty("分类（必填）")
    private String subjectName;

    /*1简单 2普通 3困难*/
    @ColumnWidth(15)
    @ExcelProperty("难度(必填)")
    private String levelName;

    @ColumnWidth(15)
    @ExcelProperty("排序（选填）")
    private String  sort;

    /*1单选 2多选 3判断 4填空*/
    @ColumnWidth(15)
    @ExcelProperty("类型id")
    private Integer qstType;










}
