
package org.springcrazy.modules.system.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 考试记录表
 */

@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class ExportExampaperRecordExcel implements Serializable {
	private static final long serialVersionUID = 1L;

	@ColumnWidth(10)
	@ExcelProperty("id")
	private String id;

	@ColumnWidth(15)
	@ExcelProperty("账号")
	private String userName;
	@ColumnWidth(15)
	@ExcelProperty("手机")
	private String mobile;

	@ColumnWidth(15)
	@ExcelProperty("邮箱")
	private String email;

	@ColumnWidth(15)
	@ExcelProperty("昵称")
	private String showName;

	@ColumnWidth(15)
	@ExcelProperty("真实姓名")
	private String realName;

	@ColumnWidth(20)
	@ExcelProperty("试卷名称")
	private String paperName;
	@ColumnWidth(15)
	@ExcelProperty("专业分类")
	private String subjectName;

	@ColumnWidth(10)
	@ExcelProperty("成绩")
	private BigDecimal userScore;
	@ColumnWidth(15)
	@ExcelProperty("正确率")
	private String accuracy;
	@ColumnWidth(15)
	@ExcelProperty("作答时间")
	private String testTime;
	@ColumnWidth(15)
	@ExcelProperty("考试题数")
	private String qstCount;
	@ColumnWidth(15)
	@ExcelProperty("已做题数")
	private String doneCount;
	@ColumnWidth(15)
	@ExcelProperty("考试状态")
	private String status;
	@ColumnWidth(25)
	@ExcelProperty("交卷时间")
	private Date addTime;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getPaperName() {
		return paperName;
	}

	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public BigDecimal getUserScore() {
		return userScore;
	}

	public void setUserScore(BigDecimal userScore) {
		this.userScore = userScore;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		accuracy = accuracy+"%";
		this.accuracy = accuracy;
	}

	public String getTestTime() {
		return testTime;
	}

	public void setTestTime(String testTime) {
		Integer seconds = Integer.valueOf(testTime);
		Integer minutes = seconds / 60;
		Integer remainingSeconds = seconds % 60;
		String minutesStr=minutes+"";
		String remainingSecondsStr=remainingSeconds+"";

		if(minutes<10){
			minutesStr = "0"+minutes;
		}
		if(remainingSeconds<10){
			remainingSecondsStr = "0"+remainingSeconds;
		}
		this.testTime = minutesStr+":"+remainingSecondsStr;
	}

	public String getQstCount() {
		return qstCount;
	}

	public void setQstCount(String qstCount) {
		this.qstCount = qstCount;
	}

	public String getDoneCount() {
		return doneCount;
	}

	public void setDoneCount(String doneCount) {
		this.doneCount = doneCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if("0".equals(status)){
			this.status = "完成";
		}else if("1".equals(status)){
			this.status = "未考";
		}else{
			this.status = "未考";
		}
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
