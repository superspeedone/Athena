package com.superspeed.common.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Properties文件加载工具类
 * @ClassName: PropKit
 * @Description: Properties文件加载工具类
 * @author xc.yanww
 * @date 2017/12/14 9:55
 */
public class PropKit {

    private static Logger logger = LoggerFactory.getLogger(PropKit.class);

    /** 默认读取编码 */
    private static final String DEFAULT_ENCODING = "UTF-8";
    /** 配置文件加载缓存 */
    private static final ConcurrentHashMap<String, Prop> map = new ConcurrentHashMap<String, Prop>();

    private static final String WX_PAY_PROP = "WxPayConfig.properties";

    public static Prop load(String fileName) {
        return load(fileName, DEFAULT_ENCODING);
    }

    public static Prop load(String fileName, String encoding) {
        Prop prop = map.get(fileName);
        if (prop == null) {
            synchronized (PropKit.class) {
                prop = map.get(fileName);
                if (prop == null) {
                    prop = new Prop(fileName, encoding);
                    map.put(fileName, prop);
                }
            }
        }
        return prop;
    }

    public static Prop getDefaultProp() {
        Prop prop = map.get(WX_PAY_PROP);
        if (prop == null) {
            prop = load(WX_PAY_PROP);
        }
        return prop;
    }

    public static String getDefaultString(String key) {
        return getDefaultProp().getProperties().getProperty(key);
    }

}
