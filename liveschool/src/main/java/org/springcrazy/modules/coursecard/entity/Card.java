
package org.springcrazy.modules.coursecard.entity;

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
 * 课程卡主表实体类
 *
 * @author TongZhou
 * @since 2021-03-31
 */
@Data
@TableName("edu_card")
@ApiModel(value = "Card对象", description = "课程卡主表")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 卡名称
     */
    @ApiModelProperty(value = "卡名称")
    private String name;
    /**
     * 卡金额
     */
    @ApiModelProperty(value = "卡金额")
    private BigDecimal money;
    /**
     * 卡类型(1课程卡2学员卡3充值卡)
     */
    @ApiModelProperty(value = "卡类型(1课程卡2学员卡)")
    private Integer type;
    /**
     * 卡数量
     */
    @ApiModelProperty(value = "卡数量")
    private Integer num;
    /**
     * 有效期开始时间
     */
    @ApiModelProperty(value = "有效期开始时间")
    private LocalDateTime beginTime;
    /**
     * 有效期结束时间
     */
    @ApiModelProperty(value = "有效期结束时间")
    private LocalDateTime endTime;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 1正常 2已过期
     */
    @ApiModelProperty(value = "1正常 2已过期")
    private Integer status;


}
