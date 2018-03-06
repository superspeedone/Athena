package com.superspeed.common.weixin.utils;

import java.util.Date;

public class Config {
	private String starupload = "0";
	private String token = "";
	private String APPID = "";
	private String APPSECRET = "";
	private String AccessToken = "";
	private Date LastTime=null;  //上次AccessToken获取时间
	private static Config application = null;

	public static synchronized Config getInstance() {
		if (application == null)
			application = new Config();
		return application;
	}

	public String getStarupload() {
		return starupload;
	}

	public void setStarupload(String starupload) {
		this.starupload = starupload;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAPPID() {
		return APPID;
	}

	public void setAPPID(String aPPID) {
		APPID = aPPID;
	}

	public String getAPPSECRET() {
		return APPSECRET;
	}

	public void setAPPSECRET(String aPPSECRET) {
		APPSECRET = aPPSECRET;
	}

	public static Config getApplication() {
		return application;
	}

	public static void setApplication(Config application) {
		Config.application = application;
	}

	public String getAccessToken() {
		return AccessToken;
	}

	public void setAccessToken(String accessToken) {
		AccessToken = accessToken;
	}

	public Date getLastTime() {
		return LastTime;
	}

	public void setLastTime(Date lastTime) {
		LastTime = lastTime;
	}

}