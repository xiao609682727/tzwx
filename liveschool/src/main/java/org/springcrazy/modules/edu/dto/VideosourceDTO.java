
package org.springcrazy.modules.edu.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.edu.entity.Videosource;

/**
 * 录播视频表数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VideosourceDTO extends Videosource {
	private static final long serialVersionUID = 1L;

	private String fileName;
}
