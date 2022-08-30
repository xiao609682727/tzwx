
package org.springcrazy.modules.edu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.mp.base.BaseEntity;

/**
 * 录播视频表实体类
 *
 * @author TongZhou
 * @since 2020-06-29
 */
@Data
@TableName("edu_videosource")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Videosource对象", description = "录播视频表")
public class Videosource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 视频id
     */
    @ApiModelProperty(value = "视频id")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 视频名称
     */
    @ApiModelProperty(value = "视频名称")
    private String name;
    /**
     * 视频原片大小
     */
    @ApiModelProperty(value = "视频原片大小")
    private String videoSize;
    /**
     * 转码状态 (init：待转码 progress：转码中 finish：转码成功 error：转码失败 delete : 已回收)
     */
    @ApiModelProperty(value = "转码状态 (init：待转码 progress：转码中 finish：转码成功 error：转码失败 delete : 已回收)")
    private String videoStatus;
    /**
     * 转码后大小
     */
    @ApiModelProperty(value = "转码后大小")
    private String videoLength;
    /**
     * 视频播放码
     */
    @ApiModelProperty(value = "视频播放码")
    private String idVarchar;
    /**
     * 视频时长（s）
     */
    @ApiModelProperty(value = "视频时长（s）")
    private Integer videoDuration;
    /**
     * 封面图片地址
     */
    @ApiModelProperty(value = "封面图片地址")
    private String imageUrl;
    /**
     * 播放文件类型 1：视频 2：音频
     */
    @ApiModelProperty(value = "播放文件类型 1：视频 2：音频")
    private Integer fileType;
    /**
     * 源文件类型 1：视频 2：音频
     */
    @ApiModelProperty(value = "源文件类型 1：视频 2：音频")
    private Integer initType;
    /**
     * 视频类型(ninetySixkoo96刻....)
     */
    @ApiModelProperty(value = "视频类型(ninetySixkoo96刻....)")
    private String videoType;

}
