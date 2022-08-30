
package org.springcrazy.modules.edu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.edu.entity.TrxorderDetail;

import java.time.LocalDateTime;

/**
 * 流水表视图实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TrxorderDetailVO对象", description = "流水表")
public class TrxorderDetailVO extends TrxorderDetail {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "图片路径")
	private String logo;

	/**
	 * 学习进度
	 */
	@ApiModelProperty(value = "学习进度")
	private String studyLearning;

	/**
	 * 应学章节数
	 */
	@ApiModelProperty(value = "应学章节数")
	private int studyKpoints;

	/**
	 * 已学章节数
	 */
	@ApiModelProperty(value = "已学章节数")
	private int studyKpoint;

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
	 * 昵称
	 */
	@ApiModelProperty(value = "昵称")
	protected String showName;
	/**
	 * 真实姓名
	 */
	@ApiModelProperty(value = "真实姓名")
	protected String realName;

	/**
	 * 面授是否有报名记录
	 */
	@ApiModelProperty(value = "面授是否有报名记录")
	protected int lineTeacherIs;

	/**
	 * 面授报名姓名
	 */
	@ApiModelProperty(value = "面授报名姓名")
	protected String lineName;

	/**
	 * 面授报名电话
	 */
	@ApiModelProperty(value = "面授报名电话")
	protected String lineMobile;

	/**
	 * 课程安排
	 */
	@ApiModelProperty(value = "课程安排")
	protected String lineContext;

	/**
	 * 报名时间
	 */
	@ApiModelProperty(value = "报名时间")
	private LocalDateTime lineTime;


	/**
	 * 面授开始时间
	 */
	@ApiModelProperty(value = "面授开始时间")
	private LocalDateTime lineBagenTime;


	/**
	 * 面授结束时间
	 */
	@ApiModelProperty(value = "面授结束时间")
	private LocalDateTime lineEndTime;

	/**
	 * 是否查询是过期的
	 */
	@ApiModelProperty(value = "是否查询是过期的")
	private Integer isCourseEnd;
}
