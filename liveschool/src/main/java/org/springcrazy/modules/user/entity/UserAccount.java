
package org.springcrazy.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springcrazy.core.json.DataDeserializerBigDecimal;
import org.springcrazy.core.json.SerializerBigDecimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户信息账户信息，记录用户的账户金额实体类
 *
 * @author TongZhou
 * @since 2020-05-07
 */
@Data
@TableName("edu_user_account")
@ApiModel(value = "UserAccount对象", description = "账户信息账户信息，记录用户的账户金额")
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户ID
     */
    @ApiModelProperty(value = "账户ID")
    @TableId(value = "id", type = IdType.AUTO)
  protected Integer id;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    protected Integer userId;
    /**
     * 账户余额(CASH+VM的和)
     */
    @ApiModelProperty(value = "账户余额(CASH+VM的和)")
    @JsonDeserialize(using = DataDeserializerBigDecimal.class)
    @JsonSerialize(using = SerializerBigDecimal.class)
    protected BigDecimal balance;
    /**
     * 冻结金额
     */
    @ApiModelProperty(value = "冻结金额")
    @JsonDeserialize(using = DataDeserializerBigDecimal.class)
    @JsonSerialize(using = SerializerBigDecimal.class)
    protected BigDecimal forzenAmount;
    /**
     * 银行、支付充值到此字段
     */
    @ApiModelProperty(value = "银行、支付充值到此字段")
    @JsonDeserialize(using = DataDeserializerBigDecimal.class)
    @JsonSerialize(using = SerializerBigDecimal.class)
    protected BigDecimal cashAmount;
    /**
     * 充值卡充值到此字段
     */
    @ApiModelProperty(value = "充值卡充值到此字段")
    @JsonDeserialize(using = DataDeserializerBigDecimal.class)
    @JsonSerialize(using = SerializerBigDecimal.class)
    protected BigDecimal vmAmount;
    /**
     * 分销返现充值到此字段
     */
    @ApiModelProperty(value = "分销返现充值到此字段")
    @JsonDeserialize(using = DataDeserializerBigDecimal.class)
    @JsonSerialize(using = SerializerBigDecimal.class)
    protected BigDecimal backAmount;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "最后更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;


}
