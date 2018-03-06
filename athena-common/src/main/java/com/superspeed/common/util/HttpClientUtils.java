package com.superspeed.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * httpClient请求工具类
 * @ClassName: HttpClientUtil
 * @Description: httpClient请求工具类
 * @author xc.yanww
 * @date 2017-08-04 上午10:13:31
 * @modify xc.yanww 2017-08-04 上午10:13:31 修复v_1.0版本只能调用80端口的问题
 * @version 2.0
 */
 
public class HttpClientUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	
	private static final String DEFAULT_ENCODING = "UTF-8"; //默认URL请求实体编码
	
    private static final Integer CONNECTION_TIMEOUT = 15 * 1000; //设置请求超时15秒钟 
	
	private static final Integer SOCKET_TIMEOUT = 15 * 1000; //设置等待数据超时时间15秒钟
	
	/**
	 * 通过GET方式发起http请求
	 * @param url 请求地址
	 * @return 返回json或xml格式字符串
	 * @throws Exception 
	 */
	public static String requestByGetMethod(String url) throws Exception {
		logger.info("HttpClient执行代理GET请求 [{}]", url);
		// 创建CloseableHttpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		try {
			// 创建HttpClientBuilder工厂类
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			// 使用HttpClientBuilder工厂类创建HttpClient对象
			httpClient = httpClientBuilder.build();
			RequestConfig config = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).
					setConnectTimeout(CONNECTION_TIMEOUT).build();
			//请求地址，将请求字符串转化成URI对象
			URL httpUrl = new URL(url);
			URI uri = new URI(httpUrl.getProtocol(), httpUrl.getHost(), httpUrl.getHost(), httpUrl.getPort(), 
					httpUrl.getPath(), httpUrl.getQuery(), null);
			//创建HttpGet对象，发起GET请求
			HttpGet httpGet = new HttpGet(uri);
			//设置请求超时时间
			httpGet.setConfig(config);
			//调用httpClient对象的execute方法执行get请求
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            //获取响应内容
			HttpEntity httpEntity = httpResponse.getEntity();
			if (null != httpEntity) {
				// 打印响应内容
				logger.info("HttpGet请求响应状态码 :[{}]", httpResponse.getStatusLine().getStatusCode());
				String result = EntityUtils.toString(httpEntity, DEFAULT_ENCODING);
				logger.info("HttpGet请求响应内容 :[{]]", result);
				return result;
			}
		} catch (Exception e) {
			logger.error("HttpClient执行代理GET请求异常:{}", e.getMessage(), e);
			throw new Exception("外部服务器异常，请稍后再试");
		} finally {
			try {
				//容器会在request处理结束后回收httpResponse资源，此处可以不调用httpResponse的close方法
				//释放httpClient资源
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * POST方式发起http请求
	 * @param url 请求地址
	 * @param paramsMap 请求参数
	 * @return  返回json或xml格式字符串
	 * @throws UnsupportedEncodingException 
	 */
	public static String requestByPostMethod(String url, Map<String, Object> paramsMap) throws Exception {
		logger.info("HttpClient执行代理POST请求 URL:[{}] Params:[{}]", url, String.valueOf(paramsMap));
		// HttpClient
		CloseableHttpClient httpClient = null;

		try {
			// 创建HttpClientBuilder工厂类
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			// 使用HttpClientBuilder工厂类创建HttpClient对象
			httpClient = httpClientBuilder.build();
			//设置超时时间
			RequestConfig config = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).
					setConnectTimeout(CONNECTION_TIMEOUT).build();
			// 请求地址
			URL httpUrl = new URL(url);
			URI uri = new URI(httpUrl.getProtocol(), httpUrl.getHost(), httpUrl.getHost(), httpUrl.getPort(), 
					httpUrl.getPath(), httpUrl.getQuery(), null);
			//创建HttpPost对象，发起POST请求
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setConfig(config);
			// 创建参数队列
			if (null != paramsMap && paramsMap.size() > 0) {
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				NameValuePair nameValuePair = null;
				
				for (String paramName : paramsMap.keySet()) {
					if (null != paramsMap.get(paramName)) {
						nameValuePair = new BasicNameValuePair(paramName, String.valueOf(paramsMap.get(paramName)));
						formparams.add(nameValuePair);
					}
				}
				//设置请求实体，默认请求参数编码UTF-8
				UrlEncodedFormEntity entity = null;
				if (null != formparams) {
					entity = new UrlEncodedFormEntity(formparams, DEFAULT_ENCODING);
					httpPost.setEntity(entity);
				}
			}
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			//获取响应内容
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				// 打印响应内容
				logger.info("HttpPost请求响应状态码:[{}]", httpResponse.getStatusLine().getStatusCode());
				String result = EntityUtils.toString(httpEntity, DEFAULT_ENCODING);
				logger.info("HttpPost请求响应内容 :[{}]", result);
				return result;
			}
		} catch (Exception e) {
			logger.error("HttpClient执行代理POST请求异常:{}", e.getMessage(), e);
			throw new Exception("外部服务器异常，请稍后再试");
		} finally {
			try {
				//容器会在request处理结束后回收httpResponse资源，此处可以不调用httpResponse的close方法
				//释放httpClient资源
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
