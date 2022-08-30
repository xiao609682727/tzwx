
package org.springcrazy.modules.agent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 代理商账户流水记录实体类
 *
 * @author TongZhou
 * @since 2021-04-30
 */
@Data
@TableName("edu_agent_user_account_history")
@ApiModel(value = "AgentUserAccountHistory对象", description = "代理商账户流水记录")
public class AgentUserAccountHistory implements Serializable {

    private static final long serialVersionUID = 1L;
  //交易类型：充值类型记录
  public static final String ACE_HISTORYTYPE_ADDMONEY = "add";
  //交易类型：扣款类型记录
  public static final String ACE_HISTORYTYPE_SUBTRACTMONEY = "subtraction";
  //交易类型：消费类型记录
  public static final String ACE_HISTORYTYPE_CONSUMPTION = "consumption";

  //业务类型
  // 系统订单 system
  public static final String BIZTYPE_SYSTEM = "system";
  //  课程订单 course
  public static final String BIZTYPE_COURSE = "course";


  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 代理商用户id
     */
    @ApiModelProperty(value = "代理商用户id")
    private Integer agentUserId;
    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Integer orderId;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 相关ID
     */
    @ApiModelProperty(value = "相关ID")
    private Integer otherId;
    /**
     * 当前余额
     */
    @ApiModelProperty(value = "当前余额")
    private BigDecimal balance;
    /**
     * cash余额
     */
    @ApiModelProperty(value = "cash余额")
    private BigDecimal cashAmount;
    /**
     * vm余额
     */
    @ApiModelProperty(value = "vm余额")
    private BigDecimal vmAmount;

    /**
     * 交易金额
     */
    @ApiModelProperty(value = "交易金额")
    private BigDecimal trxAmount;
  /**
   * 描述
   */
  private String description;
    /**
     * 账号历史类型 充值(add),扣款(subtraction),消费(consumption)
     */
    @ApiModelProperty(value = "账号历史类型 充值(add),扣款(subtraction),消费(consumption)")
    private String actHistoryType;
    /**
     * 业务类型:(系统订单 system,课程订单 course)
     */
    @ApiModelProperty(value = "业务类型:(系统订单 system,课程订单 course)")
    private String bizType;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 是否已删除
     */
    @ApiModelProperty(value = "是否已删除")
    private Integer isDeleted;


}
