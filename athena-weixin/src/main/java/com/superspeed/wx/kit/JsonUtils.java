package com.superspeed.common.kit;

import com.alibaba.fastjson.JSON;

/**
 * Json转换
 * 
 * JFinal-weixin内部使用
 * 
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年5月13日下午4:58:33
 */
public final class JsonUtils {

    private JsonUtils() {}
    
    private static JSON jsonFactory = null;
    
    /**
     * 自定义 jsonFactory 用户自定义切换
     * 1. 优先使用用户设定的JsonUtils.setJsonFactory
     * 2. 用户没有手动设定，使用JFinal中设定的
     * 3. JFinal中没有设定，使用JFinal默认的
     * @param jsonFactory json工厂
     */
    public static void setJsonFactory(JSON jsonFactory) {
        JsonUtils.jsonFactory = jsonFactory;
    }

    /**
     * 将 Object 转为json字符串
     * @param object 对象
     * @return JsonString
     */
    public static String toJson(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 将 json字符串 转为Object
     * @param jsonString json字符串
     * @param valueType 结果类型
     * @param <T> 泛型标记
     * @return T 结果
     */
    public static <T> T parse(String jsonString, Class<T> valueType) {
        return JSON.parseObject(jsonString, valueType);
    }

}
