
package org.springcrazy.modules.quartz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.core.mp.base.BaseEntity;

/**
 * 定时任务实体类
 *
 * @author TongZhou
 * @since 2020-05-11
 */
@Data
@TableName("crazy_quartz_job")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "QuartzJob对象", description = "定时任务")
public class QuartzJob extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final String JOB_KEY = "JOB_KEY";

  public static final String START = "0";
  public static final String PAUSE = "1";

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * Spring Bean名称
     */
    @ApiModelProperty(value = "Spring Bean名称")
    private String beanName;
    /**
     * cron 表达式
     */
    @ApiModelProperty(value = "cron 表达式")
    private String cronExpression;
    /**
     * 状态：1暂停、0启用
     */
    @ApiModelProperty(value = "状态：1暂停、0启用")
    private String isPause;
    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String jobName;
    /**
     * 方法名称
     */
    @ApiModelProperty(value = "方法名称")
    private String methodName;
    /**
     * 参数
     */
    @ApiModelProperty(value = "参数")
    private String params;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String description;
    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人")
    private String personInCharge;
    /**
     * 报警邮箱
     */
    @ApiModelProperty(value = "报警邮箱")
    private String email;
    /**
     * 子任务ID
     */
    @ApiModelProperty(value = "子任务ID")
    private String subTask;
    /**
     * 任务失败后是否暂停
     */
    @ApiModelProperty(value = "任务失败后是否暂停")
    private String pauseAfterFailure;

}
