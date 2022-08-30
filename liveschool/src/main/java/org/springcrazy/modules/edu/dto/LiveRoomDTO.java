
package org.springcrazy.modules.edu.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springcrazy.modules.edu.entity.LiveRoom;

/**
 * 直播房间数据传输对象实体类
 *
 * @author TongZhou
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LiveRoomDTO extends LiveRoom {
	private static final long serialVersionUID = 1L;

}
