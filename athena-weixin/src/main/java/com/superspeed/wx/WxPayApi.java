package com.superspeed.common;

import com.superspeed.common.kit.HttpKit;
import com.superspeed.common.kit.PaymentKit;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付api
 * @author L.cm
 */
public class WxPayApi extends AbstractWxPayApi {

    /**
     * 根据商户Key加密请求参数，并发送POST请求
     * @param url
     * @param params
     * @param paternerKey
     * @return Map<String, String>
     */
    private Map<String, String> request(String url, Map<String, String> params, String paternerKey) {
        params.put("nonce_str", System.currentTimeMillis() + "");
        String sign = PaymentKit.createSign(params, paternerKey);
        params.put("sign", sign);
        String xmlStr = HttpKit.post(url, PaymentKit.toXml(params));
        return PaymentKit.xmlToMap(xmlStr);
    }

    @Override
    public String pushOrder(Map<String, String> params) {
        String xmlParams = PaymentKit.toXml(params);
        System.out.println("push order params:" + xmlParams);
        return HttpKit.post(unifiedOrderUrl, xmlParams);
    }

    @Override
    public String getDeepLink(String appId, String prepayId, String paternerKey) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appId);
        params.put("noncestr", System.currentTimeMillis() + "");
        params.put("package", "WAP");
        params.put("prepayid", prepayId);
        params.put("timestamp", System.currentTimeMillis() / 1000 + "");
        String sign = PaymentKit.createSign(params, paternerKey);
        params.put("sign", sign);

        String string1 = PaymentKit.packageSign(params, true);

        String string2 = "";
        try {
            string2 = PaymentKit.urlEncode(string1);
        } catch (UnsupportedEncodingException e) {
        }

        return "weixin://wap/pay?" + string2;
    }

    @Override
    public Map<String, String> queryByTransactionId(String appid, String mch_id, String paternerKey, String
            transaction_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appid);
        params.put("mch_id", mch_id);
        params.put("transaction_id", transaction_id);
        return request(orderQueryUrl, params, paternerKey);
    }

    @Override
    public Map<String, String> queryByOutTradeNo(String appid, String mch_id, String paternerKey, String out_trade_no) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appid);
        params.put("mch_id", mch_id);
        params.put("out_trade_no", out_trade_no);
        return request(orderQueryUrl, params, paternerKey);
    }

    @Override
    public Map<String, String> closeOrder(String appid, String mch_id, String paternerKey, String out_trade_no) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appid);
        params.put("mch_id", mch_id);
        params.put("out_trade_no", out_trade_no);
        return request(closeOrderUrl, params, paternerKey);
    }

    @Override
    public Map<String, String> refund(Map<String, String> params, String paternerKey, String certPath) {
        params.put("nonce_str", System.currentTimeMillis() + "");
        String sign = PaymentKit.createSign(params, paternerKey);
        params.put("sign", sign);
        String partner = params.get("mch_id");
        String xmlStr = HttpKit.postSSL(refundUrl, PaymentKit.toXml(params), certPath, partner);
        return PaymentKit.xmlToMap(xmlStr);
    }

    private Map<String, String> baseRefundQuery(Map<String, String> params, String appid, String mch_id,
                                                  String paternerKey) {
        params.put("appid", appid);
        params.put("mch_id", mch_id);
        return request(refundQueryUrl, params, paternerKey);
    }

    @Override
    public Map<String, String> refundQueryByTransactionId(String appid, String mch_id, String paternerKey, String
            transaction_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("transaction_id", transaction_id);
        return baseRefundQuery(params, appid, mch_id, paternerKey);
    }

    @Override
    public Map<String, String> refundQueryByOutTradeNo(String appid, String mch_id, String paternerKey, String
            out_trade_no) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("out_trade_no", out_trade_no);
        return baseRefundQuery(params, appid, mch_id, paternerKey);
    }

    @Override
    public Map<String, String> refundQueryByOutRefundNo(String appid, String mch_id, String paternerKey, String
            out_refund_no) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("out_refund_no", out_refund_no);
        return baseRefundQuery(params, appid, mch_id, paternerKey);
    }

    @Override
    public Map<String, String> refundQueryByRefundId(String appid, String mch_id, String paternerKey, String
            refund_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("refund_id", refund_id);
        return baseRefundQuery(params, appid, mch_id, paternerKey);
    }

    public String downloadBill(String appid, String mch_id, String paternerKey, String billDate) {
        return downloadBill(appid, mch_id, paternerKey, billDate, null);
    }

    @Override
    public String downloadBill(String appid, String mch_id, String paternerKey, String billDate, BillType billType) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appid);
        params.put("mch_id", mch_id);
        params.put("nonce_str", System.currentTimeMillis() + "");
        params.put("bill_date", billDate);
        if (null != billType) {
            params.put("bill_type", billType.name());
        } else {
            params.put("bill_type", BillType.ALL.name());
        }
        String sign = PaymentKit.createSign(params, paternerKey);
        params.put("sign", sign);
        return HttpKit.post(downloadBillUrl, PaymentKit.toXml(params));
    }

}
