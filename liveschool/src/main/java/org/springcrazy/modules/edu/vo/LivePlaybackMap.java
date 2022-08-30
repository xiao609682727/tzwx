
package org.springcrazy.modules.edu.vo;

import lombok.Data;

/**
 * 直播回放管理视图实体类
 *
 * @author TongZhou
 * @since 2020-11-10
 */
@Data
public class LivePlaybackMap {
	private static final long serialVersionUID = 1L;

	private String room_id;
	private String session_id;
	private String version;
	private String video_id;
	private String status;
	private String preface_url;
	private String total_transcode_size;
	private String total_size;
	private String length;
	private String file_md5;
	private String now_definition;
	private String origin_definition;
	private String qid;
	private String timestamp;
	private String sign;
}
