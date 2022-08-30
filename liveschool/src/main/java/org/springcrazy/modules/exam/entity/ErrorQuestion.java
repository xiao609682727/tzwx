
package org.springcrazy.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 错题记录表实体类
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@Data
@TableName("exam_error_question")
@ApiModel(value = "ErrorQuestion对象", description = "错题记录表")
public class ErrorQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 外键关联试卷
     */
    @ApiModelProperty(value = "外键关联试卷")
    private Integer paperId;
    /**
     * 试题id
     */
    @ApiModelProperty(value = "试题id")
    private Integer qstId;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private LocalDateTime addtime;
    /**
     * 试卷记录表的id
     */
    @ApiModelProperty(value = "试卷记录表的id")
    private Integer paperRecordId;

    /**
     * 做错次数
     */
    @ApiModelProperty(value = "做错次数")
    private Integer errorCount;

    /**
     * 累计作答
     */
    @ApiModelProperty(value = "累计作答")
    private Integer count;


}
