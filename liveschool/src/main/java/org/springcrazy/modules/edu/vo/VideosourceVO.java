
package org.springcrazy.modules.edu.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.edu.entity.Videosource;

import java.util.Map;

/**
 * 录播视频表视图实体类
 *
 * @author TongZhou
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "VideosourceVO对象", description = "录播视频表")
public class VideosourceVO extends Videosource {
	private static final long serialVersionUID = 1L;

	//阿里云用
	private Map<String,String> params;

}
