
package org.springcrazy.modules.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类
 *
 * @author TongZhou
 * @since 2020-05-19
 */
@Data
@TableName("web_stat_data")
@ApiModel(value = "StatData对象", description = "StatData对象")
public class StatData implements Serializable {

    public static final String AREADATA = "areadata";
    public static final String STATADMINDATA = "statadmindata";
    public static final String STATADMINUSERDATA = "statadminuserdata";

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 统计数据编码
     */
    @ApiModelProperty(value = "统计数据编码")
    private String code;
    /**
     * 统计的json结果
     */
    @ApiModelProperty(value = "统计的json结果")
    private String data;
    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    private String day;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
