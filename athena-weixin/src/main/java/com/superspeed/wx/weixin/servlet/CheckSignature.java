package com.superspeed.common.weixin.servlet;

import com.superspeed.common.weixin.utils.CheckUtil;
import com.superspeed.common.weixin.utils.MessageUtil;
import com.superspeed.common.weixin.utils.TulingRuboot;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CheckSignature extends HttpServlet {
	private static final long serialVersionUID = -6860837038200556757L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String signature = req.getParameter("signature");	
		String timestamp	 = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		
		PrintWriter out = resp.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.write(echostr);
		}
		out.flush();
		out.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			
			String message = null;
			if (MessageUtil.MESSAGE_TEXT.equals(msgType)) {
				/*if("1".equals(content)) {
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.firstMenu());
				} else if("2".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.secondMenu());
				} else if("？".equals(content) || "?".equals(content) || "帮助".equals(content) || "help".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}*/
				if("成绩查询".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, "JAVA程序设计：100分，英语：92分，数学：85分，成绩良好");
				}else if("？".equals(content) || "?".equals(content) || "帮助".equals(content) || "help".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if("产品链接".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, "http://kfzlt.viphk.ngrok.org/dubbo-consumer/weixin/payment/index");
				}else {
					message = MessageUtil.initText(toUserName, fromUserName, TulingRuboot.reply(content));
				}
				
			}else if (MessageUtil.MESSAGE_EVENT.equals(msgType)) {
				String eventType = map.get("Event");
				if (MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)) {
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					String key = map.get("EventKey");
					if (key.equals("11")){
						message = MessageUtil.initNewsMessage(toUserName, fromUserName);
					}else if(key.equals("12")){
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
					}
				}
			}
			System.out.println("message->"+message);
			out.print(message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			out.close();
		}
	}
	
}
