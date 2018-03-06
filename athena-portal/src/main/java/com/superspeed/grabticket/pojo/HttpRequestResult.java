package com.superspeed.grabticket.pojo;

import org.apache.http.cookie.Cookie;

import java.util.List;

public class HttpRequestResult {

    int statusCode;

    String data;

    List<Cookie> cookies;

    public HttpRequestResult(String data, List<Cookie> cookies) {
        this.data = data;
        this.cookies = cookies;
    }

    public HttpRequestResult(int statusCode, String data, List<Cookie> cookies) {
        this.statusCode = statusCode;
        this.data = data;
        this.cookies = cookies;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }
}
