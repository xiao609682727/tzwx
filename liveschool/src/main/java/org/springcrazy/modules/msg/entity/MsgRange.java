
package org.springcrazy.modules.msg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息范围 一对多中间表实体类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Data
@TableName("edu_msg_range")
@ApiModel(value = "MsgRange对象", description = "消息范围 一对多中间表")
public class MsgRange implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 消息ID
     */
    @ApiModelProperty(value = "消息ID")
    private Integer msgId;
    /**
     * 范围ID(专业ID、内容ID)
     */
    @ApiModelProperty(value = "范围ID(专业ID、内容ID)")
    private Integer rangeId;
    /**
     * 类型 课程course 试卷exam
     */
    @ApiModelProperty(value = "类型 课程course 试卷exam")
    private String type;
    /**
     * 消息类型 系统消息sysmsg 手机短信phonemsg 邮件emailemsg
     */
    @ApiModelProperty(value = "消息类型 系统消息sysmsg 手机短信phonemsg 邮件emailemsg")
    private String msgType;


}
