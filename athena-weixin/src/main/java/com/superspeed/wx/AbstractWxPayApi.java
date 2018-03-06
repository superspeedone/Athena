package com.superspeed.common;

import com.superspeed.common.constants.WxPayConstants;

import java.util.Map;

/**
 * 微信支付api
 */
public abstract class AbstractWxPayApi {

    public AbstractWxPayApi() {
    }

    /** 统一下单地址 */
    protected static final String unifiedOrderUrl = WxPayConstants.DOMAIN_URL + WxPayConstants.UNIFIEDORDER_URL_SUFFIX;
    /** 订单查询地址 */
    protected static final String orderQueryUrl = WxPayConstants.DOMAIN_URL + WxPayConstants.ORDERQUERY_URL_SUFFIX;
    /** 关闭订单地址 */
    protected static final String closeOrderUrl = WxPayConstants.DOMAIN_URL + WxPayConstants.CLOSEORDER_URL_SUFFIX;
    /** 申请退款地址 */
    protected static final String refundUrl = WxPayConstants.DOMAIN_URL + WxPayConstants.REFUND_URL_SUFFIX;
    /** 查询退款地址 */
    protected static final String refundQueryUrl = WxPayConstants.DOMAIN_URL + WxPayConstants.REFUNDQUERY_URL_SUFFIX;
    /** 下载对账单地址 */
    protected static final String downloadBillUrl = WxPayConstants.DOMAIN_URL + WxPayConstants.DOWNLOADBILL_URL_SUFFIX;

    /**
     * ALL，返回当日所有订单信息，默认值
     * SUCCESS，返回当日成功支付的订单
     * REFUND，返回当日退款订单
     * REVOKED，已撤销的订单
     */
    protected static enum BillType {
        ALL, SUCCESS, REFUND, REVOKED
    }

    /**
     * 统一下单
     * @param params 参数map
     * @return String
     */
    public abstract String pushOrder(Map<String, String> params);

    /**
     * @param appId       公众账号ID
     * @param prepayId    预支付交易会话标识
     * @param paternerKey 商户密钥
     * @return
     */
    public abstract String getDeepLink(String appId, String prepayId, String paternerKey);

    /**
     * 根据商户订单号查询信息
     * @param appid          公众账号ID
     * @param mch_id         商户号
     * @param paternerKey    商户密钥
     * @param transaction_id 微信订单号
     * @return 回调信息
     */
    public abstract Map<String, String> queryByTransactionId(String appid, String mch_id, String paternerKey, String
            transaction_id);

    /**
     * 根据商户订单号查询信息
     * @param appid        公众账号ID
     * @param mch_id       商户号
     * @param paternerKey  商户密钥
     * @param out_trade_no 商户订单号
     * @return 回调信息
     */
    public abstract Map<String, String> queryByOutTradeNo(String appid, String mch_id, String paternerKey, String
            out_trade_no);

    /**
     * 关闭订单
     * @param appid        公众账号ID
     * @param mch_id       商户号
     * @param paternerKey  商户密钥
     * @param out_trade_no 商户订单号
     * @return 回调信息
     */
    public abstract Map<String, String> closeOrder(String appid, String mch_id, String paternerKey, String
            out_trade_no);

    /**
     * 申请退款，内部添加了随机字符串nonce_str和签名sign
     * @param params      参数map，内部添加了随机字符串nonce_str和签名sign
     * @param paternerKey 商户密钥
     * @param certPath    证书文件目录
     * @return map
     */
    public abstract Map<String, String> refund(Map<String, String> params, String paternerKey, String certPath);



    /**
     * 根据微信订单号查询退款
     * @param appid          公众账号ID
     * @param mch_id         商户号
     * @param paternerKey    商户密钥
     * @param transaction_id 微信订单号
     * @return map
     */
    public abstract Map<String, String> refundQueryByTransactionId(String appid, String mch_id, String paternerKey,
                                                                   String transaction_id);

    /**
     * 根据微信订单号查询退款
     * @param appid        公众账号ID
     * @param mch_id       商户号
     * @param paternerKey  商户密钥
     * @param out_trade_no 商户订单号
     * @return map
     */
    public abstract Map<String, String> refundQueryByOutTradeNo(String appid, String mch_id, String paternerKey,
                                                                String out_trade_no);

    /**
     * 根据微信订单号查询退款
     * @param appid         公众账号ID
     * @param mch_id        商户号
     * @param paternerKey   商户密钥
     * @param out_refund_no 商户退款单号
     * @return map
     */
    public abstract Map<String, String> refundQueryByOutRefundNo(String appid, String mch_id, String paternerKey,
                                                                 String out_refund_no);

    /**
     * 根据微信订单号查询退款
     * @param appid       公众账号ID
     * @param mch_id      商户号
     * @param paternerKey 商户密钥
     * @param refund_id   微信退款单号
     * @return map
     */
    public abstract Map<String, String> refundQueryByRefundId(String appid, String mch_id, String paternerKey, String
            refund_id);

    /**
     * 下载对账单
     * @param appid       公众账号ID
     * @param mch_id      商户号
     * @param paternerKey 签名密匙
     * @param billDate    对账单日期
     * @param billType    账单类型
     * @return String
     */
    public abstract String downloadBill(String appid, String mch_id, String paternerKey, String billDate, BillType
            billType);

}
