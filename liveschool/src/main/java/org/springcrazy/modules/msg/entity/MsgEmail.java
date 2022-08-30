
package org.springcrazy.modules.msg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.mp.base.BaseEntity;

/**
 * 邮件发送记录实体类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Data
@TableName("edu_msg_email")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MsgEmail对象", description = "邮件发送记录")
public class MsgEmail extends BaseEntity {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
  private String email;
    /**
     * 邮箱标题
     */
    @ApiModelProperty(value = "邮箱标题")
    private String title;
    /**
     * 邮箱正文
     */
    @ApiModelProperty(value = "邮箱正文")
    private String content;
    /**
     * 1 普通 2 定时
     */
    @ApiModelProperty(value = "1 普通 2 定时")
    private String type;
    /**
     * ALL 所有学员 COURSE  按课程专业 EXAM按考试专业 CONTENT按内容 STUDENT按学员
     */
    @ApiModelProperty(value = "ALL 所有学员 COURSE  按课程专业 EXAM按考试专业 CONTENT按内容 STUDENT按学员")
    private String sendRange;
  /**
   * 业务id
   */
  @ApiModelProperty(value = "业务id")
  private String ids;

}
