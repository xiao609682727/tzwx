
package org.springcrazy.modules.edu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 直播回放管理实体类
 *
 * @author TongZhou
 * @since 2020-11-10
 */
@Data
@TableName("edu_live_playback")
@ApiModel(value = "LivePlayback对象", description = "直播回放管理")
public class LivePlayback implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "编号")
    private Integer id;
    /**
     * 房间号
     */
    @ApiModelProperty(value = "房间号")
    private String liveRoomId;
    /**
     * 回放名称
     */
    @ApiModelProperty(value = "回放名称")
    private String name;
    /**
     * 视频id
     */
    @ApiModelProperty(value = "视频id")
    private String videoId;
    /**
     * 视频时长
     */
    @ApiModelProperty(value = "视频时长")
    private String videoDuration;
    /**
     * 回放视频封面地址
     */
    @ApiModelProperty(value = "回放视频封面地址")
    private String prefaceUrl;
    /**
     * 回放视频大小
     */
    @ApiModelProperty(value = "回放视频大小")
    private String totalSize;
    /**
     * 回放状态码 0:回放未生成 10:回放生成中 20:转码中 30:回放生成失败 100:回放生成成功
     */
    @ApiModelProperty(value = "回放状态码 0:回放未生成 10:回放生成中 20:转码中 30:回放生成失败 100:回放生成成功")
    private String status;
    /**
     * 默认状态 1 是  2不是
     */
    @ApiModelProperty(value = "默认状态 1 是  2不是")
    private String defaultStatus;
    /**
     * 回放时间
     */
    @ApiModelProperty(value = "回放时间")
    private LocalDateTime createTime;


}
