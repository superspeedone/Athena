package com.superspeed.common;

import com.superspeed.common.kit.PropKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 微信配置
 * @ClassName: WxPayConfig
 * @Description: 微信配置
 * @author xc.yanww
 * @date 2017/12/11 16:27
 */
public class WxPayConfig {
    private Logger logger = LoggerFactory.getLogger(WxPayConfig.class);

    private static WxPayConfig INSTANCE;
    private static Properties properties;
    private static final String WX_PAY_CONFIG_PROP = "WxPayConfig.properties";

    private WxPayConfig() {
        //加载properties配置文件
        properties = PropKit.load(WX_PAY_CONFIG_PROP).getProperties();
    }

    public static WxPayConfig getInstance() {
        if (INSTANCE == null) {
            synchronized (WxPayConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WxPayConfig();
                }
            }
        }
        return INSTANCE;
    }

    public String getAppId() {
        //获取公众账号ID
        return getProp().getProperty("appId");
    }

    public String getMchId() {
        //获取APPID对应的接口密码
        return getProp().getProperty("mchId");
    }

    public String getAppSecret() {
        //获取微信支付商户号
        return getProp().getProperty("appSecret");
    }

    public String getPaternerKey() {
        //获取微信支付商户设置密钥
        return getProp().getProperty("parternerKey");
    }

    public String getDomain() {
        //应用域名
        return getProp().getProperty("domain");
    }

    public String getProxyIp() {
        //http代理服务器ip
        return getProp().getProperty("proxyIp");
    }

    public int getProxyPort() {
        //http代理服务器端口号
        return Integer.valueOf(getProp().getProperty("proxyPort"));
    }

    private Properties getProp() {
        getInstance();
        return properties;
    }

    public String get(String key) {
        return getProp().getProperty(key);
    }

}
