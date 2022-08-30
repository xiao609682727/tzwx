
package org.springcrazy.modules.quartz.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 定时任务日志实体类
 *
 * @author TongZhou
 * @since 2020-05-11
 */
@Data
@TableName("crazy_quartz_log")
@ApiModel(value = "QuartzLog对象", description = "定时任务日志")
public class QuartzLog implements Serializable {

    private static final long serialVersionUID = 1L;
    //0：成功 1：失败
    public static final String SUCCESS = "0";
    public static final String FAIL = "1";

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
  private String beanName;
  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
  private String cronExpression;
  private String exceptionDetail;
  private String isSuccess;
  private String jobName;
  private String methodName;
  private String params;
  private Long time;


}
