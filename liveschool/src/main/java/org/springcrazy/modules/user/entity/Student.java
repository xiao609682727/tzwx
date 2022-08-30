
package org.springcrazy.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 学员表实体类
 *
 * @author TongZhou
 * @since 2020-04-28
 */
@Data
@TableName("edu_student")
@ApiModel(value = "Student对象", description = "学员表")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学员id
     */
    @ApiModelProperty(value = "学员id")
    @TableId(value = "id", type = IdType.AUTO)
  protected Integer id;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    protected String mobile;
    /**
     * 邮箱号
     */
    @ApiModelProperty(value = "邮箱号")
    protected String email;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    protected String password;
    /**
     * 账户名
     */
    @ApiModelProperty(value = "账户名")
    protected String userName;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    protected String showName;
    /**
     * 性别  1男  2女
     */
    @ApiModelProperty(value = "性别  1男  2女")
    protected Integer sex;
    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    protected Integer age;
    /**
     * 注册时间
     */
    @ApiModelProperty(value = "注册时间")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;
    /**
     * 是否可用 1正常  2冻结
     */
    @ApiModelProperty(value = "是否可用 2正常  1冻结")
    protected Integer isAvalible;
    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    protected String headImg;
    /**
     * 个人中心用户背景图片
     */
    @ApiModelProperty(value = "个人中心用户背景图片")
    protected String bannerUrl;
    /**
     * 站内信未读消息数
     */
    @ApiModelProperty(value = "站内信未读消息数")
    protected Integer msgNum;
    /**
     * 系统自动消息未读消息数
     */
    @ApiModelProperty(value = "系统自动消息未读消息数")
    protected Integer sysMsgNum;
    /**
     * 上传统计系统消息时间
     */
    @ApiModelProperty(value = "上传统计系统消息时间")
    protected LocalDateTime lastSystemTime;
    /**
     * 登录账号
     */
    @ApiModelProperty(value = "登录账号")
    protected String loginAccount;
    /**
     * 注册来源 register_from	1(pc网站) /2(h5注册) /3(app) /4(后台管理单独) 5( 5后台管理批量) 6 (代理商开通)
     */
    @ApiModelProperty(value = "注册来源 register_from 1(pc网站) /2(h5注册) /3(app) /4(后台管理单独开通) 5( 后台管理批量开通) 6 (代理商开通)")
    protected String registerFrom;
    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码")
    @TableField("invitationCode")
    protected String invitationCode;
    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    @TableField("realName")
    protected String realName;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    @TableField("idCardNo")
    protected String idCardNo;
    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    protected Integer province;
    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    protected Integer city;
    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    protected Integer area;
    /**
     * 学校
     */
    @ApiModelProperty(value = "学校")
    protected String school;
    /**
     * 专业
     */
    @ApiModelProperty(value = "专业")
    protected String subject;
    /**
     * 签名简介
     */
    @ApiModelProperty(value = "签名简介")
    @TableField("userInfo")
    protected String userInfo;
    /**
     * 代理商id
     */
    @ApiModelProperty(value = "代理商id")
    @TableField("agent_id")
    protected Integer agentId;


    private String weibo;

    private String wechat;

    private String qq;

  private String weiboname;

  private String wechatname;

  private String qqname;

  private String openId;
  private String wechatName;

  @TableLogic
  @ApiModelProperty("是否已删除")
  private Integer isDeleted;

  /**
   * 赠送课程list
   */
  @ApiModelProperty(value = "赠送课程list")
  @TableField(exist = false)
  private List<Integer> courseIdList;
}
