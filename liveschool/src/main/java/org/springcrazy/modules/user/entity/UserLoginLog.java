
package org.springcrazy.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类
 *
 * @author TongZhou
 * @since 2020-05-06
 */
@Data
@TableName("edu_user_login_log")
@ApiModel(value = "UserLoginLog对象", description = "UserLoginLog对象")
public class UserLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 登录时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "登录时间")
    private Date loginTime;
    /**
     * 登录ip
     */
    @ApiModelProperty(value = "登录ip")
    private String ip;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 操作系统
     */
    @ApiModelProperty(value = "操作系统")
    private String osName;
    /**
     * 浏览器
     */
    @ApiModelProperty(value = "浏览器")
    private String userAgent;


}
