
package org.springcrazy.modules.edu.wrapper;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.common.tool.BajiayunUtil;
import org.springcrazy.common.tool.MsgVodFactory;
import org.springcrazy.core.log.exception.ServiceException;
import org.springcrazy.core.mp.support.BaseEntityWrapper;
import org.springcrazy.core.tool.utils.BeanUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.vod.AliVodUtil;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.edu.entity.Videosource;
import org.springcrazy.modules.edu.vo.VideosourceVO;

import java.util.Map;

/**
 * 录播视频表包装类,返回视图层所需的字段
 *
 * @author TongZhou
 * @since 2020-06-29
 */
@Slf4j
public class VideosourceWrapper extends BaseEntityWrapper<Videosource, VideosourceVO>  {

    public static VideosourceWrapper build() {
        return new VideosourceWrapper();
    }

	@Override
	public VideosourceVO entityVO(Videosource videosource) {
		VideosourceVO videosourceVO = BeanUtil.copy(videosource, VideosourceVO.class);
		//阿里云点播
		if(Func.equals(videosourceVO.getVideoType(), WebsiteProfile.ALIYUN)){
			DefaultAcsClient client = MsgVodFactory.getMsgSender(WebsiteProfile.ALIYUN);
			GetVideoPlayAuthResponse response = AliVodUtil.getVideoPlayAuth(client, videosourceVO.getIdVarchar());
			if (Func.notNull(response)) {
				Map<String,String> params = Maps.newHashMap();
				params.put("playAuth",response.getPlayAuth());
				videosourceVO.setParams(params);
			}
		}
		//百家云云点播
		if(Func.equals(videosourceVO.getVideoType(), "2")){
			String token = null;
			try {
				token = BajiayunUtil.getToken(videosourceVO.getIdVarchar());
				Map<String,String> params = Maps.newHashMap();
				params.put("token",token);
				videosourceVO.setParams(params);
			} catch (ServiceException e) {
				e.printStackTrace();
				log.error("百家云云点播",e);
			}
		}
		return videosourceVO;
	}

}
