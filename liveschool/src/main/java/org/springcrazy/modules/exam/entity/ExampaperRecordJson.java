
package org.springcrazy.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 *
 * @author TongZhou
 * @since 2021-01-05
 */
@Data
@TableName("exam_exampaper_record_json")
@ApiModel(value = "ExampaperRecordJson对象", description = "ExampaperRecordJson对象")
public class ExampaperRecordJson implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 考试记录id
     */
    @ApiModelProperty(value = "考试记录id")
    private Integer examRecordId;
    /**
     * 考试答题记录json
     */
    @ApiModelProperty(value = "考试答题记录json")
    private String analysisJson;


}
