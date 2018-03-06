package com.superspeed.grabticket;

import com.superspeed.grabticket.pojo.HttpRequestResult;
import com.superspeed.util.HttpClientProxy;
import org.apache.http.NameValuePair;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginHandler {

    private static List<BasicClientCookie> loginCookie = new ArrayList<>();

    public static void main(String[] args) {
        LoginHandler loginHandler = new LoginHandler();
        loginHandler.doLogin();
    }

    public boolean doLogin() {

        try {

            HttpRequestResult httpRequestResult = HttpClientProxy.requestByGetMethod("https://kyfw.12306.cn/otn/");
            System.out.println(httpRequestResult.getData());
            System.out.printf("StatusCode:[%d] \n", httpRequestResult.getStatusCode());
            List<Cookie> cookies = httpRequestResult.getCookies();
            BasicClientCookie clientCookie;
            for (Cookie cookie : cookies) {
                clientCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
                clientCookie.setVersion(0);
                clientCookie.setDomain(cookie.getDomain());
                clientCookie.setPath(cookie.getPath());
                loginCookie.add(clientCookie);
            }

            httpRequestResult = HttpClientProxy.requestByPostMethod("https://kyfw.12306.cn/otn/login/userLogin", null, loginCookie);
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("loginUserDTO.username", "525390802"));
            params.add(new BasicNameValuePair("userDTO.password", ""));
            params.add(new BasicNameValuePair("randCode_validate", ""));
            params.add(new BasicNameValuePair("MTAyOTA5", runSecretKeyValueMethod("MTAyOTA5", "1111")));
            params.add(new BasicNameValuePair("myversion", ""));

            System.out.printf("StatusCode [%d] \n" ,httpRequestResult.getStatusCode());
            System.out.printf("Return Data [%s] \n", httpRequestResult.getData());

        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    public String runSecretKeyValueMethod(String mark,String jsStr) throws Exception {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByExtension("js");
        se.eval(jsStr);
        String value = (String) se.eval("eval(\"encode32(bin216(Base32.encrypt('1111','"+mark+"')))\")");
        return value;
    }



}
