package com.superspeed.util;

import com.superspeed.grabticket.pojo.HttpRequestResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * httpClient代理请求工具类
 * 
 * <p> Name: HttpClientProxy </p>
 * <p> Discription: httpClient代理请求工具类 </p>
 * @author xc.yanww
 * @date 2016-8-26 上午11:32:22
 */

public class HttpClientProxy {
	private static Logger logger = Logger.getLogger(HttpClientProxy.class.getName());
	
	private static final Integer CONNECTION_TIMEOUT = 15 * 1000; //设置请求超时15秒钟 
	
	private static final Integer SOCKET_TIMEOUT = 15 * 1000; //设置等待数据超时时间15秒钟
	
	private static final String proxyHostIp = "10.1.190.2";
	
	private static final int proxyHostPort = 31151;


	public static HttpRequestResult requestByGetMethod(String url) throws Exception {
		return requestByGetMethod(url, null);
	}
	
	/**
	 * 通过GET方式发起http代理请求(百度代理)
	 * @param url 请求地址
	 * @return 返回json或xml格式字符串
	 */
	public static HttpRequestResult requestByGetMethod(String url, List<BasicClientCookie> clientCookies) throws Exception {
		logger.info(String.format("HttpClient执行代理Get请求 Url:[%s] ", url));
		CloseableHttpClient httpClient = null;
		try {
			CookieStore cookieStore = new BasicCookieStore();
			// 创建HttpClientBuilder
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);
			// HttpClient
			httpClient = httpClientBuilder.build();
			// 依次是代理地址，代理端口号，协议类型（百度代理）
			HttpHost proxy = new HttpHost(proxyHostIp, proxyHostPort, "http");
			RequestConfig config = RequestConfig.custom().setProxy(proxy).
					setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).build();

			//请求地址，将请求字符串转化成URI对象
			URL httpUrl = new URL(url);
			URI uri = new URI(httpUrl.getProtocol(), httpUrl.getHost(), 
					httpUrl.getPath(), httpUrl.getQuery(), null);
			
			//创建HttpGet对象，发起GET请求
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setConfig(config);
			
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            //获取响应内容
			HttpEntity httpEntity = httpResponse.getEntity();
			if (null != httpEntity) {
				// 获取 Cookies
				List<Cookie> cookies = cookieStore.getCookies();
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				String returndata = EntityUtils.toString(httpEntity, "UTF-8");
				return new HttpRequestResult(statusCode, returndata, cookies);
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "get way request error.", e);
			throw new Exception("get way request error.");
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
			}
		}
		return null;
	}

	public static HttpRequestResult requestByPostMethod(String url, List<NameValuePair> params) throws Exception {
		return requestByPostMethod(url, params, null);
	}
	/**
	 * POST方式发起http请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return  返回json或xml格式字符串
	 */
	public static HttpRequestResult requestByPostMethod(String url, List<NameValuePair> params,
				List<BasicClientCookie> clientCookies) throws Exception {
		logger.info(String.format("HttpClient执行代理Post请求 Url:[%s] ", url));
		// HttpClient
		CloseableHttpClient httpClient = null;
		try {
			CookieStore cookieStore = new BasicCookieStore();
			// 设置cookie
			if (clientCookies != null && clientCookies.size() > 0) {
				for (int i = 0; i < clientCookies.size(); i++) {
					cookieStore.addCookie(clientCookies.get(i));
				}
			}
			// 创建HttpClientBuilder
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);
			httpClient = httpClientBuilder.build();
			// 依次是代理地址，代理端口号，协议类型
			HttpHost proxy = new HttpHost(proxyHostIp, proxyHostPort, "http");
			RequestConfig config = RequestConfig.custom().setProxy(proxy).
					setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).build();
			
			// 请求地址
			URL httpUrl = new URL(url);
			URI uri = new URI(httpUrl.getProtocol(), httpUrl.getHost(), 
					httpUrl.getPath(), httpUrl.getQuery(), null);
			
			//创建HttpPost对象，发起POST请求
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setConfig(config);
			UrlEncodedFormEntity entity;
			if (null != params) {
				entity = new UrlEncodedFormEntity(params, "UTF-8");
				httpPost.setEntity(entity);
			}
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			
			//获取响应内容
			HttpEntity httpEntity = httpResponse.getEntity();

			if (httpEntity != null) {
				// 获取 Cookies
				List<Cookie> cookies = cookieStore.getCookies();
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				String returndata = EntityUtils.toString(httpEntity, "UTF-8");
				return new HttpRequestResult(statusCode, returndata, cookies);
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "get way request error.", e);
			throw new Exception("get way request error.");
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
			}
		}
		return null;
	}

}
