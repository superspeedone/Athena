package com.superspeed.common.model;

/**
 * 微信支付订单信息实体类
 * @ClassName: WxPayOrder
 * @Description: 微信支付订单信息
 * @author xc.yanww
 * @date 2017/12/14 14:39
 */
public class WxPayOrder {
    private static final long serialVersionUID = 1L;
    /**
     * 商品ID
     */
    private String productId;
    /**
     * 订单名称
     */
    private String subject;
    /**
     * 商品描述
     */
    private String body;
    /**
     * 标价币种
     */
    private String feeType;
    /**
     * 总金额(单位是分)
     */
    private String totalFee;
    /**
     * 订单号(唯一)
     */
    private String outTradeNo;
    /**
     * 发起人IP地址
     */
    private String spbillCreateIp;
    /**
     * 附件数据主要用于商户携带订单的自定义数据
     */
    private String attach;
    /**
     * 订单失效时间(格式为yyyyMMddHHmmss)
     */
    private String timeExpire;
    /**
     * 指定支付方式(上传此参数no_credit--可限制用户不能使用信用卡支付)
     */
    private String limitPay;
    /**
     * 用户标识
     * trade_type=JSAPI时（即公众号支付）此参数必传
     * 此参数为微信用户在商户对应appid下的唯一标识)
     */
    private String openid;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return "WxPayOrder{" +
                "productId='" + productId + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", feeType='" + feeType + '\'' +
                ", totalFee='" + totalFee + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", spbillCreateIp='" + spbillCreateIp + '\'' +
                ", attach='" + attach + '\'' +
                ", timeExpire='" + timeExpire + '\'' +
                ", limitPay='" + limitPay + '\'' +
                ", openid='" + openid + '\'' +
                '}';
    }
}
