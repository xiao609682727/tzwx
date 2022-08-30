
package org.springcrazy.modules.cms.controller;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Maps;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.xkcoding.justauth.autoconfigure.JustAuthProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import org.springcrazy.common.tool.BajiayunUtil;
import org.springcrazy.common.tool.ProfileConfig;
import org.springcrazy.core.boot.ctrl.CrazyController;
import org.springcrazy.core.boot.file.CrazyFile;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.core.mp.support.Query;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.constant.SystemConstant;
import org.springcrazy.core.tool.jackson.JsonUtil;
import org.springcrazy.core.tool.utils.FileUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.cms.service.IWebsiteProfileService;
import org.springcrazy.modules.cms.vo.WebsiteProfileVO;
import org.springcrazy.modules.pay.config.PayConfig;
import org.springcrazy.modules.pay.entity.AliPayBean;
import org.springcrazy.modules.pay.entity.WxPayBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 系统配置表 控制器
 *
 * @author TongZhou
 * @since 2020-03-04
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cms/websiteprofile/")
@Api(value = "系统配置表", tags = "系统配置表接口")
@Slf4j
public class WebsiteProfileController extends CrazyController {

	private IWebsiteProfileService websiteProfileService;
	private PayConfig payConfig;
	private CrazyProperties crazyProperties;
	private final WxMpService wxService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入websiteProfile")
	public R<WebsiteProfile> detail(WebsiteProfile websiteProfile) {
		WebsiteProfile detail = websiteProfileService.getOne(Condition.getQueryWrapper(websiteProfile).lambda().orderByDesc(WebsiteProfile::getSort));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询系统配置列表", notes = "传入websiteProfile")
	public R<List<WebsiteProfile>> list(WebsiteProfile websiteProfile) {

		List<WebsiteProfile> list = websiteProfileService.list(Condition.getQueryWrapper(websiteProfile).lambda().orderByDesc(WebsiteProfile::getSort));
		return R.data(list);
	}

	/**
	 * 自定义分页 系统配置表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入websiteProfile")
	public R<IPage<WebsiteProfileVO>> page(WebsiteProfileVO websiteProfile, Query query) {
		IPage<WebsiteProfileVO> pages = websiteProfileService.selectWebsiteProfilePage(Condition.getPage(query), websiteProfile);
		return R.data(pages);
	}

	/**
	 * 新增 系统配置表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入websiteProfile")
	public R save(@Valid @RequestBody Map<String,String> params) {
		String configType = params.get("configType");
		params.remove("configType");

		//更新百家云信息
		if(Func.equals(configType,"baijiayun_vod")&&Func.isNotEmpty(params.get("key"))){
			Map<String, String> prop = crazyProperties.getProp();
			String videoyunDomain = prop.get("videoyun-domain");
			String roomMsg = HttpUtil.get(videoyunDomain + "/front/video/videouser?userKey="+params.get("key"));
			Map<String, Object> stringObjectMap = JsonUtil.toMap(roomMsg);
			if(stringObjectMap.get("code").equals(200)){
				Map data = (Map) stringObjectMap.get("data");
				String partnerKey = data.get("partnerKey") + "";
				String domain = data.get("domain") + "";
				String partnerId = data.get("partnerId") + "";
				String secretKey = data.get("secretKey") + "";
				Map<String,String> baijia = Maps.newHashMap();
				baijia.put("PartnerId",partnerId);
				baijia.put("SecretKey",secretKey);
				baijia.put("PartnerKey",partnerKey);
				baijia.put("URL",domain);
				baijia.forEach((k,v) ->{
					WebsiteProfile websiteProfile = new WebsiteProfile();
					websiteProfile.setDataValue(v);
					UpdateWrapper updateWrapper = new UpdateWrapper();
					updateWrapper.eq("config_type",WebsiteProfile.BAIJIA_VIDEO);
					updateWrapper.eq("data_key",k);
					websiteProfileService.update(websiteProfile, updateWrapper);
				});
			}
			log.info("接口返回："+roomMsg);
		}

		params.forEach((k,v) ->{
			WebsiteProfile websiteProfile = new WebsiteProfile();
			websiteProfile.setDataValue(v);
			UpdateWrapper updateWrapper = new UpdateWrapper();
			updateWrapper.eq("config_type",configType);
			updateWrapper.eq("data_key",k);
			websiteProfileService.update(websiteProfile, updateWrapper);
		});

		//更新支付宝微信信息
		//清除缓存
		clearCache(configType);
		//设置百家回调
		if(Func.equals(configType,"baijiayun_vod")){
			BajiayunUtil.setTranscodeCallbackUrl();
		}

		return R.success("操作成功");
	}

	/**
	 * 通用上传请求
	 */
	@PostMapping("/uploadIcon")
	public R uploadIcon(MultipartFile file){

		SystemConstant me = SystemConstant.me();
		//获取文件名判断文件是否支持上传
		if(me.getFileExt().size() > 0){
			if(!me.getFileExt().contains(FileUtil.getFileExtension(file.getOriginalFilename()))){
				return R.fail("不支持该文件类型上传");
			}
		}

		String frontPath = crazyProperties.get("front-icon-path");
		String adminPath = crazyProperties.get("admin-icon-path");
		CrazyFile crazyFile = getFile(file,"ico");
		crazyFile.transfer();
		try {
			FileUtil.copy(new File(crazyFile.getUploadPath()),new File(frontPath));
			FileUtil.copy(new File(crazyFile.getUploadPath()),new File(adminPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//上传文件
		Map<String,String> result = Maps.newHashMap();
		result.put("url", me.getDomain() + crazyFile.getUploadVirtualPath());
		return R.data(result);
	}



	public void clearCache(String configType){
		ProfileConfig.clearMap();
		if(Func.equals("alipay",configType)){
			AliPayBean aliPayBean = SpringUtil.getBean(AliPayBean.class);

			Map<String,String> map = ProfileConfig.getConfig(WebsiteProfile.ALIPAY);
			aliPayBean.setAppId(map.get("appId"));;
			aliPayBean.setPrivateKey(map.get("privateKey"));
			aliPayBean.setPublicKey(map.get("publicKey"));

			AliPayApiConfig aliPayApiConfig = SpringUtil.getBean(AliPayApiConfig.class);
			aliPayApiConfig = AliPayApiConfig.builder()
					.setAppId(aliPayBean.getAppId())
					.setAliPayPublicKey(aliPayBean.getPublicKey())
//                    .setAppCertPath(aliPayBean.getAppCertPath())
//                    .setAliPayCertPath(aliPayBean.getAliPayCertPath())
//                    .setAliPayRootCertPath(aliPayBean.getAliPayRootCertPath())
					.setCharset("UTF-8")
					.setPrivateKey(aliPayBean.getPrivateKey())
					.setServiceUrl(aliPayBean.getServerUrl())
					.setSignType("RSA2")
					// 普通公钥方式
					.build();
			// 证书模式
//                    .buildByCert();
			AliPayApiConfigKit.putApiConfig(aliPayApiConfig);
			AliPayApiConfigKit.setThreadLocalAppId(aliPayBean.getAppId());
		}
		if(Func.equals("wechatpay",configType)){
//			WxPayBean wxPayBean = SpringUtil.getBean(WxPayBean.class);
//			WxPayApiConfigKit.removeApiConfig(wxPayBean.getAppId());
//			Map<String,String> map = ProfileConfig.getConfig(WebsiteProfile.WECHATPAY);
//			wxPayBean.setAppId(map.get("appId"));;
//			wxPayBean.setAppSecret(map.get("AppSecret"));
//			wxPayBean.setMchId(map.get("wxMchId"));
//			wxPayBean.setPartnerKey(map.get("privateKey"));
//
//			WxPayApiConfig apiConfig = SpringUtil.getBean(WxPayApiConfig.class);;
//
//			apiConfig = WxPayApiConfig.builder()
//					.appId(wxPayBean.getAppId())
//					.mchId(wxPayBean.getMchId())
//					.partnerKey(wxPayBean.getPartnerKey())
//					.certPath(wxPayBean.getCertPath())
//					.domain(wxPayBean.getDomain())
//					.build();
//
//			WxPayApiConfigKit.putApiConfig(apiConfig);
//			WxPayApiConfigKit.setThreadLocalAppId(wxPayBean.getAppId());
		}
		//更新快捷登录信息
		JustAuthProperties justAuthProperties = SpringUtil.getBean(JustAuthProperties.class);
		Map<AuthDefaultSource, AuthConfig> type = justAuthProperties.getType();
		CrazyProperties crazyProperties = SpringUtil.getBean(CrazyProperties.class);
		String domain = crazyProperties.get("domain");
		if(Func.equals(configType,"qqlogin")){
			AuthConfig authConfig = type.get(AuthDefaultSource.QQ);
			Map<String, String> qqLogin = ProfileConfig.getConfig("qqlogin");
			authConfig.setClientId(qqLogin.get("qqClientId"));
			authConfig.setClientSecret(qqLogin.get("qqClientSecret"));
			authConfig.setRedirectUri(domain + "/front/oauth/qq/callback");
			justAuthProperties.setType(type);
		}
		if(Func.equals(configType,"weibologin")){
			AuthConfig authConfig = type.get(AuthDefaultSource.WEIBO);
			Map<String, String> weiboLogin = ProfileConfig.getConfig("weibologin");
			authConfig.setClientId(weiboLogin.get("weiboClientId"));
			authConfig.setClientSecret(weiboLogin.get("weiboClientSecret"));
			authConfig.setRedirectUri(domain + "/front/oauth/weibo/callback");
			justAuthProperties.setType(type);
		}
		if(Func.equals(configType,"wechatlogin")){
			AuthConfig authConfig = type.get(AuthDefaultSource.WECHAT_OPEN);
			Map<String, String> wechatLogin = ProfileConfig.getConfig("wechatlogin");
			authConfig.setClientId(wechatLogin.get("wechatClientId"));
			authConfig.setClientSecret(wechatLogin.get("wechatClientSecret"));
			authConfig.setRedirectUri(domain + "/front/oauth/wechat_open/callback");
			justAuthProperties.setType(type);
		}
		if(Func.equals(configType,WebsiteProfile.WECHATMP)){
			Map<String, String> config = ProfileConfig.getConfig(WebsiteProfile.WECHATMP);
			WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
			configStorage.setAppId(config.get("AppID"));
			configStorage.setSecret(config.get("AppSecret"));
			Map<String, WxMpConfigStorage> map = Maps.newHashMap();
			map.put(config.get("AppID"),configStorage);
			wxService.setMultiConfigStorages(map);
		}


	}

	/**
	 * 修改 系统配置表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入websiteProfile")
	public R update(@Valid @RequestBody WebsiteProfile websiteProfile) {
		return R.status(websiteProfileService.updateById(websiteProfile));
	}

	/**
	 * 新增或修改 系统配置表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入websiteProfile")
	public R submit(@Valid @RequestBody WebsiteProfile websiteProfile) {
		return R.status(websiteProfileService.saveOrUpdate(websiteProfile));
	}


	/**
	 * 删除 系统配置表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(websiteProfileService.removeByIds(Func.toIntList(ids)));
	}


	/**
	 * 上传app的包
	 */
	@PostMapping("/uploadAppPack")
	@ApiOperation(value = "上传app的包", notes = "上传app的包")
	public R uploadAppPack(MultipartFile file){

		SystemConstant me = SystemConstant.me();

		String fileName = file.getOriginalFilename();
		CrazyFile crazyFile = getFile(file,"appPack");
		crazyFile.transfer();
		//上传文件
		Map<String,String> result = Maps.newHashMap();
		result.put("url", me.getDomain() + crazyFile.getUploadVirtualPath());
		result.put("fileName" , fileName);
		return R.data(result);
	}

}
