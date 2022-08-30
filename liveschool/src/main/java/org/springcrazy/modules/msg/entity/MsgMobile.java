
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
 * 手机短信发送记录实体类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Data
@TableName("edu_msg_mobile")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MsgMobile对象", description = "手机短信发送记录")
public class MsgMobile extends BaseEntity {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
  private String mobile;
  private String content;
    /**
     * 1 正常 2 定时
     */
    @ApiModelProperty(value = "1 正常 2 定时")
    private String type;
    /**
     * ALL 所有学员 COURSE  按课程专业 EXAM按考试专业 CONTENT按内容 STUDENT按学员
     */
    @ApiModelProperty(value = "ALL 所有学员 COURSE  按课程专业 EXAM按考试专业 CONTENT按内容 STUDENT按学员")
    private String sendRange;

    private String templateCode;

    private String signName;

  /**
   * 业务id
   */
  @ApiModelProperty(value = "业务id")
  protected String ids;
}
