package com.superspeed.common;

import com.superspeed.common.constants.WxPayConstants;
import com.superspeed.common.constants.WxPayConstants.SignType;
import com.superspeed.common.kit.JsonKit;
import com.superspeed.common.kit.PaymentKit;
import com.superspeed.common.model.WxPayOrder;
import com.superspeed.common.model.WxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付服务
 */
public class WxPayment {
    private Logger logger = LoggerFactory.getLogger(WxPayment.class);

    private WxPayConfig wxPayConfig;
    private SignType signType;
    private boolean useSandbox;
    private String notifyUrl;
    private WxPayApi wxPayApi;

    public WxPayment() {
        this(null, true);
    }

    public WxPayment(final String notifyUrl) {
        this(notifyUrl, true);
    }

    public WxPayment(final String notifyUrl, final boolean useSandbox) {
        wxPayConfig = WxPayConfig.getInstance();
        this.notifyUrl = notifyUrl;
        this.useSandbox = useSandbox;
        if (useSandbox) {
            this.signType = SignType.MD5;
        } else {
            //SignType.HMACSHA256证书签名未开启
            this.signType = SignType.MD5;
        }
        if (notifyUrl == null || "".equals(notifyUrl.trim())) {
            this.notifyUrl = wxPayConfig.getDomain() + WxPayConstants.NOTIFY_URL_SUFFIX;
        }
        this.wxPayApi = new WxPayApi();
    }

    private void checkWXPayConfig() throws Exception {
        if (this.wxPayConfig == null) {
            throw new Exception("config is null");
        }
        if (this.wxPayConfig.getAppId() == null || "".equals(this.wxPayConfig.getAppId().trim())) {
            throw new Exception("appId in config is empty");
        }
        if (this.wxPayConfig.getAppSecret() == null || "".equals(this.wxPayConfig.getAppSecret().trim())) {
            throw new Exception("appSecret in config is empty");
        }
        if (this.wxPayConfig.getMchId() == null || "".equals(this.wxPayConfig.getMchId().trim())) {
            throw new Exception("mchId in config is empty");
        }
        if (this.wxPayConfig.getPaternerKey() == null || "".equals(this.wxPayConfig.getPaternerKey().trim())) {
            throw new Exception("paternerKey in config is empty");
        }
        if (this.wxPayConfig.getDomain() == null || "".equals(this.wxPayConfig.getDomain().trim())) {
            throw new Exception("domain in config is empty");
        }
    }

    /**
     * 下单服务
     * @param wxPayOrder
     */
    public WxResult transorder(WxPayOrder wxPayOrder) throws Exception {
        logger.info("下单预支付");
        WxResult wxResult = new WxResult();
        String  outTradeNo = null;
        try {
            checkWXPayConfig();
            if (wxPayOrder == null) {
                wxResult.setBusinessError("订单信息不能为空");
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("appid", wxPayConfig.getAppId());
            params.put("mch_id", this.wxPayConfig.getMchId());
            params.put("body", wxPayOrder.getBody());
            outTradeNo = wxPayOrder.getOutTradeNo();
            params.put("out_trade_no", outTradeNo);
            int price = ((int) (Float.valueOf(wxPayOrder.getTotalFee()) * 100));
            params.put("total_fee", price + "");
            // params.put("attach", JsonKit.toJson(new PayAttach(out_trade_no, 2, 3)));
            params.put("spbill_create_ip", wxPayOrder.getSpbillCreateIp());
            params.put("trade_type", WxPayConstants.TradeType.JSAPI.name());
            params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
            params.put("notify_url", this.notifyUrl);
            params.put("openid", wxPayOrder.getOpenid());
            String sign = PaymentKit.createSign(params, this.wxPayConfig.getPaternerKey());
            params.put("sign", sign);
            //调用支付订单生成接口
            String xmlResult = this.wxPayApi.pushOrder(params);
            logger.info("Weixin transorder params : {}", xmlResult);
            Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
            String returnCode = result.get("return_code");
            String returnMsg  = result.get("return_msg");
            //return_code为SUCCESS表示调用成功
            if (WxPayConstants.SUCCESS.equals(returnCode)) {
                String resultCode = result.get("result_code");
                String errCodeDes = result.get("err_code_des");
                //resultCode为SUCCESS表示支付成功
                if (WxPayConstants.SUCCESS.equals(resultCode)) {
                    //获取预支付交易会话标识
                    String prepay_id = (String) result.get("prepay_id");
                    Map<String, String> packageParams = new HashMap<String, String>();
                    packageParams.put("appId", this.wxPayConfig.getAppId());
                    packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
                    packageParams.put("nonceStr", System.currentTimeMillis() + "");
                    packageParams.put("package", "prepay_id=" + prepay_id);
                    packageParams.put("signType", "DigestUtils");
                    String packageSign = PaymentKit.createSign(packageParams, this.wxPayConfig.getPaternerKey());
                    packageParams.put("paySign", packageSign);
                    String jsonStr = JsonKit.toJson(packageParams);
                    wxResult.setData(jsonStr);
                } else {
                    logger.info("订单号:{} 下单预支付错误:{}", outTradeNo, errCodeDes);
                    wxResult.setBusinessError(errCodeDes);
                }
            } else {
                logger.info("订单号:{} 下单预支付错误:{}", outTradeNo, returnMsg);
                wxResult.setBusinessError(returnMsg);
            }
        } catch (Exception e) {
            logger.error("订单号:{} 下单预支付错误.", outTradeNo, e);
            wxResult.setError(e.getMessage());
        }
        return wxResult;
    }

    public WxResult paynotify(String xmlstr) {
        logger.info("支付结果通知");
        WxResult wxResult = new WxResult();
        try {
            Map<String, String> params = PaymentKit.xmlToMap(xmlstr);
            String appid = params.get("appid");
            //商户号
            String mch_id = params.get("mch_id");
            String result_code = params.get("result_code");
            String openId = params.get("openid");
            //交易类型
            String trade_type = params.get("trade_type");
            //付款银行
            String bank_type = params.get("bank_type");
            // 总金额
            String total_fee = params.get("total_fee");
            //现金支付金额
            String cash_fee = params.get("cash_fee");
            // 微信支付订单号
            String transaction_id = params.get("transaction_id");
            // 商户订单号
            String out_trade_no = params.get("out_trade_no");
            // 支付完成时间，格式为yyyyMMddHHmmss
            String time_end = params.get("time_end");
            //////// 以下是附加参数 /////////
            String attach = params.get("attach");
            String fee_type = params.get("fee_type");
            String is_subscribe = params.get("is_subscribe");
            String err_code = params.get("err_code");
            String err_code_des = params.get("err_code_des");

            // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
            // 避免已经成功、关闭、退款的订单被再次更新
            //Order order = Order.dao.getOrderByTransactionId(transaction_id);
            Order order = null;

            if (order == null) {
                if (PaymentKit.verifyNotify(params, this.wxPayConfig.getPaternerKey())) {
                    if ((WxPayConstants.SUCCESS).equals(result_code)) {
                        //更新订单信息
                        logger.warn("更新订单信息:" + attach);
                        //发送通知等 TODO
                        Map<String, String> xml = new HashMap<String, String>();
                        xml.put("return_code", WxPayConstants.SUCCESS);
                        xml.put("return_msg", "OK");
                        wxResult.setData(PaymentKit.toXml(xml));
                    }
                }
                wxResult.setBusinessError("签名校验失败");
            } else {
                wxResult.setBusinessError("该订单已支付");
            }
        } catch (Exception e) {
            logger.error("微信通知异常.", e);
            wxResult.setError(e.getMessage());
        }
        return wxResult;
    }


    public static void main(String[] args) {
        try {
            String appId = "SHWT" + System.currentTimeMillis() + "";
            WxPayOrder order = new WxPayOrder();
            order.setOpenid("oGsOGv5WtflhWWS7HUSrwPBH3N4Y");
            order.setBody("自助投保系统支付测试 订单号:" + appId);
            order.setOutTradeNo(appId);
            order.setTotalFee("0.1");
            order.setSpbillCreateIp("10.7.187.217");
            WxPayment wxPayment = new WxPayment();
            WxResult wxResult = wxPayment.transorder(order);
            System.out.println(wxResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
