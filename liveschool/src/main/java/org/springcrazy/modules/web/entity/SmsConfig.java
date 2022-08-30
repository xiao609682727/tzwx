
package org.springcrazy.modules.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类
 *
 * @author TongZhou
 * @since 2020-05-09
 */
@Data
@TableName("web_sms_config")
@ApiModel(value = "SmsConfig对象", description = "SmsConfig对象")
public class SmsConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 1 签名   2模板
     */
    @ApiModelProperty(value = "1 签名   2模板")
    private String type;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
    /**
     * 模板id，或签名id
     */
    @ApiModelProperty(value = "模板id，或签名id")
    private String otherId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
