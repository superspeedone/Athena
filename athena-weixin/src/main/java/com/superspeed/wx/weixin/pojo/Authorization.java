package com.superspeed.common.weixin.pojo;

import java.io.Serializable;

/**
 * Created by yanweiwen on 2017/12/9.
 */
public class Authorization implements Serializable {

    private String accessToken;	//接口调用凭证
    private int expiresin;	//access_token接口调用凭证超时时间，单位（秒）
    private String refreshToken;	//用户刷新access_token
    private String openid;	//授权用户唯一标识
    private String scope;	//用户授权的作用域，使用逗号（,）分隔
    private String unionid;	 //当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresin() {
        return expiresin;
    }

    public void setExpiresin(int expiresin) {
        this.expiresin = expiresin;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public String toString() {
        return "Authorization{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresin='" + expiresin + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }

}
