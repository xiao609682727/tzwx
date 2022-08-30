
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
 * 系统消息实体类
 *
 * @author TongZhou
 * @since 2020-05-18
 */
@Data
@TableName("edu_msg_system")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MsgSystem对象", description = "系统消息")
public class MsgSystem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final String ALLUSER = "1";
    public static final String USERS = "2";

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
  protected Integer id;
    /**
     * 信内容
     */
    @ApiModelProperty(value = "信内容")
    protected String content;
    /**
     * 0 所有学员 1 课程专业学员 2 按学员
     */
    @ApiModelProperty(value = "0 所有学员 1 课程专业学员 2 按学员")
    protected String sendRange;

    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private String ids;
}
