package com.superspeed.common.weixin.servlet;

import com.superspeed.common.weixin.utils.Config;
import org.apache.http.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemInit extends HttpServlet {
	private static final long serialVersionUID = 5480766647609692279L;

	@Override
	public void destroy() {
		System.out.println("微信接口关闭");
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		// 微信底部菜单跟随系统启动自动生成
		System.out.println("*********   微信接口正在启动   *********");
		try {
			System.out.println("*********   正在设置系统参数   *********");
			// 设置系统参数
			InputStream in = SystemInit.class.getClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			properties.load(in);
			Config.getInstance().setToken(properties.getProperty("token"));
			Config.getInstance().setAPPID(properties.getProperty("APPID"));
			Config.getInstance().setAPPSECRET(properties.getProperty("APPSECRET"));
			Config.getInstance().setStarupload(properties.getProperty("starupload"));
			System.out.println("*********   系统参数设置完成   *********");
			
			//2014年以后需要认证到订阅号才能调用自定义菜单接口 
			
			//如果为starupload为1，则重新生成菜单，为0，不做任何操作
//			if (Config.getInstance().getStarupload().equals("1")) {
//				// 删除旧菜单
//				int result = WeixinUtil.deleteMenu(WeixinUtil.getAccessToken().getToken());
//				if (result == 0) {
//					System.out.println("*********   删除旧菜单成功   *********");
//				} else {
//					System.out.println("*********   删除旧菜单失败，错误代码：" + result + "   *********");
//				}
//				// 创建菜单
//				result = WeixinUtil.createMenu(WeixinUtil.getAccessToken().getToken(),
//								JSONObject.fromObject(WeixinUtil.initMenu()).toString());
//				if (result == 0) {
//					System.out.println("*********  创建新菜单成功   *********");
//				} else {
//					System.out.println("*********   创建新菜单失败，错误代码：" + result +"   *********");
//				}
//			}
			System.out.println("*********   微信接口启动成功！ *********");
		} catch (ParseException e) {
			System.out.println("*********    微信接口启动失败！ *********");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("*********   微信接口启动失败！ *********");
			e.printStackTrace();
		}
//		} catch (DocumentException e) {
//			System.out.println("*********    微信接口启动失败！ *********");
//			e.printStackTrace();
//		}
	}

}
