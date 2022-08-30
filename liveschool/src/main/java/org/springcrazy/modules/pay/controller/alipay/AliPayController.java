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
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 *
 * <p>支付宝支付 Demo</p>
 *
 */
@Controller
@RequestMapping("/front/aliPay")
@AllArgsConstructor
@Slf4j
@Api(value = "支付宝支付", tags = "支付宝支付")
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
     * 普通公钥模式
     */
     private final static String NOTIFY_URL = "/front/aliPay/notify_url";
    /**
     * 证书模式
     */
//    private final static String NOTIFY_URL = "/aliPay/cert_notify_url";
    private final static String RETURN_URL = "/front/aliPay/return_url";
    /**
     * 证书模式
     */
//    private final static String RETURN_URL = "/aliPay/cert_return_url";




    /**
     * app支付
     */
    @GetMapping(value = "/appPay")
    @ResponseBody
    @ApiOperation("app支付")
    public R appPay(Integer orderId,@RequestParam(defaultValue = "app") String clientType) throws AlipayApiException {

        Map<String, String> config = profileConfig.getConfig(WebsiteProfile.ALIPAY);
        AliPayApiConfigKit.setThreadLocalAppId(config.get("appId"));

        Orders order = ordersService.getById(orderId);
        //获取订单详情
        List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<TrxorderDetail>().lambda().eq(TrxorderDetail::getTrxorderId, orderId));
        //获取网站配置
        WebsiteProfile websiteProfile = new WebsiteProfile();
        websiteProfile.setConfigType("web");
        List<WebsiteProfile> websiteProfileList = websiteProfileService.list(new QueryWrapper<WebsiteProfile>(websiteProfile).lambda().orderByDesc(WebsiteProfile::getSort));
        //list转map
        Map<String, String> map = websiteProfileList.stream().collect(Collectors.toMap(WebsiteProfile::getDataKey, WebsiteProfile::getDataValue));

        //配置订单信息
        String bodyInfo = list.stream().map(trxorderDetail -> trxorderDetail.getCourseName()).collect(Collectors.joining(","));
        //拼接body内容
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
    @ApiOperation("手机网页支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id"),
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
            bizMap.put("body", "IJPay 聚合支付-H5");
            bizMap.put("subject", "IJPay 让支付触手可及");
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
    @ApiOperation("手机支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id"),
            @ApiImplicitParam(name = "clientType", value = "客户端类型 pc")
    })
    public void wapPay(HttpServletResponse response,Integer orderId,@RequestParam(defaultValue = "pc") String clientType) {

        Map<String, String> config = profileConfig.getConfig(WebsiteProfile.ALIPAY);
        AliPayApiConfigKit.setThreadLocalAppId(config.get("appId"));

        Orders order = ordersService.getById(orderId);
        //获取订单详情
        List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<TrxorderDetail>().lambda().eq(TrxorderDetail::getTrxorderId, orderId));
        //获取网站配置
        WebsiteProfile websiteProfile = new WebsiteProfile();
        websiteProfile.setConfigType("web");
        List<WebsiteProfile> websiteProfileList = websiteProfileService.list(new QueryWrapper<WebsiteProfile>(websiteProfile).lambda().orderByDesc(WebsiteProfile::getSort));
        //list转map
        Map<String, String> map = websiteProfileList.stream().collect(Collectors.toMap(WebsiteProfile::getDataKey, WebsiteProfile::getDataValue));

        //配置订单信息
        String bodyInfo = list.stream().map(trxorderDetail -> trxorderDetail.getCourseName()).collect(Collectors.joining(","));
        //拼接body内容
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
     * PC支付
     */
    @GetMapping(value = "/pcPay")
    @ResponseBody
    @ApiOperation("pc支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id"),
            @ApiImplicitParam(name = "clientType", value = "客户端类型 pc")
    })
    public void pcPay(HttpServletResponse response,Integer orderId,@RequestParam(defaultValue = "pc") String clientType) {
        Map<String, String> config = profileConfig.getConfig(WebsiteProfile.ALIPAY);
        AliPayApiConfigKit.setThreadLocalAppId(config.get("appId"));

        try {
            Orders order = ordersService.getById(orderId);
            //获取订单详情
            List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<TrxorderDetail>().lambda().eq(TrxorderDetail::getTrxorderId, orderId));
            //获取网站配置
            WebsiteProfile websiteProfile = new WebsiteProfile();
            websiteProfile.setConfigType("web");
            List<WebsiteProfile> websiteProfileList = websiteProfileService.list(new QueryWrapper<WebsiteProfile>(websiteProfile).lambda().orderByDesc(WebsiteProfile::getSort));
            //list转map
            Map<String, String> map = websiteProfileList.stream().collect(Collectors.toMap(WebsiteProfile::getDataKey, WebsiteProfile::getDataValue));

            //配置订单信息
            String bodyInfo = list.stream().map(trxorderDetail -> trxorderDetail.getCourseName()).collect(Collectors.joining(","));
            //拼接body内容
            String body = order.getUserId() + "-" + order.getOrderNo() + "-" + order.getOutTradeNo()+"-"+bodyInfo;
            String totalAmount = BigDecimalUtils.toString(order.getSumMoney());
            log.info("pc outTradeNo>" + order.getOutTradeNo());

            String returnUrl = crazyProperties.get("domain") + RETURN_URL;
            String notifyUrl = crazyProperties.get("domain") + NOTIFY_URL;
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();

            model.setOutTradeNo(order.getOutTradeNo());
            model.setProductCode("FAST_INSTANT_TRADE_PAY");
            model.setTotalAmount(totalAmount);
            //填入公司信息
            model.setSubject(map.get("company"));
            model.setBody(body);
            model.setPassbackParams(clientType);
            /**
             * 花呗分期相关的设置,测试环境不支持花呗分期的测试
             * hb_fq_num代表花呗分期数，仅支持传入3、6、12，其他期数暂不支持，传入会报错；
             * hb_fq_seller_percent代表卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持，传入会报错。
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
    @ApiOperation("支付宝商家支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authCode", value = "授权码"),
            @ApiImplicitParam(name = "scene", value = "场景 wave_code支付宝声波支付,bar_code条形码支付")
    })
    public String tradePay(@RequestParam("authCode") String authCode, @RequestParam("scene") String scene) {


        String subject = null;
        String waveCode = "wave_code";
        String barCode = "bar_code";
        if (scene.equals(waveCode)) {
            subject = "Javen 支付宝声波支付测试";
        } else if (scene.equals(barCode)) {
            subject = "Javen 支付宝条形码支付测试";
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
     * 扫码支付
     */
    @GetMapping(value = "/tradePreCreatePay")
    @ResponseBody
    @ApiOperation("扫码支付")
    public String tradePreCreatePay() {
        String subject = "Javen 支付宝扫码支付测试";
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
     * 单笔转账到支付宝账户
     * https://docs.open.alipay.com/309/106235/
     */
    @GetMapping(value = "/transfer")
    @ResponseBody
    @ApiOperation("单笔转账到支付宝账户")
    public String transfer() {
        String totalAmount = "66";
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(CommonUtil.getOrderNum());
        model.setPayeeType("ALIPAY_LOGONID");
        model.setPayeeAccount("gxthqd7606@sandbox.com");
        model.setAmount(totalAmount);
        model.setPayerShowName("测试退款");
        model.setPayerRealName("沙箱环境");
        model.setRemark("javen测试单笔转账到支付宝");

        try {
            return AliPayApi.transferToResponse(model).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*@RequestMapping(value = "/transferQuery")
    @ResponseBody
    @ApiOperation("单笔转账到支付宝账户")
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
        model.setOrderTitle("统一转账-转账至支付宝账户");
        model.setRemark("IJPay 测试统一转账");

        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity("gxthqd7606@sandbox.com");
        payeeInfo.setIdentityType("ALIPAY_LOGON_ID");
        payeeInfo.setName("沙箱环境");
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
     * 资金授权冻结接口
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
            model.setOrderTitle("资金授权冻结-By IJPay");
            model.setAmount("36");
            model.setProductCode("PRE_AUTH");

            return AliPayApi.authOrderFreezeToResponse(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/


    /**
     * 红包协议支付接口
     * https://docs.open.alipay.com/301/106168/
     */
   /* @RequestMapping(value = "/agreementPay")
    @ResponseBody
    public AlipayFundCouponOrderAgreementPayResponse agreementPay() {
        try {
            AlipayFundCouponOrderAgreementPayModel model = new AlipayFundCouponOrderAgreementPayModel();
            model.setOutOrderNo(CommonUtil.getOrderNum());
            model.setOutRequestNo(CommonUtil.getOrderNum());
            model.setOrderTitle("红包协议支付接口-By IJPay");
            model.setAmount("36");
            model.setPayerUserId("2088102180432465");

            return AliPayApi.fundCouponOrderAgreementPayToResponse(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * 下载对账单
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
     * 退款
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
            model.setRefundReason("正常退款");
            return AliPayApi.tradeRefundToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * 交易查询
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
     * 创建订单
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
        model.setSubject("Javen 测试统一收单交易创建接口");
        //买家支付宝账号，和buyer_id不能同时为空
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
     * 撤销订单
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
     * 关闭订单
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
     * 结算
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
     * 获取应用授权URL并授权
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
     * 应用授权回调
     */
   /* @RequestMapping(value = "/redirect_uri")
    @ResponseBody
    public String redirectUri(@RequestParam("app_id") String appId, @RequestParam("app_auth_code") String appAuthCode) {
        try {
            log.info("app_id:" + appId);
            log.info("app_auth_code:" + appAuthCode);
            //使用app_auth_code换取app_auth_token
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
     * 查询授权信息
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
     * 批量付款到支付宝账户有密接口
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
            params.put("detail_data", "流水号1^收款方账号1^收款账号姓名1^付款金额1^备注说明1|流水号2^收款方账号2^收款账号姓名2^付款金额2^备注说明2");
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
     * 地铁购票核销码发码
     */
    /*@RequestMapping(value = "/voucherGenerate")
    @ResponseBody
    public String voucherGenerate(@RequestParam("tradeNo") String tradeNo) {
        try {
            //需要支付成功的订单号
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

    @ApiOperation("支付宝支付同步回调")
    @GetMapping(value = "/return_url")
    public String returnUrl(HttpServletRequest request) {
        try {
            // 获取支付宝GET过来反馈信息
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
                // TODO 请在这里加上商户的业务逻辑程序代码
                log.info("return_url 验证成功");
                //更改订单状态
                String outTradeNo = map.get("out_trade_no");

                Orders orders = ordersService.getOne(new QueryWrapper<Orders>().lambda().eq(Orders::getOutTradeNo, outTradeNo));
                if(Func.equals(orders.getStates(),Orders.STATES_INIT)){
                    orders.setStates(Orders.STATES_SUCCESS);
                    orders.setPayTime(org.springcrazy.core.tool.utils.DateUtil.now());
                    orders.setPayType(Orders.PAYTYPE_ALIPAY);
                    ordersService.updateOrder(orders);
                    //账户充值
                    if(Func.equals(orders.getOrderType(),"ACCOUNT")){
                        //更新订单详情表
                        TrxorderDetail trxorderDetail = new TrxorderDetail();
                        trxorderDetail.setTrxorderId(orders.getId());
                        List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<>(trxorderDetail));
                        list.forEach(l ->{
                            l.setPayTime(org.springcrazy.core.tool.utils.DateUtil.now());
                            l.setAuthStatus(TrxorderDetail.STATUS_SUCCESS);
                        });
                        trxorderDetailService.updateBatchById(list);
                        //添加储值
                        userAccountService.money(orders.getUserId(),orders.getSumMoney(),"1", UserAccountHistory.BIZTYPE_MONEY,"用户充值",orders.getOrderNo());
                    }
                    //课程购买
                    if(Func.equals(orders.getOrderType(),"COURSE") || Func.equals(orders.getOrderType(),"LIVE") || Func.equals(orders.getOrderType(),"PACKAGE")){
                        //更新订单详情表
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
                            //处理销售量
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
                log.info("return_url 验证失败");
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
            // 获取支付宝GET过来反馈信息
            Map<String, String> map = AliPayApi.toMap(request);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                log.info(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCertCheckV1(map, aliPayBean.getAliPayCertPath(), "UTF-8",
                    "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码
                log.info("certReturnUrl 验证成功");

                return "success";
            } else {
                log.info("certReturnUrl 验证失败");
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
    @ApiOperation("支付宝异步回调")
    public String notifyUrl(HttpServletRequest request) {
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayApi.toMap(request);

            for (Map.Entry<String, String> entry : params.entrySet()) {
                log.info(entry.getKey() + " = " + entry.getValue());
            }


            Map<String, String> config = profileConfig.getConfig(WebsiteProfile.ALIPAY);
            boolean verifyResult = AlipaySignature.rsaCheckV1(params, config.get("publicKey"), "UTF-8", "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
                log.info("notify_url 验证成功succcess");
                //更改订单状态
                String outTradeNo = params.get("out_trade_no");
                Orders orders = ordersService.getOne(new QueryWrapper<Orders>().lambda().eq(Orders::getOutTradeNo, outTradeNo));
                if(Objects.nonNull(orders) && Func.equals(orders.getStates(),Orders.STATES_INIT)){
                    orders.setStates(Orders.STATES_SUCCESS);
                    orders.setPayTime(org.springcrazy.core.tool.utils.DateUtil.now());
                    orders.setPayType(Orders.PAYTYPE_ALIPAY);
                    ordersService.updateOrder(orders);

                    if(Func.equals(orders.getOrderType(),"ACCOUNT")){
                        //更新订单详情表
                        TrxorderDetail trxorderDetail = new TrxorderDetail();
                        List<TrxorderDetail> list = trxorderDetailService.list(new QueryWrapper<>(trxorderDetail));
                        list.forEach(l ->{
                            l.setPayTime(org.springcrazy.core.tool.utils.DateUtil.now());
                            l.setAuthStatus(TrxorderDetail.STATUS_SUCCESS);
                        });
                        trxorderDetailService.updateBatchById(list);
                        //添加储值
                        userAccountService.money(orders.getUserId(),orders.getSumMoney(),"1",UserAccountHistory.BIZTYPE_MONEY,"用户充值",orders.getOrderNo());
                    }
                    //课程购买
                    if(Func.equals(orders.getOrderType(),"COURSE") || Func.equals(orders.getOrderType(),"LIVE") || Func.equals(orders.getOrderType(),"PACKAGE")){
                        //更新订单详情表
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
                            //处理销售量
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
                log.info("notify_url 验证失败");
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
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayApi.toMap(request);

            for (Map.Entry<String, String> entry : params.entrySet()) {
                log.info(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCertCheckV1(params, aliPayBean.getAliPayCertPath(), "UTF-8", "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
                log.info("certNotifyUrl 验证成功succcess");
                return "success";
            } else {
                log.info("certNotifyUrl 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }*/


}
