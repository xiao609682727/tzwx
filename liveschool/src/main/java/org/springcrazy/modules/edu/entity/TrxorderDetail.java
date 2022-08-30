
package org.springcrazy.modules.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springcrazy.core.json.SerializerBigDecimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 流水表实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@TableName("edu_trxorder_detail")
@ApiModel(value = "TrxorderDetail对象", description = "流水表")
public class TrxorderDetail implements Serializable {

    private static final long serialVersionUID = 1L;
  //待支付
  public static final String STATUS_INIT = "1";
  //支付成功
  public static final String STATUS_SUCCESS = "2";
  //取消订单
  public static final String STATES_CANCEL = "3";
  //退款
  public static final String STATES_BACKMONEY = "4";

  //订单类型
  public static final String TYPE_COURSE = "COURSE";

  public TrxorderDetail() {
  }

  @Builder(toBuilder = true)
  public TrxorderDetail(Integer id, Integer userId, Integer courseId, Integer trxorderId, String orderNo, String losetype, Date loseAbsTime, String loseTime, Date beginTime, Date authTime, Date createTime, Date payTime, BigDecimal sourcePrice, BigDecimal couponPrice, BigDecimal currentPrice, String courseName, String authStatus, String description, Date lastUpdateTime, String remindStatus, String delStatus, String trxorderType) {
    this.id = id;
    this.userId = userId;
    this.courseId = courseId;
    this.trxorderId = trxorderId;
    this.orderNo = orderNo;
    this.losetype = losetype;
    this.loseAbsTime = loseAbsTime;
    this.loseTime = loseTime;
    this.beginTime = beginTime;
    this.authTime = authTime;
    this.createTime = createTime;
    this.payTime = payTime;
    this.sourcePrice = sourcePrice;
    this.couponPrice = couponPrice;
    this.currentPrice = currentPrice;
    this.courseName = courseName;
    this.authStatus = authStatus;
    this.description = description;
    this.lastUpdateTime = lastUpdateTime;
    this.remindStatus = remindStatus;
    this.delStatus = delStatus;
    this.trxorderType = trxorderType;
  }




  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 相关联的课程id/套餐id/会员商品 id/试卷id（前台快照）
     */
    @ApiModelProperty(value = "相关联的课程id/套餐id/会员商品 id/试卷id（前台快照）")
    private Integer courseId;
    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Integer trxorderId;
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /**
     * 有效期类型（前台快照）
     */
    @ApiModelProperty(value = "有效期类型（前台快照）")
    private String losetype;
    /**
     * 订单过期时间段（前台快照）
     */
    @ApiModelProperty(value = "订单过期时间段（前台快照）")
    private Date loseAbsTime;
    /**
     * 订单过期时间点（前台快照）
     */
    @ApiModelProperty(value = "订单过期时间点（前台快照）")
    private String loseTime;
    /**
     * 商品开始时间
     */
    @ApiModelProperty(value = "商品开始时间")
    private Date beginTime;
    /**
     * 商品过期时间
     */
    @ApiModelProperty(value = "商品过期时间")
    private Date authTime;
    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 支付成功时间
     */
    @ApiModelProperty(value = "支付成功时间")
    private Date payTime;
    /**
     * 原价格（前台快照）
     */
    @ApiModelProperty(value = "原价格（前台快照）")
    @JsonSerialize(using = SerializerBigDecimal.class)
    private BigDecimal sourcePrice;
    /**
     * 优惠价格
     */
    @ApiModelProperty(value = "优惠价格")
    @JsonSerialize(using = SerializerBigDecimal.class)
    private BigDecimal couponPrice;
    /**
     * 销售价格（前台快照）
     */
    @ApiModelProperty(value = "销售价格（前台快照）")
    @JsonSerialize(using = SerializerBigDecimal.class)
    private BigDecimal currentPrice;
    /**
     * 课程名称（前台goods快照）
     */
    @ApiModelProperty(value = "课程名称（前台goods快照）")
    private String courseName;
    /**
     * 课程状态（init 未支付，success 成功，refund 退款，closed，losed,cancel 删除,delete(个人删除课程)）
     */
    @ApiModelProperty(value = "课程状态（init 未支付，success 成功，refund 退款，closed，losed,cancel 删除,delete(个人删除课程)）")
    private String authStatus;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;
    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "最后更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastUpdateTime;
    /**
     * 过期是否提醒 init 未提醒 already 已提醒
     */
    @ApiModelProperty(value = "过期是否提醒 init 未提醒 already 已提醒")
    private String remindStatus;
    /**
     * 个人订单中心删除课程
     */
    @ApiModelProperty(value = "个人订单中心删除课程")
    private String delStatus;
    /**
     * 流水类型（COURSE 录播 LIVE 直播 PACKAGE 课程套餐 lineCourse 线下课 MEMBER 会员 ACCOUNT 充值 examPaper 试卷 practiceQuestion 练习题库 passThrough 闯关 examPackage 考试套餐 ClASS 班级 examTimed 定时考试）
     */
    @ApiModelProperty(value = "流水类型（COURSE 录播 LIVE 直播 PACKAGE 课程套餐 lineCourse 线下课 MEMBER 会员 ACCOUNT 充值 examPaper 试卷 practiceQuestion 练习题库 passThrough 闯关 examPackage 考试套餐 ClASS 班级 examTimed 定时考试）")
    private String trxorderType;


}
