
package org.springcrazy.modules.lineclass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 线下课报名记录表实体类
 *
 * @author TongZhou
 * @since 2021-05-27
 */
@Data
@TableName("edu_lineclass_enroll")
@ApiModel(value = "LineclassEnroll对象", description = "线下课报名记录表")
public class LineclassEnroll implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 流水id
     */
    @ApiModelProperty(value = "流水id")
    private Integer trxorderId;
    /**
     * 报名用户姓名
     */
    @ApiModelProperty(value = "报名用户姓名")
    private String userName;
    /**
     * 报名用户手机号
     */
    @ApiModelProperty(value = "报名用户手机号")
    private String mobile;
    /**
     * 课程id
     */
    @ApiModelProperty(value = "课程id")
    private Integer courseId;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private LocalDateTime createTime;
    /**
     *  未支付，2 支付成功，3 后台赠送未报名
     */
    @ApiModelProperty(value = " 未支付，2 支付成功，3 后台赠送未报名")
    private Integer state;


}
