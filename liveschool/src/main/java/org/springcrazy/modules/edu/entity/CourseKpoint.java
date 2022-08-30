
package org.springcrazy.modules.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 知识点的基本信息实体类
 *
 * @author TongZhou
 * @since 2020-04-25
 */
@Data
@TableName("edu_course_kpoint")
@ApiModel(value = "CourseKpoint对象", description = "知识点的基本信息")
public class CourseKpoint implements Serializable {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 课程id
     */
    @ApiModelProperty(value = "课程id")
    private Integer courseId;
    /**
     * 节点名称
     */
    @ApiModelProperty(value = "节点名称")
    private String name;
    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id")
    private Integer parentId;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;
    /**
     * 显示排序
     */
    @ApiModelProperty(value = "显示排序")
    private Integer sort;
    /**
     * 播放次数
     */
    @ApiModelProperty(value = "播放次数")
    private Integer playCount;
    /**
     * 是否可以试听1免费2收费
     */
    @ApiModelProperty(value = "是否可以试听1免费2收费")
    private Integer isFree;
    /**
     * 视频地址
     */
    @ApiModelProperty(value = "视频地址")
    private String videoUrl;
    /**
     * 回放地址
     */
    @ApiModelProperty(value = "回放地址")
    @TableField("replayUrl")
  private String replayUrl;
    /**
     * 讲师id
     */
    @ApiModelProperty(value = "讲师id")
    private Integer teacherId;
    /**
     * 播放时间
     */
    @ApiModelProperty(value = "播放时间")
    private String playTime;
    /**
     * 节点类型 0文件目录 1视频
     */
    @ApiModelProperty(value = "节点类型 0文件目录 1视频")
    private Integer kpointType;
    /**
     * 视频类型
     */
    @ApiModelProperty(value = "视频类型")
    private String videoType;
    /**
     * video视频
     */
    @ApiModelProperty(value = "video视频 ")
    private String fileType;
    /**
     * 文本
     */
    @ApiModelProperty(value = "文本")
    private String content;
    /**
     * 页数
     */
    @ApiModelProperty(value = "页数")
    private Integer pageCount;
    /**
     * 直播开始时间
     */
    @ApiModelProperty(value = "直播开始时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime liveBeginTime;
    /**
     * 直播结束时间
     */
    @ApiModelProperty(value = "直播结束时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime liveEndTime;
    /**
     * 进入直播 （百家云讲师邀请码）
     */
    @ApiModelProperty(value = "进入直播 （百家云讲师邀请码）")
    @TableField("intoLiveRoom")
  private String intoLiveRoom;
    /**
     * 直播供应商
     */
    @ApiModelProperty(value = "直播供应商")
    private String supplier;
    /**
     * 打开方式(WEB网页APP客户端)
     */
    @ApiModelProperty(value = "打开方式(WEB网页APP客户端)")
    @TableField("openType")
  private String openType;
    /**
     * 是否自动回放1是，2否
     */
    @ApiModelProperty(value = "是否自动回放1是，2否")
    @TableField("autoReplay")
  private Integer autoReplay;

  /**
   * 开启回放1是，2否
   */
  @ApiModelProperty(value = "开启回放")
  private Integer openReplay;


    /**
     * 视频时长
     */
    @ApiModelProperty(value = "视频时长")
    @TableField("videoTime")
    private String videoTime;

    /**
     * 学习完成状态(0未学习 1 学习中 2学习完成)
     */
    @TableField(exist = false)
    private String complete;

}
