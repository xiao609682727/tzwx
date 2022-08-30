
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
 * 课程订单表实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@TableName("edu_orders")
@ApiModel(value = "Orders对象", description = "课程订单表")
public class Orders implements Serializable {


  public Orders() {
  }

  @Builder(toBuilder = true)
  public Orders(Integer id, Integer userId, String orderNo, BigDecimal sumMoney, String states, Date createTime, Date payTime, Integer sysUserId, String payType, String reqChannel, String description, BigDecimal orderAmount, BigDecimal cashAmount, BigDecimal vmAmount, BigDecimal backAmount, BigDecimal couponAmount, Integer couponcodeId, BigDecimal bargainAmount, Integer bargainPublishid, BigDecimal refundAmount, String outTradeNo, String orderType) {
    this.id = id;
    this.userId = userId;
    this.orderNo = orderNo;
    this.sumMoney = sumMoney;
    this.states = states;
    this.createTime = createTime;
    this.payTime = payTime;
    this.sysUserId = sysUserId;
    this.payType = payType;
    this.reqChannel = reqChannel;
    this.description = description;
    this.orderAmount = orderAmount;
    this.cashAmount = cashAmount;
    this.vmAmount = vmAmount;
    this.backAmount = backAmount;
    this.couponAmount = couponAmount;
    this.couponcodeId = couponcodeId;
    this.bargainAmount = bargainAmount;
    this.bargainPublishid = bargainPublishid;
    this.refundAmount = refundAmount;
    this.outTradeNo = outTradeNo;
    this.orderType = orderType;
  }




  private static final long serialVersionUID = 1L;
  //待支付订单
  public static final String STATES_INIT = "1";
  //支付成功订单
  public static final String STATES_SUCCESS = "2";
  //取消订单
  public static final String STATES_CANCEL = "3";
  //退款订单
  public static final String STATES_BACKMONEY = "4";

  //支付类型
  public static final String PAYTYPE_ALIPAY = "1";//支付宝支付
  public static final String PAYTYPE_WECHATPAY = "2";//微信支付
  public static final String PAYTYPE_BACKGIVEPAY = "3";//后台赠送
  public static final String PAYTYPE_MONEYPAY = "4";//余额支付
  public static final String PAYTYPE_COURSECARD = "5";//课程卡类型
  public static final String PAYTYPE_AGENT_OPEN_PAY = "6";//代理商开通订单

  //订单类型
  public static final String ORDERTYPE_COURSE = "1";
    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /**
     * 订单总金额
     */
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal sumMoney;
    /**
     * 订单状态 success已支付 init未支付  cancel已取消
     */
    @ApiModelProperty(value = "订单状态 success已支付 init未支付  cancel已取消")
    private String states;
    /**
     * 订单创建时间
     */
    @ApiModelProperty(value = "订单创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 订单支付时间
     */
    @ApiModelProperty(value = "订单支付时间")
    private Date payTime;
    /**
     * 审核用户id
     */
    @ApiModelProperty(value = "审核用户id")
    private Integer sysUserId;
    /**
     * 支付类型
     */
    @ApiModelProperty(value = "支付类型")
    private String payType;
    /**
     * 请求渠道(web,app)
     */
    @ApiModelProperty(value = "请求渠道(web,app)")
    private String reqChannel;
    /**
     * 备用描述
     */
    @ApiModelProperty(value = "备用描述")
    private String description;
    /**
     * 订单原始金额
     */
    @ApiModelProperty(value = "订单原始金额")
    @JsonSerialize(using = SerializerBigDecimal.class)
    private BigDecimal orderAmount;
    /**
     * 实际支付的cash金额
     */
    @ApiModelProperty(value = "实际支付的cash金额")
    @JsonSerialize(using = SerializerBigDecimal.class)
    private BigDecimal cashAmount;
    /**
     * 实际支付的vm金额
     */
    @ApiModelProperty(value = "实际支付的vm金额")
    @JsonSerialize(using = SerializerBigDecimal.class)
    private BigDecimal vmAmount;
    /**
     * 实际支付的返现金额
     */
    @ApiModelProperty(value = "实际支付的返现金额")
    @JsonSerialize(using = SerializerBigDecimal.class)
    private BigDecimal backAmount;
    /**
     * 优惠券金额
     */
    @ApiModelProperty(value = "优惠券金额")
    @JsonSerialize(using = SerializerBigDecimal.class)
    private BigDecimal couponAmount;
    /**
     * 优惠券编码id
     */
    @ApiModelProperty(value = "优惠券编码id")
    private Integer couponcodeId;
    /**
     * 砍价金额
     */
    @ApiModelProperty(value = "砍价金额")
    @TableField("bargainAmount")
    @JsonSerialize(using = SerializerBigDecimal.class)
  private BigDecimal bargainAmount;
    /**
     * 用户砍价id
     */
    @ApiModelProperty(value = "用户砍价id")
    @TableField("bargain_publishId")
  private Integer bargainPublishid;
    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    @JsonSerialize(using = SerializerBigDecimal.class)
    private BigDecimal refundAmount;
    /**
     * 第三方支付商户订单号
     */
    @ApiModelProperty(value = "第三方支付商户订单号")
    private String outTradeNo;
    /**
     * 订单类型（COURSE课程、MEMBER会员、ACCOUNT账户充值）
     */
    @ApiModelProperty(value = "订单类型（COURSE课程、MEMBER会员、ACCOUNT账户充值）")
    private String orderType;


}
