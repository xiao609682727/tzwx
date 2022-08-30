
package org.springcrazy.modules.edu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.mp.base.BaseEntity;

import java.time.LocalDateTime;

/**
 * 直播房间实体类
 *
 * @author TongZhou
 * @since 2020-11-09
 */
@Data
@TableName("edu_live_room")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LiveRoom对象", description = "直播房间")
public class LiveRoom extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 房间id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "房间id")
    private Integer id;
    /**
     * 房间名称
     */
    @ApiModelProperty(value = "房间名称")
    private String name;
    /**
     * 教室id
     */
    @ApiModelProperty(value = "教室id")
    private String classroomId;
    @ApiModelProperty(value = "直播类型")
    private String liveType;
    /**
     * 房间类型2 普通大班课
     */
    @ApiModelProperty(value = "房间类型2 普通大班课")
    private String roomType;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
    /**
     * 课程状态
     */
    @ApiModelProperty(value = "课程状态")
    private String roomStatus;
    /**
     * 回放状态
     */
    @ApiModelProperty(value = "回放状态")
    private String playbackStatus;

    /**
     * 讲师参加码
     */
    @ApiModelProperty(value = "讲师参加码")
    private String teacherCode;
    /**
     * 助教参加码
     */
    @ApiModelProperty(value = "助教参加码")
    private String tutorCode;
    /**
     * 学生参加码
     */
    @ApiModelProperty(value = "学员参加码")
    private String studentCode;
    /**
     * 自动录制
     */
    @ApiModelProperty(value = "自动录制")
    private String autoPlaybackRecord;
    /**
     * 指定PC端是否以视频为主 1:以视频为主 2:以PPT为主 （默认是以ppt为主，该选项只针对三分屏有效）
     */
    @ApiModelProperty(value = "指定PC端是否以视频为主 1:以视频为主 2:以PPT为主 （默认是以ppt为主，该选项只针对三分屏有效）")
    private String isVideoMain;
    /**
     * 指定手机H5页面是否以视频为主 1:以视频为主 2:以PPT为主 （默认是以视频为主）
     */
    @ApiModelProperty(value = "指定手机H5页面是否以视频为主 1:以视频为主 2:以PPT为主 （默认是以视频为主）")
    private String mobileIsVideoMain;
    /**
     * 课程预设的结束时间后可以拖堂的时间，到时间会强制下课，单位（秒），0不强制，大于0生效,最大不可超过7200秒（两小时）
     */
    @ApiModelProperty(value = "课程预设的结束时间后可以拖堂的时间，到时间会强制下课，单位（秒），0不强制，大于0生效,最大不可超过7200秒（两小时）")
    private String endDelayTime;


}
