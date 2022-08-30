
package org.springcrazy.modules.coursecard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课程卡编码表实体类
 *
 * @author TongZhou
 * @since 2021-04-02
 */
@Data
@TableName("edu_card_code")
@ApiModel(value = "CardCode对象", description = "课程卡编码表")
public class CardCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * card_id
     */
    @ApiModelProperty(value = "card_id")
    private Integer cardId;
    /**
     * 卡编码
     */
    @ApiModelProperty(value = "卡编码")
    private String cardCode;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String cardCodePassword;
    /**
     * 课程卡init 未使用,used 已经使用,overdue 已过期
     */
    @ApiModelProperty(value = "课程卡init 未使用,used 已经使用,overdue 已过期")
    private String status;
    /**
     * 用户email
     */
    @ApiModelProperty(value = "用户email")
    private String userEmail;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 订单主键
     */
    @ApiModelProperty(value = "订单id")
    private Integer trxorderId;
    /**
     * 订单requestid
     */
    @ApiModelProperty(value = "订单号")
    private String requestId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 使用时间
     */
    @ApiModelProperty(value = "使用时间")
    private LocalDateTime useTime;


}
