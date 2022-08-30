
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
 * 代理商账户信息账户信息，记录用户的代理商账户金额实体类
 *
 * @author TongZhou
 * @since 2021-04-30
 */
@Data
@TableName("edu_agent_account")
@ApiModel(value = "AgentAccount对象", description = "代理商账户信息账户信息，记录用户的代理商账户金额")
public class AgentAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户ID
     */
    @ApiModelProperty(value = "账户ID")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 代理商用户ID
     */
    @ApiModelProperty(value = "代理商用户ID")
    private Integer agentUserId;
    /**
     * 代理商账户余额
     */
    @ApiModelProperty(value = "代理商账户余额")
    private BigDecimal balance;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "最后更新时间")
    private LocalDateTime updateTime;


}
