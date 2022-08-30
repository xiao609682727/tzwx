
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
 * 试题纠错表实体类
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@Data
@TableName("exam_error_check")
@ApiModel(value = "ErrorCheck对象", description = "试题纠错表")
public class ErrorCheck implements Serializable {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 试卷id
     */
    @ApiModelProperty(value = "试卷id")
    private Integer paperid;
    /**
     * 试题id
     */
    @ApiModelProperty(value = "试题id")
    private Integer questionid;
    /**
     * 纠错内容
     */
    @ApiModelProperty(value = "纠错内容")
    private String content;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private LocalDateTime addtime;


}
