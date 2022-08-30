
package org.springcrazy.modules.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2020-03-01
 */
@Data
@TableName("cms_comment")
@ApiModel(value = "Comment对象", description = "Comment对象")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论表
     */
    @ApiModelProperty(value = "评论表")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 父级评论id(为0则是一级评论,不为0则是回复)
     */
    @ApiModelProperty(value = "父级评论id(为0则是一级评论,不为0则是回复)")
    private Integer parentId;
    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    private String content;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date addtime;
    /**
     * 评论的相关id
     */
    @ApiModelProperty(value = "评论的相关id")
    private Integer otherId;
    /**
     * 点赞数量
     */
    @ApiModelProperty(value = "点赞数量")
    private Integer praiseCount;
    /**
     * 回复数量
     */
    @ApiModelProperty(value = "回复数量")
    private Integer replyCount;
    /**
     * 1文章 2课程 3直播 4播放大厅章节评论 5即时聊天(Socket) 7试卷 8练习题库 9闯关 10考试套餐 11班级
     */
    @ApiModelProperty(value = "1文章 2课程 3直播 4播放大厅章节评论 5即时聊天(Socket) 7试卷 8练习题库 9闯关 10考试套餐 11班级")
    private Integer type;
  @TableField("roleType")
  private String roleType;
    /**
     * 评分
     */
    @ApiModelProperty(value = "评分")
    @TableField("commentScore")
  private Integer commentScore;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    @TableField("commentSort")
  private Long commentSort;
    /**
     * 是否置顶，0不置顶，1置顶
     */
    @ApiModelProperty(value = "是否置顶，0不置顶，1置顶")
    @TableField("commentTop")
  private Integer commentTop;


}
