package com.superspeed.controller.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Created by yanweiwen on 2017/12/6.
 */
public class BaseController {

    protected Logger log = LogManager.getLogger(getClass());

    private static final String DEFAULT_APP_ID_KEY = "appId";

    private final String appIdKey;

    public BaseController() {
        this.appIdKey = DEFAULT_APP_ID_KEY;
    }

    public BaseController(String appIdKey) {
        this.appIdKey = appIdKey;
    }

    /**
     * 获取appId
     *
     * @return
     */
    public String getAppId() {
        return getRequest().getParameter(appIdKey);
    }

    /**
     * 获取当前请求对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    /**
     * 获取当前会话对象
     *
     * @return
     */
    public static HttpSession getSession() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }

    /**
     * getAttribute这个方法是提取放置在某个共享区间的对象
     * @param name
     * @return
     */
    public static Object getAttribute(String name){
        return  getRequest().getSession().getAttribute(name);
    }

    /**
     * getParameter系列的方法主要用于处理“请求数据”，是服务器端程序获取浏览器所传递参数的主要接口。
     * @param name 表单name属性
     * @return
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * getParameterNames这个方法是获得传过来的参数名相同的一个数组;
     * @return
     */
    public static Enumeration<String> getParameterNames(){
        return getRequest().getParameterNames();
    }

    public static String toJson(Object object) {
        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 获取当前请求地址
     *
     * @return
     */
    public static String getRequestUrl() {
        HttpServletRequest request = getRequest();
        String url = request.getRequestURL().toString();
        return url;
    }

}
