
package org.springcrazy.modules.system.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import java.io.Serializable;
import java.util.Date;

/**
 * UserDTO

 */
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class ExportStudentStudyExcel implements Serializable {
	private static final long serialVersionUID = 1L;

	@ColumnWidth(20)
	@ExcelProperty("账户名")
	private String userName;

	@ColumnWidth(25)
	@ExcelProperty("课程名称")
	private String courseName;

	@ColumnWidth(25)
	@ExcelProperty("章节名称")
	private String kpointName;

	@ColumnWidth(15)
	@ExcelProperty("应学时长")
	private String studyTime;

	@ColumnWidth(20)
	@ExcelProperty("累计时长")
	private String studyTimeNums;

	@ColumnWidth(15)
	@ExcelProperty("学习进度")
	private String studyLearning;

	@ColumnWidth(10)
	@ExcelProperty("是否完成")
	private String complete;

	@ColumnWidth(20)
	@ExcelProperty("最后观看时间")
	private Date updateTime;


	@ColumnWidth(20)
	@ExcelProperty("套餐名称")
	private String pakeName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getKpointName() {
		return kpointName;
	}

	public void setKpointName(String kpointName) {
		this.kpointName = kpointName;
	}

	public String getStudyTime() {
		return studyTime;
	}

	public void setStudyTime(String studyTime) {
		int studyTimeS = Integer.valueOf(studyTime);
		if(studyTimeS!=0){
			String timeStr="";
			Integer hour = studyTimeS / 60 / 60;
			Integer minutes = studyTimeS / 60 % 60;
			Integer remainingSeconds = studyTimeS % 60;
			timeStr=hour+":";
			if(minutes<10){
				timeStr+="0"+minutes+":";
			}else {
				timeStr+=minutes+":";
			}
			if(remainingSeconds<10){
				timeStr+="0"+remainingSeconds;
			}else {
				timeStr+=remainingSeconds;
			}
			this.studyTime = timeStr;
		}else {
			this.studyTime = "";
		}
	}

	public String getStudyTimeNums() {
		return studyTimeNums;
	}

	public void setStudyTimeNums(String studyTimeNums) {
		if(("0").equals(studyTimeNums)){
			this.studyTimeNums = "暂未学习时长";
		}else {
			int studyTimeS = Integer.valueOf(studyTimeNums);
			String timeStr="";
			Integer hour = studyTimeS / 60 / 60;
			Integer minutes = studyTimeS / 60 % 60;
			Integer remainingSeconds = studyTimeS % 60;
			timeStr=hour+":";
			if(minutes<10){
				timeStr+="0"+minutes+":";
			}else {
				timeStr+=minutes+":";
			}
			if(remainingSeconds<10){
				timeStr+="0"+remainingSeconds;
			}else {
				timeStr+=remainingSeconds;
			}
			this.studyTimeNums = timeStr;
		}
	}

	public String getStudyLearning() {
		return studyLearning;
	}

	public void setStudyLearning(String studyLearning) {
		this.studyLearning = studyLearning;
	}

	public String getComplete() {
		return complete;
	}

	public void setComplete(String complete) {
		this.complete = complete;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getPakeName() {
		return pakeName;
	}

	public void setPakeName(String pakeName) {
		this.pakeName = pakeName;
	}

}
