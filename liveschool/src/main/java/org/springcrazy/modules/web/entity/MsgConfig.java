
package org.springcrazy.modules.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@TableName("web_msg_config")
@ApiModel(value = "MsgConfig对象", description = "MsgConfig对象")
public class MsgConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String CODE_LOGIN = "code_login";
    public static final String CODE_REGISTER = "code_register";
    public static final String ACTIVE_EMAIL = "active_email";
    public static final String RETRIEVE_PASSWORD = "retrieve_password";

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 消息的唯一标识
     */
    @ApiModelProperty(value = "消息的唯一标识")
    private String code;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 消息类型
     */
    @ApiModelProperty(value = "消息类型")
    private String msgType;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
    /**
     * 短信签名
     */
    @ApiModelProperty(value = "短信签名")
    private String smsSign;
    /**
     * 短信模板
     */
    @ApiModelProperty(value = "短信模板")
    private String smsTemplate;


}
