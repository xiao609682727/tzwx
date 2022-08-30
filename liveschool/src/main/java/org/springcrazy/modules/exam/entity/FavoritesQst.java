
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
 * 考试试题收藏表实体类
 *
 * @author TongZhou
 * @since 2020-12-17
 */
@Data
@TableName("exam_favorites_qst")
@ApiModel(value = "FavoritesQst对象", description = "考试试题收藏表")
public class FavoritesQst implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 试题id
     */
    @ApiModelProperty(value = "试题id")
    private Integer qstId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime addDate;


}
