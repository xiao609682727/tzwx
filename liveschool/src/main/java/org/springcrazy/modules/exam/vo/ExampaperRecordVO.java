
package org.springcrazy.modules.exam.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.exam.entity.ExampaperRecord;

import java.util.Map;

/**
 * 考试记录表视图实体类
 *
 * @author TongZhou
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExampaperRecordVO对象", description = "考试记录表")
public class ExampaperRecordVO extends ExampaperRecord {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户7天所做试卷总数量
	 * */
	private int paperRecordNums;

	/**
	 * 用户7天所做试题总数量
	 * */
	private int questionNums;

	/**
	 * 用户7天所做试题错题总数量
	 * */
	private int questionErrorNums;

	/**
	 * 用户7天收藏试题数量
	 * */
	private int collectionQuestionNums;

	private String analysisJson;

	private String typeName;
	private String subjectName;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	protected String mobile;
	/**
	 * 邮箱号
	 */
	@ApiModelProperty(value = "邮箱号")
	protected String email;

	/**
	 * 账户名
	 */
	@ApiModelProperty(value = "账户名")
	protected String userName;

	/**
	 * 登录账号
	 */
	@ApiModelProperty(value = "登录账号")
	protected String loginAccount;

	/**
	 * 账户名
	 */
	@ApiModelProperty(value = "真实姓名")
	protected String realName;

	/**
	 * 图表数据
	 */
	Map<String, Object> quesData;

}
