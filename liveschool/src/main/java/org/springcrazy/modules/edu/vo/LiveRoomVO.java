
package org.springcrazy.modules.edu.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.edu.entity.LiveRoom;

import java.util.Map;

/**
 * 直播房间视图实体类
 *
 * @author TongZhou
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LiveRoomVO对象", description = "直播房间")
public class LiveRoomVO extends LiveRoom {
	private static final long serialVersionUID = 1L;

	private Map<String,Object> params;

}
