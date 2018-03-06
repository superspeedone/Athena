package com.superspeed.common.weixin.utils;

import com.superspeed.common.weixin.pojo.News;
import com.superspeed.common.weixin.pojo.NewsMessage;
import com.superspeed.common.weixin.pojo.TextMessage;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * 消息工具类
 * @author yww
 *
 */
public class MessageUtil {
	/**
	 * 消息类型常量
	 */
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_NEWS = "news";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_MUSIC = "music";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	public static final String MESSAGE_SCANCODE= "scancode_push";
	
	public static List<News> news = null;
	public static String helpMenu = null;
	
	/**
	 * 从xml文件读取图文消息
	 * @return
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	public static List<News> getNews() throws DocumentException {
		if (news  == null){
			news = new ArrayList<News>();
			// 读取NewMessage.xml图文消息
			InputStream in = WeixinUtil.class.getClassLoader().getResourceAsStream("news.xml");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			// 获取所有New节点
			Element root = doc.getRootElement();
			List<Element> list = root.elements();
			News ne = null;
			for (Element e : list) {
				ne = new News();
				ne.setTitle(e.elementText("Title"));
				ne.setDescription(e.elementText("Description"));
				ne.setPicUrl(e.elementText("PicUrl"));
				ne.setUrl(e.elementText("Url"));
				news.add(ne);
			}
		}
		return news;
	}
	
	public static String getHelpMenu() throws IOException {
		String helptext = "";
		if(helpMenu == null) {
			InputStream in = WeixinUtil.class.getClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			properties.load(in);
			helptext = properties.getProperty("help");
		}
		return helptext;
	}

	/**
	 * 消息xml字符串转换成Map集合
	 * @param request
	 * @return
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMap(HttpServletRequest request)
			throws DocumentException {
		Map<String, String> map = new HashMap<String, String>();
		try {
			SAXReader reader = new SAXReader();
			InputStream in;
			in = request.getInputStream();
			Document doc = reader.read(in);

			Element root = doc.getRootElement();
			List<Element> list = root.elements();
			for (Element e : list) {
				map.put(e.getName(), e.getText());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 将文本消息转换成xml格式字符串
	 * @param textMessage
	 * @return xmlstring
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		XStream xStream = new XStream();
		xStream.alias("xml", textMessage.getClass());
		return xStream.toXML(textMessage);
	}
	
	/**
	 * 将图文消息对象转换成xml格式字符串
	 * @param newsMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}
	
	
	/**
	 * 组装文本消息
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initText(String toUserName, String fromUserName,
			String content) {
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MessageUtil.MESSAGE_TEXT);
		text.setCreateTime(System.currentTimeMillis());
		text.setContent(content);
		return textMessageToXml(text);
	}
	
	/**
	 * 组装图文消息
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 * @throws DocumentException 
	 */
	public static String initNewsMessage(String toUserName,String fromUserName) throws DocumentException{
		String message = null;
		NewsMessage newsMessage = new NewsMessage();

		List<News> newsList = getNews();
		
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(System.currentTimeMillis());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());
		
		message = newsMessageToXml(newsMessage);
		return message;
	}
	
	/**
	 * 拼接欢迎文本菜单消息
	 * @return
	 * @throws IOException 
	 */
	public static String menuText() throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(getHelpMenu().replace("\\n", "\n"));
		return sb.toString();
	}
	
	public static String firstMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("");
        return sb.toString();
	}
	
	public static String secondMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("");
        return sb.toString();
	}

}
