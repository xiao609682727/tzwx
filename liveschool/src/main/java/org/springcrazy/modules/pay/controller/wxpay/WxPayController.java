package org.springcrazy.modules.pay.controller.wxpay;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.IpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springcrazy.common.tool.BigDecimalUtils;
import org.springcrazy.common.tool.CourseUtils;
import org.springcrazy.common.tool.ProfileConfig;
import org.springcrazy.core.launch.props.CrazyProperties;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.cms.entity.WebsiteProfile;
import org.springcrazy.modules.cms.service.IWebsiteProfileService;
import org.springcrazy.modules.edu.entity.Course;
import org.springcrazy.modules.edu.entity.Orders;
import org.springcrazy.modules.edu.entity.TrxorderDetail;
import org.springcrazy.modules.edu.service.ICourseService;
import org.springcrazy.modules.edu.service.IOrdersService;
import org.springcrazy.modules.edu.service.ITrxorderDetailService;
import org.springcrazy.modules.lineclass.entity.LineclassEnroll;
import org.springcrazy.modules.lineclass.service.ILineclassEnrollService;
import org.springcrazy.modules.user.entity.UserAccountHistory;
import org.springcrazy.modules.user.service.IUserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>IJPay ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????</p>
 *
 * <p>???????????????????????? mvc ??????????????????????????????????????????????????????????????????????????????????????????????????????????????? </p>
 *
 * <p>IJPay ?????????: 723992875</p>
 *
 * <p>Node.js ???: https://gitee.com/javen205/TNWX</p>
 *
 * <p>???????????? Demo</p>
 *
 * @author Javen
 */
@Controller
@RequestMapping("/front/wxPay")
@AllArgsConstructor
@Api(value = "????????????", tags = "????????????")
public class WxPayController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private IOrdersService ordersService;
    private ITrxorderDetailService trxorderDetailService;
    private IWebsiteProfileService websiteProfileService;
    private IUserAccountService userAccountService;
    private ICourseService courseService;
    private ILineclassEnrollService lineclassEnrollService;

    ProfileConfig profileConfig;
    CrazyProperties crazyProperties;

    private static final String USER_PAYING = "USERPAYING";




    /**
     * ??????H5 ??????
     * ??????????????????web?????????????????????????????????????????????????????????
     */
    /*@RequestMapping(value = "/wapPay", method = {RequestMethod.POST, RequestMethod.GET})
    public void wapPay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ip = IpKit.getRealIp(request);
        if (Func.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        H5SceneInfo sceneInfo = new H5SceneInfo();

        H5SceneInfo.H5 h5_info = new H5SceneInfo.H5();
        h5_info.setType("Wap");
        //??????????????????????????????--"????????????"--"????????????"?????????
        h5_info.setWap_url("https://gitee.com/javen205/IJPay");
        h5_info.setWap_name("IJPay VIP ??????");
        sceneInfo.setH5Info(h5_info);

//        WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("IJPay ?????????????????????-H5??????")
                .attach("Node.js ???:https://gitee.com/javen205/TNWX")
                .out_trade_no(WxPayKit.generateStr())
                .total_fee("1000")
                .spbill_create_ip(ip)
                .notify_url(wxPayBean.getDomain().concat("/wxPay/payNotify"))
                .trade_type(TradeType.MWEB.getTradeType())
                .scene_info(JSON.toJSONString(sceneInfo))
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info(xmlResult);

        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(return_code)) {
            throw new RuntimeException(return_msg);
        }
        String result_code = result.get("result_code");
        if (!WxPayKit.codeIsOk(result_code)) {
            throw new RuntimeException(return_msg);
        }
        // ???????????????return_code ???result_code??????SUCCESS??????????????????

        String prepayId = result.get("prepay_id");
        String webUrl = result.get("mweb_url");

        log.info("prepay_id:" + prepayId + " mweb_url:" + webUrl);
        response.sendRedirect(webUrl);
    }*/

    /**
     * ???????????????
     */
    @GetMapping(value = "/webPay")
    @ResponseBody
    @ApiOperation("???????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "??????id"),
            @ApiImplicitParam(name = "openId", value = "????????????????????????openId")
    })
    public R webPay(HttpServletRequest request,@RequestParam("orderId")Integer orderId,@RequestParam String openId) {

        WxPayApiConfig wxPayApiConfig  = WxPayApiConfigKit.getWxPayApiConfig();


        // openId????????? ?????????????????? access_token API???SnsAccessTokenApi??????
        if (Func.isEmpty(orderId)) {
            return R.fail("??????id????????????");
        }
        Orders order = ordersService.getById(orderId);
        if (Func.isEmpty(order)) {
            return R.fail("?????????????????????");
        }
        if (StrUtil.isEmpty(openId)) {
            return R.fail("openId is null");
        }
        String ip = IpKit.getRealIp(request);
        if (StrUtil.isEmpty(ip)) {
            ip = "127.0.0.1";
        }

//        WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();
        //??????????????????
        List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<TrxorderDetail>().lambda().eq(TrxorderDetail::getTrxorderId, orderId));
        //??????????????????
        String bodyInfo = list.stream().map(trxorderDetail -> trxorderDetail.getCourseName()).collect(Collectors.joining(","));

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("???????????????"+order.getOrderNo())
                .attach(bodyInfo)
                .out_trade_no(order.getOutTradeNo())
                .total_fee(BigDecimalUtils.toInt(order.getSumMoney().multiply(new BigDecimal(100))))
                .spbill_create_ip(ip)
                .notify_url(crazyProperties.get("domain").concat("/front/wxPay/payNotify"))
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(openId)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info(xmlResult);

        Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
        String returnCode = resultMap.get("return_code");
        String returnMsg = resultMap.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            return R.fail(returnMsg);
        }
        String resultCode = resultMap.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            return R.fail(returnMsg);
        }

        // ??????????????? return_code ??? result_code ?????? SUCCESS ??????????????????

        String prepayId = resultMap.get("prepay_id");

        Map<String, String> packageParams = WxPayKit.prepayIdCreateSign(prepayId, wxPayApiConfig.getAppId(),
                wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        return R.data(packageParams);
    }

    /**
     * ???????????????
     */
    /*@RequestMapping(value = "/scanCode1", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public R scanCode1(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam("productId") String productId) {
        try {
            if (Func.isBlank(productId)) {
                return R.fail("productId is null");
            }
//            WxPayApiConfig config = PayConfig.getWxPayApiConfig();
            //?????????????????????????????????url
            String qrCodeUrl = WxPayKit.bizPayUrl(wxPayApiConfig.getPartnerKey(), wxPayApiConfig.getAppId(), wxPayApiConfig.getMchId(), productId);
            log.info(qrCodeUrl);
            //??????????????????????????????
            String name = "payQRCode1.png";
            log.info(ResourceUtils.getURL("classpath:").getPath());
            boolean encode = QrCodeKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H,
                    "png", 200, 200,
                    ResourceUtils.getURL("classpath:").getPath().concat("static").concat(File.separator).concat(name));
            if (encode) {
                //??????????????????
                return R.success(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("???????????????" + e.getMessage());
        }
        return null;
    }
*/
    /**
     * ???????????????????????????
     */
    /*@RequestMapping(value = "/scanCodeNotify", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String scanCodeNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            String result = HttpKit.readData(request);
            log.info("scanCodeNotify>>>" + result);
            *//**
     * ????????????????????????????????????????????????
     *//*
            Map<String, String> map = WxPayKit.xmlToMap(result);
            for (String key : map.keySet()) {
                log.info("key= " + key + " and value= " + map.get(key));
            }

            String appId = map.get("appid");
            String openId = map.get("openid");
            String mchId = map.get("mch_id");
            String isSubscribe = map.get("is_subscribe");
            String nonceStr = map.get("nonce_str");
            String productId = map.get("product_id");
            String sign = map.get("sign");

            Map<String, String> packageParams = new HashMap<String, String>(6);
            packageParams.put("appid", appId);
            packageParams.put("openid", openId);
            packageParams.put("mch_id", mchId);
            packageParams.put("is_subscribe", isSubscribe);
            packageParams.put("nonce_str", nonceStr);
            packageParams.put("product_id", productId);

//            WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

            String packageSign = WxPayKit.createSign(packageParams, wxPayApiConfig.getPartnerKey(), SignType.MD5);

            String ip = IpKit.getRealIp(request);
            if (Func.isBlank(ip)) {
                ip = "127.0.0.1";
            }
            Map<String, String> params = UnifiedOrderModel
                    .builder()
                    .appid(wxPayApiConfig.getAppId())
                    .mch_id(wxPayApiConfig.getMchId())
                    .nonce_str(WxPayKit.generateStr())
                    .body("IJPay ?????????????????????-?????????????????????")
                    .attach("Node.js ???:https://gitee.com/javen205/TNWX")
                    .out_trade_no(WxPayKit.generateStr())
                    .total_fee("1")
                    .spbill_create_ip(ip)
                    .notify_url(crazyProperties.get("domain").concat("/wxPay/payNotify"))
                    .trade_type(TradeType.NATIVE.getTradeType())
                    .openid(openId)
                    .build()
                    .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
            String xmlResult = WxPayApi.pushOrder(false, params);
            log.info("????????????:" + xmlResult);
            *//**
     * ??????????????????????????????
     *//*
            Map<String, String> payResult = WxPayKit.xmlToMap(xmlResult);
            String returnCode = payResult.get("return_code");
            String resultCode = payResult.get("result_code");
            if (WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode)) {
                // ??????????????? return_code ??? result_code ?????? SUCCESS ??????????????????
                String prepayId = payResult.get("prepay_id");

                Map<String, String> prepayParams = new HashMap<String, String>(10);
                prepayParams.put("return_code", "SUCCESS");
                prepayParams.put("appid", appId);
                prepayParams.put("mch_id", mchId);
                prepayParams.put("nonce_str", System.currentTimeMillis() + "");
                prepayParams.put("prepay_id", prepayId);
                String prepaySign;
                if (sign.equals(packageSign)) {
                    prepayParams.put("result_code", "SUCCESS");
                } else {
                    prepayParams.put("result_code", "FAIL");
                    //result_code???FAIL???????????????????????????value?????????????????????????????????
                    prepayParams.put("err_code_des", "????????????");
                }
                prepaySign = WxPayKit.createSign(prepayParams, wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
                prepayParams.put("sign", prepaySign);
                String xml = WxPayKit.toXml(prepayParams);
                log.error(xml);
                return xml;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ?????????????????????
     */
    @ApiOperation("??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "??????id"),
    })
    @GetMapping(value = "/scanCode2")
    @ResponseBody
    public R scanCode2(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam("orderId")Integer orderId) {
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        if (Func.isEmpty(orderId)) {
            return R.fail("??????id????????????");
        }

        String ip = IpKit.getRealIp(request);
        if (Func.isBlank(ip)) {
            ip = "127.0.0.1";
        }
//        WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

        Orders order = ordersService.getById(orderId);
        if (Func.isEmpty(order)) {
            return R.fail("?????????????????????");
        }
        //??????????????????
        List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<TrxorderDetail>().lambda().eq(TrxorderDetail::getTrxorderId, orderId));
        //??????????????????
        WebsiteProfile websiteProfile = new WebsiteProfile();
        websiteProfile.setConfigType("web");

        //??????????????????
        String bodyInfo = list.stream().map(trxorderDetail -> trxorderDetail.getCourseName()).collect(Collectors.joining(","));

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("???????????????"+order.getOrderNo())
                .attach(bodyInfo)
                .out_trade_no(order.getOutTradeNo())
                .total_fee(BigDecimalUtils.toInt(order.getSumMoney().multiply(new BigDecimal(100))))
                .spbill_create_ip(ip)
                .notify_url(crazyProperties.get("domain").concat("/front/wxPay/payNotify"))
                .trade_type(TradeType.NATIVE.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info("????????????:" + xmlResult);

        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        System.out.println(returnMsg);
        if (!WxPayKit.codeIsOk(returnCode)) {
            return R.fail("error:" + returnMsg);
        }
        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            return R.fail("error:" + returnMsg);
        }
        //??????????????????success

        String qrCodeUrl = result.get("code_url");
//        String name = "payQRCode-"+order.getOrderNo()+".png";

//        boolean encode = QrCodeKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
//                SystemConstant.me().getUploadPath() + File.separator + name);

        return R.data(qrCodeUrl);
    }

    /**
     * ????????????
     */
    /*@RequestMapping(value = "/micropay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public R microPay(HttpServletRequest request, HttpServletResponse response) {
        String authCode = request.getParameter("auth_code");
        String totalFee = request.getParameter("total_fee");
        if (Func.isBlank(totalFee)) {
            return R.fail("????????????????????????");
        }
        if (Func.isBlank(authCode)) {
            return R.fail("auth_code????????????");
        }
        String ip = IpKit.getRealIp(request);
        if (Func.isBlank(ip)) {
            ip = "127.0.0.1";
        }
//        WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

        Map<String, String> params = MicroPayModel.builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("IJPay ?????????????????????-????????????")
                .attach("Node.js ???:https://gitee.com/javen205/TNWXX")
                .out_trade_no(WxPayKit.generateStr())
                .total_fee("1")
                .spbill_create_ip(ip)
                .auth_code(authCode)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.microPay(false, params);
        //??????????????????
        log.info("xmlResult:" + xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            //????????????
            String errCode = result.get("err_code");
            if (Func.isNotBlank(errCode)) {
                //????????????????????????????????????
                if (USER_PAYING.equals(errCode)) {
                    //??????5???????????????????????????API???
                }
            }
            log.info("????????????????????????>>" + xmlResult);
            return R.fail(returnMsg);
        }

        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            log.info("????????????>>" + xmlResult);
            String errCodeDes = result.get("err_code_des");
            return R.fail(errCodeDes);
        }
        //????????????
        return R.success(xmlResult);
    }*/

    /**
     * ??????APP??????
     */
    @RequestMapping(value = "/appPay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public R appPay(HttpServletRequest request,Integer orderId,@RequestParam(defaultValue = "app") String clientType) {

        String ip = IpKit.getRealIp(request);
        if (Func.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        Orders order = ordersService.getById(orderId);

        Map<String,String> map = ProfileConfig.getConfig(WebsiteProfile.WECHATAPPPAY);

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getApiConfig(map.get("appId"));
        //??????????????????
        List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<TrxorderDetail>().lambda().eq(TrxorderDetail::getTrxorderId, orderId));
        //??????????????????
        String bodyInfo = list.stream().map(trxorderDetail -> trxorderDetail.getCourseName()).collect(Collectors.joining(","));


        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("???????????????"+order.getOrderNo())
                .attach(bodyInfo)
                .out_trade_no(order.getOutTradeNo())
                .total_fee(BigDecimalUtils.toInt(order.getSumMoney().multiply(new BigDecimal(100))))
                .spbill_create_ip(ip)
                .notify_url(crazyProperties.get("domain").concat("/front/wxPay/payNotify"))
                .trade_type(TradeType.APP.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        log.info(xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            return R.fail(returnMsg);
        }
        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            return R.fail(returnMsg);
        }
        // ??????????????? return_code ??? result_code ?????? SUCCESS ??????????????????
        String prepayId = result.get("prepay_id");

        Map<String, String> packageParams = WxPayKit.appPrepayIdCreateSign(wxPayApiConfig.getAppId(), wxPayApiConfig.getMchId(), prepayId,
                wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String jsonStr = JSON.toJSONString(packageParams);
        log.info("??????apk?????????:" + jsonStr);
        return R.data(packageParams);
    }
    /**
     * ?????????????????????
     */
   /* @RequestMapping(value = "/miniAppPay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public R miniAppPay(HttpServletRequest request) {
        //???????????????????????????openId
        String openId = (String) request.getSession().getAttribute("openId");

        String ip = IpKit.getRealIp(request);
        if (Func.isBlank(ip)) {
            ip = "127.0.0.1";
        }

//        WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("IJPay ?????????????????????-???????????????")
                .attach("Node.js ???:https://gitee.com/javen205/TNWXX")
                .out_trade_no(WxPayKit.generateStr())
                .total_fee("1000")
                .spbill_create_ip(ip)
                .notify_url(wxPayBean.getDomain().concat("/wxPay/payNotify"))
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(openId)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        log.info(xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            return R.fail(returnMsg);
        }
        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            return R.fail(returnMsg);
        }
        // ??????????????? return_code ??? result_code ?????? SUCCESS ??????????????????
        String prepayId = result.get("prepay_id");
        Map<String, String> packageParams = WxPayKit.miniAppPrepayIdCreateSign(wxPayApiConfig.getAppId(), prepayId,
                wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
        String jsonStr = JSON.toJSONString(packageParams);
        log.info("????????????????????????:" + jsonStr);
        return R.success(jsonStr);
    }*/

    /**
     * ?????????????????????
     */
    /*@RequestMapping(value = "/transfer", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String transfer(HttpServletRequest request, @RequestParam("openId") String openId) {

        String ip = IpKit.getRealIp(request);
        if (Func.isBlank(ip)) {
            ip = "127.0.0.1";
        }

//        WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

        Map<String, String> params = TransferModel.builder()
                .mch_appid(wxPayApiConfig.getAppId())
                .mchid(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .partner_trade_no(WxPayKit.generateStr())
                .openid(openId)
                .check_name("NO_CHECK")
                .amount("100")
                .desc("IJPay ?????????????????????-????????????")
                .spbill_create_ip(ip)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5, false);

        // ??????
        String transfers = WxPayApi.transfers(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
        log.info("????????????:" + transfers);
        Map<String, String> map = WxPayKit.xmlToMap(transfers);
        String returnCode = map.get("return_code");
        String resultCode = map.get("result_code");
        if (WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode)) {
            // ????????????
        } else {
            // ????????????
        }
        return transfers;
    }*/

    /**
     * ???????????????????????????
     */
    /*@RequestMapping(value = "/transferInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String transferInfo(@RequestParam("partner_trade_no") String partnerTradeNo) {
        try {
//            WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

            Map<String, String> params = GetTransferInfoModel.builder()
                    .nonce_str(WxPayKit.generateStr())
                    .partner_trade_no(partnerTradeNo)
                    .mch_id(wxPayApiConfig.getMchId())
                    .appid(wxPayApiConfig.getAppId())
                    .build()
                    .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5, false);

            return WxPayApi.getTransferInfo(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ??????RSA????????????
     */
   /* @RequestMapping(value = "/getPublicKey", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String getPublicKey() {
        try {
//            WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

            Map<String, String> params = new HashMap<String, String>(4);
            params.put("mch_id", wxPayApiConfig.getMchId());
            params.put("nonce_str", String.valueOf(System.currentTimeMillis()));
            params.put("sign_type", "MD5");
            String createSign = WxPayKit.createSign(params, wxPayApiConfig.getPartnerKey(), SignType.MD5);
            params.put("sign", createSign);
            return WxPayApi.getPublicKey(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ????????????????????????
     */
    /*@RequestMapping(value = "/payBank", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String payBank() {
        try {
//            WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

            //??????WxPayApi.getPublicKey????????????RSA????????????
            //??????????????????RSA???????????????PUBLIC_KEY(PKCS#8??????)
            final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6Bl76IwSvBTiibZ+CNRUA6BfahMshZ0WJpHD1GpmvcQjeN6Yrv6c9eIl6gB4nU3isN7bn+LmoVTpH1gHViaV2YyG/zXj4z4h7r+V+ezesMqqorEg38BCNUHNmhnw4/C0I4gBAQ4x0SJOGnfKGZKR9yzvbkJtvEn732JcEZCbdTZmaxkwlenXvM+mStcJaxBCB/h5xJ5VOF5nDbTPzLphIpzddr3zx/Jxjna9QB1v/YSKYXn+iuwruNUXGCvvxBWaBGKrjOdRTRy9adWOgNmtuYDQJ2YOfG8PtPe06ELKjmr2CfaAGrKKUroyaGvy3qxAV0PlT+UQ4ADSXWt/zl0o5wIDAQAB";

            Map<String, String> params = new HashMap<String, String>(10);
            params.put("mch_id", wxPayApiConfig.getMchId());
            params.put("partner_trade_no", System.currentTimeMillis() + "");
            params.put("nonce_str", System.currentTimeMillis() + "");
            //?????????????????????
            params.put("enc_bank_no", RsaKit.encryptByPublicKeyByWx("????????????", PUBLIC_KEY));
            //??????????????????
            params.put("enc_true_name", RsaKit.encryptByPublicKeyByWx("????????????????????????", PUBLIC_KEY));
            //??????????????????
            params.put("bank_code", "1001");
            params.put("amount", "1");
            params.put("desc", "IJPay ?????????????????????-??????????????????");
            params.put("sign", WxPayKit.createSign(params, wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256));
            return WxPayApi.payBank(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ???????????????????????????
     */
    /*@RequestMapping(value = "/queryBank", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String queryBank(@RequestParam("partner_trade_no") String partnerTradeNo) {
        try {
//            WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

            Map<String, String> params = new HashMap<String, String>(4);
            params.put("mch_id", wxPayApiConfig.getMchId());
            params.put("partner_trade_no", partnerTradeNo);
            params.put("nonce_str", System.currentTimeMillis() + "");
            params.put("sign", WxPayKit.createSign(params, wxPayApiConfig.getPartnerKey(), SignType.MD5));
            return WxPayApi.queryBank(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ????????????
     */
    /*@RequestMapping(value = "/refund", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String refund(@RequestParam("transactionId") String transactionId,
                         @RequestParam("out_trade_no") String outTradeNo) {

        if (Func.isBlank(outTradeNo) && Func.isBlank(transactionId)) {
            return "transactionId???out_trade_no?????????";
        }
//        WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

        Map<String, String> params = RefundModel.builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .transaction_id(transactionId)
                .out_trade_no(outTradeNo)
                .out_refund_no(WxPayKit.generateStr())
                .total_fee("1")
                .refund_fee("1")
                .notify_url(wxPayBean.getDomain().concat("/wxPay/refundNotify"))
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5);

        return WxPayApi.orderRefund(false, params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
    }*/

    /**
     * ??????????????????
     */
   /* @RequestMapping(value = "/refundQuery", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String refundQuery(@RequestParam("transactionId") String transactionId,
                              @RequestParam("out_trade_no") String outTradeNo,
                              @RequestParam("out_refund_no") String outRefundNo,
                              @RequestParam("refund_id") String refundId) {

//        WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

        Map<String, String> params = RefundQueryModel.builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .transaction_id(transactionId)
                .out_trade_no(outTradeNo)
                .out_refund_no(outRefundNo)
                .refund_id(refundId)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5);

        return WxPayApi.orderRefundQuery(false, params);
    }*/

    /**
     * ????????????
     */
   /* @RequestMapping(value = "/refundNotify", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String refundNotify(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        log.info("????????????=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

        String returnCode = params.get("return_code");
        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (WxPayKit.codeIsOk(returnCode)) {
            String reqInfo = params.get("req_info");
            String decryptData = WxPayKit.decryptData(reqInfo, wxPayApiConfig.getPartnerKey());
            log.info("??????????????????????????????=" + decryptData);
            // ??????????????????
            // ???????????????
            Map<String, String> xml = new HashMap<String, String>(2);
            xml.put("return_code", "SUCCESS");
            xml.put("return_msg", "OK");
            return WxPayKit.toXml(xml);
        }
        return null;
    }*/

    /*@RequestMapping(value = "/sendRedPack", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String sendRedPack(HttpServletRequest request, @RequestParam("openId") String openId) {
        try {
            String ip = IpKit.getRealIp(request);
            if (Func.isBlank(ip)) {
                ip = "127.0.0.1";
            }

//            WxPayApiConfig wxPayApiConfig = PayConfig.getWxPayApiConfig();

            Map<String, String> params = SendRedPackModel.builder()
                    .nonce_str(WxPayKit.generateStr())
                    .mch_billno(WxPayKit.generateStr())
                    .mch_id(wxPayApiConfig.getMchId())
                    .wxappid(wxPayApiConfig.getAppId())
                    .send_name("IJPay ????????????")
                    .re_openid(openId)
                    .total_amount("1000")
                    .total_num("1")
                    .wishing("??????????????? IJPay")
                    .client_ip(ip)
                    .act_name("??????????????????")
                    .remark("??? start ?????????????????????!")
                    .build()
                    .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5);
            String result = WxPayApi.sendRedPack(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
            System.out.println("??????????????????:" + result);
            Map<String, String> map = WxPayKit.xmlToMap(result);
            return JSON.toJSONString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ????????????
     */
    @ApiOperation("????????????????????????")
    @RequestMapping(value = "/payNotify", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String payNotify(HttpServletRequest request) {
        WxPayApiConfig wxPayApiConfig  = WxPayApiConfigKit.getWxPayApiConfig();
        String xmlMsg = HttpKit.readData(request);
        log.info("????????????=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

        String returnCode = params.get("return_code");

        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ???????????????????????????????????????????????????????????????
        if (WxPayKit.verifyNotify(params, wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // ??????????????????
                String outTradeNo = params.get("out_trade_no");
                Orders orders = ordersService.getOne(new QueryWrapper<Orders>().lambda().eq(Orders::getOutTradeNo, outTradeNo));
                if(Objects.nonNull(orders) && Func.equals(orders.getStates(),Orders.STATES_INIT)){
                    orders.setStates(Orders.STATES_SUCCESS);
                    orders.setPayTime(org.springcrazy.core.tool.utils.DateUtil.now());
                    orders.setPayType(Orders.PAYTYPE_WECHATPAY);
                    ordersService.updateOrder(orders);

                    if(Func.equals(orders.getOrderType(),"ACCOUNT")){
                        //?????????????????????
                        TrxorderDetail trxorderDetail = new TrxorderDetail();
                        List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<>(trxorderDetail));
                        list.forEach(l ->{
                            l.setPayTime(org.springcrazy.core.tool.utils.DateUtil.now());
                            l.setAuthStatus(TrxorderDetail.STATUS_SUCCESS);
                        });
                        trxorderDetailService.updateBatchById(list);
                        //????????????
                        userAccountService.money(orders.getUserId(),orders.getSumMoney(),"1", UserAccountHistory.BIZTYPE_MONEY,"????????????",orders.getOrderNo());
                    }
                    //????????????
                    if(Func.equals(orders.getOrderType(),"COURSE") || Func.equals(orders.getOrderType(),"LIVE") || Func.equals(orders.getOrderType(),"PACKAGE")){
                        //?????????????????????
                        TrxorderDetail trxorderDetail = new TrxorderDetail();
                        trxorderDetail.setTrxorderId(orders.getId());
                        List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<>(trxorderDetail));
                        list.forEach(l ->{
                            l.setPayTime(org.springcrazy.core.tool.utils.DateUtil.now());
                            l.setAuthStatus(TrxorderDetail.STATUS_SUCCESS);
                            Course course = new Course();
                            course.setLoseTime(l.getLoseTime());
                            course.setLosetype(l.getLosetype());
                            course.setEndTime(l.getLoseAbsTime());
                            l.setAuthTime(CourseUtils.getAuthTime(course));
                            //???????????????
                            Course tcourse = courseService.getById(l.getCourseId());
                            QueryWrapper<TrxorderDetail> queryWrapper = new QueryWrapper<TrxorderDetail>();
                            queryWrapper.lambda().eq(TrxorderDetail::getCourseId,l.getCourseId()).eq(TrxorderDetail::getAuthStatus,TrxorderDetail.STATUS_SUCCESS);
                            int buyCount = trxorderDetailService.count(queryWrapper);
                            tcourse.setPageBuycount(buyCount);
                            courseService.updateById(tcourse);
                            if("LINECLASS".equals(l.getTrxorderType())){
                                LineclassEnroll lineclassEnroll = new LineclassEnroll();
                                lineclassEnroll.setUserId(l.getUserId());
                                lineclassEnroll.setCourseId(l.getCourseId());
                                lineclassEnroll.setTrxorderId(l.getTrxorderId());
                                lineclassEnrollService.updateLineClassEnrol(lineclassEnroll);
                            }
                        });
                        trxorderDetailService.updateBatchById(list);
                    }
                }
                // ???????????????
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayKit.toXml(xml);
            }
        }
        return null;
    }
}
