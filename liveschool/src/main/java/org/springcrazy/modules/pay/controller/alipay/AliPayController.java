package org.springcrazy.modules.pay.controller.alipay;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.RsaKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.common.tool.BigDecimalUtils;
import org.springcrazy.common.tool.CommonUtil;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>IJPay ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????</p>
 *
 * <p>???????????????????????? mvc ??????????????????????????????????????????????????????????????????????????????????????????????????????????????? </p>
 *
 * <p>IJPay ?????????: 723992875</p>
 *
 *
 * <p>??????????????? Demo</p>
 *
 */
@Controller
@RequestMapping("/front/aliPay")
@AllArgsConstructor
@Slf4j
@Api(value = "???????????????", tags = "???????????????")
public class AliPayController {
    private IOrdersService ordersService;
    private ITrxorderDetailService trxorderDetailService;
    private IWebsiteProfileService websiteProfileService;
    private IUserAccountService userAccountService;
    private ICourseService courseService;
    CrazyProperties crazyProperties;
    ProfileConfig profileConfig;
    private ILineclassEnrollService lineclassEnrollService;
    /**
     * ??????????????????
     */
     private final static String NOTIFY_URL = "/front/aliPay/notify_url";
    /**
     * ????????????
     */
//    private final static String NOTIFY_URL = "/aliPay/cert_notify_url";
    private final static String RETURN_URL = "/front/aliPay/return_url";
    /**
     * ????????????
     */
//    private final static String RETURN_URL = "/aliPay/cert_return_url";




    /**
     * app??????
     */
    @GetMapping(value = "/appPay")
    @ResponseBody
    @ApiOperation("app??????")
    public R appPay(Integer orderId,@RequestParam(defaultValue = "app") String clientType) throws AlipayApiException {

        Map<String, String> config = profileConfig.getConfig(WebsiteProfile.ALIPAY);
        AliPayApiConfigKit.setThreadLocalAppId(config.get("appId"));

        Orders order = ordersService.getById(orderId);
        //??????????????????
        List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<TrxorderDetail>().lambda().eq(TrxorderDetail::getTrxorderId, orderId));
        //??????????????????
        WebsiteProfile websiteProfile = new WebsiteProfile();
        websiteProfile.setConfigType("web");
        List<WebsiteProfile> websiteProfileList = websiteProfileService.list(new QueryWrapper<WebsiteProfile>(websiteProfile).lambda().orderByDesc(WebsiteProfile::getSort));
        //list???map
        Map<String, String> map = websiteProfileList.stream().collect(Collectors.toMap(WebsiteProfile::getDataKey, WebsiteProfile::getDataValue));

        //??????????????????
        String bodyInfo = list.stream().map(trxorderDetail -> trxorderDetail.getCourseName()).collect(Collectors.joining(","));
        //??????body??????
        String body = order.getUserId() + "-" + order.getOrderNo() + "-" + order.getOutTradeNo()+"-"+bodyInfo;
        String totalAmount = BigDecimalUtils.toString(order.getSumMoney());

        String subject = map.get("company");
        String passBackParams = clientType;
        String notifyUrl = crazyProperties.get("domain") + NOTIFY_URL;

        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(body);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setPassbackParams(passBackParams);
        log.info("wap outTradeNo>" + order.getOutTradeNo());
        model.setOutTradeNo(order.getOutTradeNo());
        model.setTimeoutExpress("30m");
        model.setPassbackParams(passBackParams);
        model.setProductCode("QUICK_MSECURITY_PAY");
        String orderInfo = AliPayApi.appPayToResponse(model, notifyUrl).getBody();
        return R.data(orderInfo);

    }

    @GetMapping(value = "/wapPayNoSdk")
    @ResponseBody
    @ApiOperation("??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "??????id"),
    })
    public void wapPayNoSdk(HttpServletResponse response,Integer orderId) {
        try {
//            AliPayApiConfig aliPayApiConfig = PayConfig.getAliPayApiConfig();
            AliPayApiConfig aliPayApiConfig = AliPayApiConfigKit.getAliPayApiConfig();
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("app_id", aliPayApiConfig.getAppId());
            paramsMap.put("method", "alipay.trade.wap.pay");
            paramsMap.put("return_url", crazyProperties.get("domain") + RETURN_URL);
            paramsMap.put("charset", aliPayApiConfig.getCharset());
            paramsMap.put("sign_type", aliPayApiConfig.getSignType());
            paramsMap.put("timestamp", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            paramsMap.put("version", "1.0");
            paramsMap.put("notify_url", crazyProperties.get("domain") + NOTIFY_URL);

            Map<String, String> bizMap = new HashMap<>();
            bizMap.put("body", "IJPay ????????????-H5");
            bizMap.put("subject", "IJPay ?????????????????????");
            bizMap.put("out_trade_no", CommonUtil.getOrderNum());
            bizMap.put("total_amount", "6.66");
            bizMap.put("product_code", "QUICK_WAP_WAY");

            paramsMap.put("biz_content", JSON.toJSONString(bizMap));

            String content = PayKit.createLinkString(paramsMap);


            String encrypt = RsaKit.encryptByPrivateKey(content, aliPayApiConfig.getPrivateKey());
            paramsMap.put("sign", encrypt);

            String url = aliPayApiConfig.getServiceUrl() + "?" + PayKit.createLinkString(paramsMap, true);
            log.info(url);
            response.sendRedirect(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/wapPay")
    @ResponseBody
    @ApiOperation("????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "??????id"),
            @ApiImplicitParam(name = "clientType", value = "??????????????? pc")
    })
    public void wapPay(HttpServletResponse response,Integer orderId,@RequestParam(defaultValue = "pc") String clientType) {

        Map<String, String> config = profileConfig.getConfig(WebsiteProfile.ALIPAY);
        AliPayApiConfigKit.setThreadLocalAppId(config.get("appId"));

        Orders order = ordersService.getById(orderId);
        //??????????????????
        List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<TrxorderDetail>().lambda().eq(TrxorderDetail::getTrxorderId, orderId));
        //??????????????????
        WebsiteProfile websiteProfile = new WebsiteProfile();
        websiteProfile.setConfigType("web");
        List<WebsiteProfile> websiteProfileList = websiteProfileService.list(new QueryWrapper<WebsiteProfile>(websiteProfile).lambda().orderByDesc(WebsiteProfile::getSort));
        //list???map
        Map<String, String> map = websiteProfileList.stream().collect(Collectors.toMap(WebsiteProfile::getDataKey, WebsiteProfile::getDataValue));

        //??????????????????
        String bodyInfo = list.stream().map(trxorderDetail -> trxorderDetail.getCourseName()).collect(Collectors.joining(","));
        //??????body??????
        String body = order.getUserId() + "-" + order.getOrderNo() + "-" + order.getOutTradeNo()+"-"+bodyInfo;
        String totalAmount = BigDecimalUtils.toString(order.getSumMoney());

        String subject = map.get("company");
        String passBackParams = clientType;
        String returnUrl = crazyProperties.get("domain") + RETURN_URL+"?passback_params="+clientType;
        String notifyUrl = crazyProperties.get("domain") + NOTIFY_URL;

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setBody(body);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setPassbackParams(passBackParams);
        log.info("wap outTradeNo>" + order.getOutTradeNo());
        model.setOutTradeNo(order.getOutTradeNo());
        model.setProductCode("QUICK_WAP_PAY");

        try {
            AliPayApi.wapPay(response, model, returnUrl, notifyUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * PC??????
     */
    @GetMapping(value = "/pcPay")
    @ResponseBody
    @ApiOperation("pc??????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "??????id"),
            @ApiImplicitParam(name = "clientType", value = "??????????????? pc")
    })
    public void pcPay(HttpServletResponse response,Integer orderId,@RequestParam(defaultValue = "pc") String clientType) {
        Map<String, String> config = profileConfig.getConfig(WebsiteProfile.ALIPAY);
        AliPayApiConfigKit.setThreadLocalAppId(config.get("appId"));

        try {
            Orders order = ordersService.getById(orderId);
            //??????????????????
            List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<TrxorderDetail>().lambda().eq(TrxorderDetail::getTrxorderId, orderId));
            //??????????????????
            WebsiteProfile websiteProfile = new WebsiteProfile();
            websiteProfile.setConfigType("web");
            List<WebsiteProfile> websiteProfileList = websiteProfileService.list(new QueryWrapper<WebsiteProfile>(websiteProfile).lambda().orderByDesc(WebsiteProfile::getSort));
            //list???map
            Map<String, String> map = websiteProfileList.stream().collect(Collectors.toMap(WebsiteProfile::getDataKey, WebsiteProfile::getDataValue));

            //??????????????????
            String bodyInfo = list.stream().map(trxorderDetail -> trxorderDetail.getCourseName()).collect(Collectors.joining(","));
            //??????body??????
            String body = order.getUserId() + "-" + order.getOrderNo() + "-" + order.getOutTradeNo()+"-"+bodyInfo;
            String totalAmount = BigDecimalUtils.toString(order.getSumMoney());
            log.info("pc outTradeNo>" + order.getOutTradeNo());

            String returnUrl = crazyProperties.get("domain") + RETURN_URL;
            String notifyUrl = crazyProperties.get("domain") + NOTIFY_URL;
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();

            model.setOutTradeNo(order.getOutTradeNo());
            model.setProductCode("FAST_INSTANT_TRADE_PAY");
            model.setTotalAmount(totalAmount);
            //??????????????????
            model.setSubject(map.get("company"));
            model.setBody(body);
            model.setPassbackParams(clientType);
            /**
             * ???????????????????????????,??????????????????????????????????????????
             * hb_fq_num???????????????????????????????????????3???6???12????????????????????????????????????????????????
             * hb_fq_seller_percent????????????????????????????????????????????????????????????100??????????????????????????????0??????????????????100???0??????????????????????????????????????????????????????
             */
//            ExtendParams extendParams = new ExtendParams();
//            extendParams.setHbFqNum("3");
//            extendParams.setHbFqSellerPercent("0");
//            model.setExtendParams(extendParams);

            AliPayApi.tradePage(response, model, notifyUrl, returnUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping(value = "/tradePay")
    @ResponseBody
    @ApiOperation("?????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authCode", value = "?????????"),
            @ApiImplicitParam(name = "scene", value = "?????? wave_code?????????????????????,bar_code???????????????")
    })
    public String tradePay(@RequestParam("authCode") String authCode, @RequestParam("scene") String scene) {


        String subject = null;
        String waveCode = "wave_code";
        String barCode = "bar_code";
        if (scene.equals(waveCode)) {
            subject = "Javen ???????????????????????????";
        } else if (scene.equals(barCode)) {
            subject = "Javen ??????????????????????????????";
        }
        String totalAmount = "100";
        String notifyUrl = crazyProperties.get("domain") + NOTIFY_URL;

        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setAuthCode(authCode);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setOutTradeNo(CommonUtil.getOrderNum());
        model.setScene(scene);
        try {
            return AliPayApi.tradePayToResponse(model, notifyUrl).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * ????????????
     */
    @GetMapping(value = "/tradePreCreatePay")
    @ResponseBody
    @ApiOperation("????????????")
    public String tradePreCreatePay() {
        String subject = "Javen ???????????????????????????";
        String totalAmount = "86";
        String storeId = "123";
        String notifyUrl = crazyProperties.get("domain") + NOTIFY_URL;
//        String notifyUrl = crazyProperties.get("domain") + "/aliPay/cert_notify_url";

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setStoreId(storeId);
        model.setTimeoutExpress("5m");
        model.setOutTradeNo(CommonUtil.getOrderNum());
        try {
            String resultStr = AliPayApi.tradePrecreatePayToResponse(model, notifyUrl).getBody();
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            return jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ??????????????????????????????
     * https://docs.open.alipay.com/309/106235/
     */
    @GetMapping(value = "/transfer")
    @ResponseBody
    @ApiOperation("??????????????????????????????")
    public String transfer() {
        String totalAmount = "66";
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(CommonUtil.getOrderNum());
        model.setPayeeType("ALIPAY_LOGONID");
        model.setPayeeAccount("gxthqd7606@sandbox.com");
        model.setAmount(totalAmount);
        model.setPayerShowName("????????????");
        model.setPayerRealName("????????????");
        model.setRemark("javen??????????????????????????????");

        try {
            return AliPayApi.transferToResponse(model).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*@RequestMapping(value = "/transferQuery")
    @ResponseBody
    @ApiOperation("??????????????????????????????")
    public String transferQuery(@RequestParam(required = false, name = "outBizNo") String outBizNo,
                                @RequestParam(required = false, name = "orderId") String orderId) {
        AlipayFundTransOrderQueryModel model = new AlipayFundTransOrderQueryModel();
        if (Func.isNotEmpty(outBizNo)) {
            model.setOutBizNo(outBizNo);
        }
        if (Func.isNotEmpty(orderId)) {
            model.setOrderId(orderId);
        }

        try {
            return AliPayApi.transferQueryToResponse(model).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

   /* @RequestMapping(value = "/uniTransfer")
    @ResponseBody
    public String uniTransfer() {
        String totalAmount = "1";
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setOutBizNo(CommonUtil.getOrderNum());
        model.setTransAmount(totalAmount);
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        model.setBizScene("DIRECT_TRANSFER");
        model.setOrderTitle("????????????-????????????????????????");
        model.setRemark("IJPay ??????????????????");

        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity("gxthqd7606@sandbox.com");
        payeeInfo.setIdentityType("ALIPAY_LOGON_ID");
        payeeInfo.setName("????????????");
        model.setPayeeInfo(payeeInfo);

        try {
            return AliPayApi.uniTransferToResponse(model,null).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

   /* @RequestMapping(value = "/uniTransferQuery")
    @ResponseBody
    public String uniTransferQuery(@RequestParam(required = false, name = "outBizNo") String outBizNo,
                                @RequestParam(required = false, name = "orderId") String orderId) {
        AlipayFundTransCommonQueryModel model = new AlipayFundTransCommonQueryModel();
        if (Func.isNotEmpty(outBizNo)) {
            model.setOutBizNo(outBizNo);
        }
        if (Func.isNotEmpty(orderId)) {
            model.setOrderId(orderId);
        }

        try {
            return AliPayApi.transCommonQueryToResponse(model,null).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/accountQuery")
    @ResponseBody
    public String accountQuery(@RequestParam(required = true, name = "aliPayUserId") String aliPayUserId) {
        AlipayFundAccountQueryModel model = new AlipayFundAccountQueryModel();
        model.setAlipayUserId(aliPayUserId);
        model.setAccountType("ACCTRANS_ACCOUNT");
        try {
            return AliPayApi.accountQueryToResponse(model,null).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ????????????????????????
     */
    /*@RequestMapping(value = "/authOrderFreeze")
    @ResponseBody
    public AlipayFundAuthOrderFreezeResponse authOrderFreeze(@RequestParam("auth_code") String authCode) {
        try {
            AlipayFundAuthOrderFreezeModel model = new AlipayFundAuthOrderFreezeModel();
            model.setOutOrderNo(CommonUtil.getOrderNum());
            model.setOutRequestNo(CommonUtil.getOrderNum());
            model.setAuthCode(authCode);
            model.setAuthCodeType("bar_code");
            model.setOrderTitle("??????????????????-By IJPay");
            model.setAmount("36");
            model.setProductCode("PRE_AUTH");

            return AliPayApi.authOrderFreezeToResponse(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/


    /**
     * ????????????????????????
     * https://docs.open.alipay.com/301/106168/
     */
   /* @RequestMapping(value = "/agreementPay")
    @ResponseBody
    public AlipayFundCouponOrderAgreementPayResponse agreementPay() {
        try {
            AlipayFundCouponOrderAgreementPayModel model = new AlipayFundCouponOrderAgreementPayModel();
            model.setOutOrderNo(CommonUtil.getOrderNum());
            model.setOutRequestNo(CommonUtil.getOrderNum());
            model.setOrderTitle("????????????????????????-By IJPay");
            model.setAmount("36");
            model.setPayerUserId("2088102180432465");

            return AliPayApi.fundCouponOrderAgreementPayToResponse(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ???????????????
     */
  /*  @RequestMapping(value = "/dataDataServiceBill")
    @ResponseBody
    public String dataDataServiceBill(@RequestParam("billDate") String billDate) {
        try {
            AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
            model.setBillType("trade");
            model.setBillDate(billDate);
            return AliPayApi.billDownloadUrlQuery(model);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ??????
     */
    /*@RequestMapping(value = "/tradeRefund")
    @ResponseBody
    public String tradeRefund(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo) {

        try {
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            if (Func.isNotEmpty(outTradeNo)) {
                model.setOutTradeNo(outTradeNo);
            }
            if (Func.isNotEmpty(tradeNo)) {
                model.setTradeNo(tradeNo);
            }
            model.setRefundAmount("86.00");
            model.setRefundReason("????????????");
            return AliPayApi.tradeRefundToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ????????????
     */
    /*@RequestMapping(value = "/tradeQuery")
    @ResponseBody
    public String tradeQuery(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo) {
        try {
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            if (Func.isNotEmpty(outTradeNo)) {
                model.setOutTradeNo(outTradeNo);
            }
            if (Func.isNotEmpty(tradeNo)) {
                model.setTradeNo(tradeNo);
            }
            return AliPayApi.tradeQueryToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*@RequestMapping(value = "/tradeQueryByStr")
    @ResponseBody
    public String tradeQueryByStr(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo) {
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        if (Func.isNotEmpty(outTradeNo)) {
            model.setOutTradeNo(outTradeNo);
        }
        if (Func.isNotEmpty(tradeNo)) {
            model.setTradeNo(tradeNo);
        }

        try {
            return AliPayApi.tradeQueryToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return null;
    }*/

    /**
     * ????????????
     * {"alipay_trade_create_response":{"code":"10000","msg":"Success","out_trade_no":"081014283315033","trade_no":"2017081021001004200200274066"},"sign":"ZagfFZntf0loojZzdrBNnHhenhyRrsXwHLBNt1Z/dBbx7cF1o7SZQrzNjRHHmVypHKuCmYifikZIqbNNrFJauSuhT4MQkBJE+YGPDtHqDf4Ajdsv3JEyAM3TR/Xm5gUOpzCY7w+RZzkHevsTd4cjKeGM54GBh0hQH/gSyhs4pEN3lRWopqcKkrkOGZPcmunkbrUAF7+AhKGUpK+AqDw4xmKFuVChDKaRdnhM6/yVsezJFXzlQeVgFjbfiWqULxBXq1gqicntyUxvRygKA+5zDTqE5Jj3XRDjVFIDBeOBAnM+u03fUP489wV5V5apyI449RWeybLg08Wo+jUmeOuXOA=="}
     */
   /* @RequestMapping(value = "/tradeCreate")
    @ResponseBody
    public String tradeCreate(@RequestParam("outTradeNo") String outTradeNo) {

        String notifyUrl = crazyProperties.get("domain") + NOTIFY_URL;

        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount("88.88");
        model.setBody("Body");
        model.setSubject("Javen ????????????????????????????????????");
        //???????????????????????????buyer_id??????????????????
        model.setBuyerLogonId("abpkvd0206@sandbox.com");
        try {
            AlipayTradeCreateResponse response = AliPayApi.tradeCreateToResponse(model, notifyUrl);
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ????????????
     */
   /* @RequestMapping(value = "/tradeCancel")
    @ResponseBody
    public String tradeCancel(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo) {
        try {
            AlipayTradeCancelModel model = new AlipayTradeCancelModel();
            if (Func.isNotEmpty(outTradeNo)) {
                model.setOutTradeNo(outTradeNo);
            }
            if (Func.isNotEmpty(tradeNo)) {
                model.setTradeNo(tradeNo);
            }

            return AliPayApi.tradeCancelToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }
*/
    /**
     * ????????????
     */
    /*@RequestMapping(value = "/tradeClose")
    @ResponseBody
    public String tradeClose(@RequestParam("outTradeNo") String outTradeNo, @RequestParam("tradeNo") String tradeNo) {
        try {
            AlipayTradeCloseModel model = new AlipayTradeCloseModel();
            if (Func.isNotEmpty(outTradeNo)) {
                model.setOutTradeNo(outTradeNo);
            }
            if (Func.isNotEmpty(tradeNo)) {
                model.setTradeNo(tradeNo);
            }

            return AliPayApi.tradeCloseToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ??????
     */
   /* @RequestMapping(value = "/tradeOrderSettle")
    @ResponseBody
    public String tradeOrderSettle(@RequestParam("tradeNo") String tradeNo) {
        try {
            AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
            model.setOutRequestNo(CommonUtil.getOrderNum());
            model.setTradeNo(tradeNo);

            return AliPayApi.tradeOrderSettleToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ??????????????????URL?????????
     */
 /*   @RequestMapping(value = "/toOauth")
    @ResponseBody
    public void toOauth(HttpServletResponse response) {
        try {
            String redirectUri = crazyProperties.get("domain") + "/aliPay/redirect_uri";
            String oauth2Url = AliPayApi.getOauth2Url(aliPayBean.getAppId(), redirectUri);
            response.sendRedirect(oauth2Url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * ??????????????????
     */
   /* @RequestMapping(value = "/redirect_uri")
    @ResponseBody
    public String redirectUri(@RequestParam("app_id") String appId, @RequestParam("app_auth_code") String appAuthCode) {
        try {
            log.info("app_id:" + appId);
            log.info("app_auth_code:" + appAuthCode);
            //??????app_auth_code??????app_auth_token
            AlipayOpenAuthTokenAppModel model = new AlipayOpenAuthTokenAppModel();
            model.setGrantType("authorization_code");
            model.setCode(appAuthCode);
            return AliPayApi.openAuthTokenAppToResponse(model).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ??????????????????
     */
   /* @RequestMapping(value = "/openAuthTokenAppQuery")
    @ResponseBody
    public String openAuthTokenAppQuery(@RequestParam("appAuthToken") String appAuthToken) {
        try {
            AlipayOpenAuthTokenAppQueryModel model = new AlipayOpenAuthTokenAppQueryModel();
            model.setAppAuthToken(appAuthToken);
            return AliPayApi.openAuthTokenAppQueryToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * ??????????????????????????????????????????
     */
    /*@RequestMapping(value = "/batchTrans")
    @ResponseBody
    public void batchTrans(HttpServletResponse response) {
        try {
            String signType = "MD5";
            String notifyUrl = crazyProperties.get("domain") + NOTIFY_URL;
            Map<String, String> params = new HashMap<>(15);
            params.put("partner", "PID");
            params.put("sign_type", signType);
            params.put("notify_url", notifyUrl);
            params.put("account_name", "xxx");
            params.put("detail_data", "?????????1^???????????????1^??????????????????1^????????????1^????????????1|?????????2^???????????????2^??????????????????2^????????????2^????????????2");
            params.put("batch_no", String.valueOf(System.currentTimeMillis()));
            params.put("batch_num", 1 + "");
            params.put("batch_fee", 10.00 + "");
            params.put("email", "xx@xxx.com");

            AliPayApi.batchTrans(params, aliPayBean.getPrivateKey(), signType, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    /**
     * ???????????????????????????
     */
    /*@RequestMapping(value = "/voucherGenerate")
    @ResponseBody
    public String voucherGenerate(@RequestParam("tradeNo") String tradeNo) {
        try {
            //??????????????????????????????
//			String tradeNo = getPara("tradeNo");

            AlipayCommerceCityfacilitatorVoucherGenerateModel model = new AlipayCommerceCityfacilitatorVoucherGenerateModel();
            model.setCityCode("440300");
            model.setTradeNo(tradeNo);
            model.setTotalFee("8");
            model.setTicketNum("2");
            model.setTicketType("oneway");
            model.setSiteBegin("001");
            model.setSiteEnd("002");
            model.setTicketPrice("4");
            return AliPayApi.voucherGenerateToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    @ApiOperation("???????????????????????????")
    @GetMapping(value = "/return_url")
    public String returnUrl(HttpServletRequest request) {
        try {
            // ???????????????GET??????????????????
            Map<String, String> map = AliPayApi.toMap(request);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                log.info(entry.getKey() + " = " + entry.getValue());
            }
            String passbackParams = map.get("passback_params")+"";
            map.remove("passback_params");
            Map<String, String> config = profileConfig.getConfig(WebsiteProfile.ALIPAY);
            boolean verifyResult = AlipaySignature.rsaCheckV1(map, config.get("publicKey"), "UTF-8",
                    "RSA2");

            if (verifyResult) {
                // TODO ???????????????????????????????????????????????????
                log.info("return_url ????????????");
                //??????????????????
                String outTradeNo = map.get("out_trade_no");

                Orders orders = ordersService.getOne(new QueryWrapper<Orders>().lambda().eq(Orders::getOutTradeNo, outTradeNo));
                if(Func.equals(orders.getStates(),Orders.STATES_INIT)){
                    orders.setStates(Orders.STATES_SUCCESS);
                    orders.setPayTime(org.springcrazy.core.tool.utils.DateUtil.now());
                    orders.setPayType(Orders.PAYTYPE_ALIPAY);
                    ordersService.updateOrder(orders);
                    //????????????
                    if(Func.equals(orders.getOrderType(),"ACCOUNT")){
                        //?????????????????????
                        TrxorderDetail trxorderDetail = new TrxorderDetail();
                        trxorderDetail.setTrxorderId(orders.getId());
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

                String domain = crazyProperties.get("front-domain");
                String url = domain+"/paySuccess";
                if(Func.equals("h5",passbackParams)){
                    url = domain+"/schoolapp/#/pages/order/paySuccess";
                }
                return "redirect:"+url;
            } else {
                log.info("return_url ????????????");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }



    /*@RequestMapping(value = "/cert_return_url")
    @ResponseBody
    public String certReturnUrl(HttpServletRequest request) {
        try {
            // ???????????????GET??????????????????
            Map<String, String> map = AliPayApi.toMap(request);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                log.info(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCertCheckV1(map, aliPayBean.getAliPayCertPath(), "UTF-8",
                    "RSA2");

            if (verifyResult) {
                // TODO ???????????????????????????????????????????????????
                log.info("certReturnUrl ????????????");

                return "success";
            } else {
                log.info("certReturnUrl ????????????");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }
*/

    @PostMapping(value = "/notify_url")
    @ResponseBody
    @ApiOperation("?????????????????????")
    public String notifyUrl(HttpServletRequest request) {
        try {
            // ???????????????POST??????????????????
            Map<String, String> params = AliPayApi.toMap(request);

            for (Map.Entry<String, String> entry : params.entrySet()) {
                log.info(entry.getKey() + " = " + entry.getValue());
            }


            Map<String, String> config = profileConfig.getConfig(WebsiteProfile.ALIPAY);
            boolean verifyResult = AlipaySignature.rsaCheckV1(params, config.get("publicKey"), "UTF-8", "RSA2");

            if (verifyResult) {
                // TODO ??????????????????????????????????????????????????? ?????????????????????????????????????????? ?????????????????????
                log.info("notify_url ????????????succcess");
                //??????????????????
                String outTradeNo = params.get("out_trade_no");
                Orders orders = ordersService.getOne(new QueryWrapper<Orders>().lambda().eq(Orders::getOutTradeNo, outTradeNo));
                if(Objects.nonNull(orders) && Func.equals(orders.getStates(),Orders.STATES_INIT)){
                    orders.setStates(Orders.STATES_SUCCESS);
                    orders.setPayTime(org.springcrazy.core.tool.utils.DateUtil.now());
                    orders.setPayType(Orders.PAYTYPE_ALIPAY);
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
                        userAccountService.money(orders.getUserId(),orders.getSumMoney(),"1",UserAccountHistory.BIZTYPE_MONEY,"????????????",orders.getOrderNo());
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

                return "success";
            } else {
                log.info("notify_url ????????????");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }

   /* @RequestMapping(value = "/cert_notify_url")
    @ResponseBody
    public String certNotifyUrl(HttpServletRequest request) {
        try {
            // ???????????????POST??????????????????
            Map<String, String> params = AliPayApi.toMap(request);

            for (Map.Entry<String, String> entry : params.entrySet()) {
                log.info(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCertCheckV1(params, aliPayBean.getAliPayCertPath(), "UTF-8", "RSA2");

            if (verifyResult) {
                // TODO ??????????????????????????????????????????????????? ?????????????????????????????????????????? ?????????????????????
                log.info("certNotifyUrl ????????????succcess");
                return "success";
            } else {
                log.info("certNotifyUrl ????????????");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }*/


}
