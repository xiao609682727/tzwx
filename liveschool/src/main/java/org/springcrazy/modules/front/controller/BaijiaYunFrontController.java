
package org.springcrazy.modules.front.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springcrazy.common.tool.BajiayunUtil;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *  控制器
 *
 * @author TongZhou
 * @since 2020-05-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/front/web/baijia")
@Api(value = "百家云相关配置", tags = "百家云相关配置")
public class BaijiaYunFrontController extends CrazyController {


	/**
	 * 详情
	 */
	@GetMapping("/getAccountInfo")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "")
	public R detail() {
		return R.data(BajiayunUtil.getAccountInfo("83562889"));
	}

	/**
	 * 获取视频/音频上传地址
	 */
	@GetMapping("/getUploadUrl")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "")
	public R getUploadUrl(String filename, HttpServletRequest request) {
		String scheme = request.getHeader("X-Forwarded-Scheme");
		Map uploadUrl = BajiayunUtil.getUploadUrl(filename);
		String upload_url = uploadUrl.get("upload_url") + "";
		System.out.println(scheme);
		if(ObjectUtil.isNotNull(scheme)){
			uploadUrl.put("upload_url",upload_url.replace("http",scheme));
		}else {
			uploadUrl.put("upload_url",upload_url.replace("http",request.getScheme()));
		}
		return R.data(uploadUrl);
	}


	/**
	 * 获取断点续传地址
	 */
	@GetMapping("/getResumeUploadUrl")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "")
	public R getResumeUploadUrl(String videoId,HttpServletRequest request) {
		String scheme = request.getHeader("X-Forwarded-Scheme");
		Map resumeUploadUrl = BajiayunUtil.getResumeUploadUrl(videoId);
		String upload_url = resumeUploadUrl.get("upload_url") + "";
		if(ObjectUtil.isNotNull(scheme)){
			resumeUploadUrl.put("upload_url",upload_url.replace("http",scheme));
		}else {
			resumeUploadUrl.put("upload_url",upload_url.replace("http",request.getScheme()));
		}
		return R.data(resumeUploadUrl);
	}
}
