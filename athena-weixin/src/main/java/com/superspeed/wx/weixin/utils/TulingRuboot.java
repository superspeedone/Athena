package com.superspeed.common.weixin.utils;


import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TulingRuboot {
	
	private static final String APIKEY = "da35aa4d66880783f337955b7a768537";
    
	public static String reply(String sayText){
		String text = "";
		try {
			String INFO = URLEncoder.encode(sayText, "utf-8");
			String getURL = "http://47.94.75.98/openapi/api?key="
					+ APIKEY + "&info=" + INFO;
			URL getUrl = new URL(getURL);
			HttpURLConnection connection = (HttpURLConnection) getUrl
					.openConnection();
			connection.connect();

			// 取得输入流，并使用Reader读取
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			// 断开连接
			connection.disconnect();
			System.out.println(sb.toString());
			JSONObject jsonObject = JSONObject.parseObject(sb.toString());
			if(jsonObject.containsKey("url")){
				text = jsonObject.getString("text")+"，点击链接地址查看内容："+jsonObject.getString("url");
			}else{
				text = jsonObject.getString("text");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return text;
	}
	
}
