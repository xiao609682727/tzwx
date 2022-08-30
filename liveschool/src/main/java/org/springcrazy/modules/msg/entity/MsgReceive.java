
package org.springcrazy.modules.msg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 站内信实体类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Data
@TableName("edu_msg_receive")
@ApiModel(value = "MsgReceive对象", description = "站内信")
public class MsgReceive implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String SYSTEMMSG = "1";


    public static final String STATUS_NOREAD = "0";
    public static final String STATUS_READ = "1";

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private Date createTime;
    /**
     * 发信人用户id
     */
    @ApiModelProperty(value = "发信人用户id")
    private Integer cusId;
    /**
     * 信内容
     */
    @ApiModelProperty(value = "信内容")
    private String content;
    /**
     * 类型1系统通知2站内信5课程过期6优惠券过期
     */
    @ApiModelProperty(value = "类型1系统通知2站内信5课程过期6优惠券过期")
    private String type;
    /**
     * 0未读1已读2接受3拒绝4黑名单
     */
    @ApiModelProperty(value = "0未读1已读2接受3拒绝4黑名单")
    private String status;
    /**
     * 收信人id
     */
    @ApiModelProperty(value = "收信人id")
    private Integer receivingCusid;
    /**
     * 会员名
     */
    @ApiModelProperty(value = "会员名")
    private String showname;
    /**
     * 如果是系统通知 ALL 所有学员 COURSE 课程专业学员 EXAM 考试专业学员 发送范围(CONTENT 学习内容的学员) STUDENT 按学员
     */
    @ApiModelProperty(value = "如果是系统通知 ALL 所有学员 COURSE 课程专业学员 EXAM 考试专业学员 发送范围(CONTENT 学习内容的学员) STUDENT 按学员")
    private String sendRange;
    /**
     * 学习内容类别(COURSE课程EXAM考试CLASS班级)
     */
    @ApiModelProperty(value = "学习内容类别(COURSE课程EXAM考试CLASS班级)")
    private String contentType;
    /**
     * 学习内容ID
     */
    @ApiModelProperty(value = "学习内容ID")
    private Integer contentId;


}
