
package org.springcrazy.modules.web.controller;

import cn.hutool.http.HttpUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springcrazy.common.tool.BajiayunUtil;
import org.springcrazy.common.tool.ProfileConfig;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.jackson.JsonUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.web.vo.VideoUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *  控制器
 *
 * @author TongZhou
 * @since 2020-05-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/web/baijia")
@Api(value = "百家云相关配置", tags = "百家云相关配置")
public class BaijiaYunController extends CrazyController {

    private CrazyProperties crazyProperties;

    /**
     * 详情
     */
    @GetMapping("/getAccountInfo")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "")
    public R detail() {
        VideoUser videoUser = new VideoUser();
        videoUser.setExpireTime("--");
        videoUser.setUsedMediaLength("--");
        videoUser.setUsableMediaLength("--");
        videoUser.setUsedFlow("--");
        videoUser.setUsableFlow("--");
        videoUser.setUsableStorage("--");
        videoUser.setUsedStorage("--");
        videoUser.setStatus("--");
        videoUser.setLargeClassStatus("--");
        videoUser.setVideoStatus("--");
        Map<String, String> prop = crazyProperties.getProp();
        String videoyunDomain = prop.get("videoyun-domain");
        Map<String,String> map = ProfileConfig.getConfig(WebsiteProfile.BAIJIA_VOD);
        String roomMsg = HttpUtil.get(videoyunDomain + "/front/video/videouser?userKey="+map.get("key"));
        Map<String, Object> stringObjectMap = JsonUtil.toMap(roomMsg);
        if(stringObjectMap.get("code").equals(200)){
            Map data = (Map) stringObjectMap.get("data");
            Long mediaLength = Func.toLong(data.get("mediaLength") + "");
            Long totalMediaLength = Func.toLong(data.get("totalMediaLength") + "");
            Long dayMediaLength = Func.toLong(data.get("dayMediaLength") + "");

            Long totalStorage = Func.toLong(data.get("totalStorage") + "");
            Long usedStorage = Func.toLong(data.get("usedStorage") + "");
            Long totalFlowLimit = Func.toLong(data.get("totalFlowLimit") + "");
            Long usedFlow = Func.toLong(data.get("usedFlow") + "");

            String expireTime = data.get("expireTime") + "";
            String status = data.get("status") + "";

            String largeClassStatus = data.get("largeClassStatus") + "";
            String videoStatus = data.get("videoStatus") + "";


            videoUser.setExpireTime(expireTime);
            videoUser.setUsableMediaLength(totalMediaLength - mediaLength - dayMediaLength + "");
            videoUser.setUsedMediaLength(mediaLength + dayMediaLength + "");
            videoUser.setUsedFlow(usedFlow+ "");
            videoUser.setUsableFlow(totalFlowLimit - usedFlow + "");
            videoUser.setUsableStorage(totalStorage - usedStorage + "");
            videoUser.setUsedStorage(usedStorage + "");
            videoUser.setStatus(status);
            videoUser.setVideoStatus(videoStatus);
            videoUser.setLargeClassStatus(largeClassStatus);
            return R.data(videoUser);
        }
        return R.data(1,videoUser,"无数据");
    }

    /**
     * 获取视频/音频上传地址
     */
    @GetMapping("/getUploadUrl")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "")
    public R getUploadUrl(String filename) {
        return R.data(BajiayunUtil.getUploadUrl(filename));
    }


    /**
     * 获取断点续传地址
     */
    @GetMapping("/getResumeUploadUrl")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "")
    public R getResumeUploadUrl(String videoId) {
        return R.data(BajiayunUtil.getResumeUploadUrl(videoId));
    }
}
