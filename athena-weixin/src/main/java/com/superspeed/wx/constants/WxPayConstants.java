package com.superspeed.common.constants;

/**
 * 常量
 */
public class WxPayConstants {

    /**
     * 加密类型常量
     */
    public enum SignType {
        MD5, HMACSHA256
    }

    /**
     * 标价币种
     */
    public enum FeeType {
        CNY
    }

    /**
     * 交易类型枚举
     */
    public enum TradeType {
        JSAPI, NATIVE, APP, WAP, MWEB
    }

    /**
     * 支付相关域名常量
     */
    public static final String DOMAIN_API = "api.mch.weixin.qq.com";
    public static final String DOMAIN_URL = "https://api.mch.weixin.qq.com";

    /**
     * 支付加密及接口返回成功与失败常量
     */
    public static final String FAIL = "FAIL";
    public static final String SUCCESS = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";

    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";

    //下单地址
    public static final String UNIFIEDORDER_URL_SUFFIX = "/pay/unifiedorder";
    //订单查询地址
    public static final String ORDERQUERY_URL_SUFFIX = "/pay/orderquery";
    //关闭订单
    public static final String CLOSEORDER_URL_SUFFIX = "/pay/closeorder";
    //退款地址
    public static final String REFUND_URL_SUFFIX = "/secapi/pay/refund";
    //退款查询地址
    public static final String REFUNDQUERY_URL_SUFFIX = "/pay/refundquery";
    //下载对账单地址
    public static final String DOWNLOADBILL_URL_SUFFIX = "/pay/downloadbill";
    //交易保障(接口调用信息上报)
    public static final String REPORT_URL_SUFFIX = "/payitil/report";
    //长链接转短链接地址
    public static final String SHORTURL_URL_SUFFIX = "/tools/shorturl";
    public static final String NOTIFY_URL_SUFFIX = "/wx/pay/notify";

    // sandbox
    public static final String SANDBOX_UNIFIEDORDER_URL_SUFFIX = "/sandboxnew/pay/unifiedorder";
    public static final String SANDBOX_ORDERQUERY_URL_SUFFIX = "/sandboxnew/pay/orderquery";
    public static final String SANDBOX_CLOSEORDER_URL_SUFFIX = "/sandboxnew/pay/closeorder";
    public static final String SANDBOX_REFUND_URL_SUFFIX = "/sandboxnew/secapi/pay/refund";
    public static final String SANDBOX_REFUNDQUERY_URL_SUFFIX = "/sandboxnew/pay/refundquery";
    public static final String SANDBOX_DOWNLOADBILL_URL_SUFFIX = "/sandboxnew/pay/downloadbill";
    public static final String SANDBOX_REPORT_URL_SUFFIX = "/sandboxnew/payitil/report";
    public static final String SANDBOX_SHORTURL_URL_SUFFIX = "/sandboxnew/tools/shorturl";

}

