
package org.springcrazy.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.json.DataDeserializerBigDecimal;
import org.springcrazy.core.json.SerializerBigDecimal;
import org.springcrazy.core.mp.base.BaseEntity;

import java.math.BigDecimal;

/**
 * 账户流水记录实体类
 *
 * @author TongZhou
 * @since 2020-05-07
 */
@Data
@TableName("edu_user_account_history")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserAccountHistory对象", description = "账户流水记录")
public class UserAccountHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;
   //交易类型 1 充值类型
    public static final String HISTORYTYPE_ADDMONEY = "1";
    //2消费类型
    public static final String HISTORYTYPE_SUBTRACTMONEY = "2";

    //业务类型
  // 后台充值与消费
    public static final String BIZTYPE_SYSTEM = "1";
    //课程消费
  public static final String BIZTYPE_COURSE = "2";
  public static final String BIZTYPE_MONEY = "3";

  @TableId(value = "id", type = IdType.AUTO)
  protected Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    protected Integer userId;
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    protected String orderNo;
    /**
     * 相关ID
     */
    @ApiModelProperty(value = "相关ID")
    protected Integer otherId;
    /**
     * 当前余额
     */
    @ApiModelProperty(value = "当前余额")
    @JsonDeserialize(using = DataDeserializerBigDecimal.class)
    @JsonSerialize(using = SerializerBigDecimal.class)
    protected BigDecimal balance;
    /**
     * cash余额
     */
    @ApiModelProperty(value = "cash余额")
    @JsonDeserialize(using = DataDeserializerBigDecimal.class)
    @JsonSerialize(using = SerializerBigDecimal.class)
    protected BigDecimal cashAmount;
    /**
     * vm余额
     */
    @ApiModelProperty(value = "vm余额")
    @JsonDeserialize(using = DataDeserializerBigDecimal.class)
    @JsonSerialize(using = SerializerBigDecimal.class)
    protected BigDecimal vmAmount;
    /**
     * 分销返现余额
     */
    @ApiModelProperty(value = "分销返现余额")
    @JsonDeserialize(using = DataDeserializerBigDecimal.class)
    @JsonSerialize(using = SerializerBigDecimal.class)
    protected BigDecimal backAmount;
    /**
     * 交易金额
     */
    @ApiModelProperty(value = "交易金额")
    @JsonDeserialize(using = DataDeserializerBigDecimal.class)
    @JsonSerialize(using = SerializerBigDecimal.class)
    protected BigDecimal trxAmount;
  protected String description;
    /**
     * 帐务历史类型.充值。消费等
     */
    @ApiModelProperty(value = "帐务历史类型.充值。消费等")
    protected String actHistoryType;
    /**
     * 业务类型(课程订单、会员订单、图书订单)
     */
    @ApiModelProperty(value = "业务类型(课程订单、会员订单、图书订单)")
    protected String bizType;

    /**
     * 收款人姓名
     */
    @ApiModelProperty(value = "收款人姓名")
    protected String payee;
    /**
     * 转账银行
     */
    @ApiModelProperty(value = "转账银行")
    protected String bank;
    /**
     * 银行卡号
     */
    @ApiModelProperty(value = "银行卡号")
    protected String card;


}
