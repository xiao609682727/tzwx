
package org.springcrazy.modules.partyLogin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户第三方绑定实体类
 *
 * @author TongZhou
 * @since 2021-03-15
 */
@Data
@TableName("edu_user_profile")
@ApiModel(value = "UserProfile对象", description = "用户第三方绑定")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String name;
    /**
     * 第三方唯一key
     */
    @ApiModelProperty(value = "第三方唯一key")
    private String openid;
    /**
     * 第三方多应用的唯一key
     */
    @ApiModelProperty(value = "第三方多应用的唯一key")
    private String unionid;
    /**
     * 第三方类型qqPC(qqPC登录),qqApp(qqAPP登录),wxPc(微信PC登录),wxH5(微信H5登录),wxApp(微信APP登录),weibo(微博登录)
     */
    @ApiModelProperty(value = "第三方类型qqPC(qqPC登录),qqApp(qqAPP登录),wxPC(微信PC登录),wxH5(微信H5登录),wxApp(微信APP登录),weibo(微博登录)")
    private String profiletype;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userid;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private LocalDateTime profiledate;


}
