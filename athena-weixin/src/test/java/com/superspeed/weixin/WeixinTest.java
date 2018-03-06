package com.superspeed.weixin;


import com.superspeed.common.weixin.utils.TulingRuboot;

public class WeixinTest {
	private static final String APPID = "wx7e1c8418fc9f3887";
	private static final String APPSECRET = "7c558ec9f596a53d1896b00df4dc52ef";
	public static void main(String[] args) {
		try {
			//AccessToken token = WeixinUtil.getAccessTokenTest(APPID, APPSECRET);
			//System.out.println("票据"+token.getToken());
			//System.out.println("有效时间"+token.getExpiresIn());
			
			/*String path = "D:/imooc_thumb.png";
			String mediaId = WeixinUtil.upload(path, token.getToken(), "thumb");
			System.out.println(mediaId);*/
			
			//String result = WeixinUtil.translate("my name is laobi");
			//String result = WeixinUtil.translateFull("");
			//System.out.println(result);
			//删除菜单
			/*int result = WeixinUtil.deleteMenu(WeixinUtil.getAccessToken().getToken());
			if(result == 0) {
				System.out.println("删除菜单成功");
			}else {
				System.out.println("删除菜单失败，错误代码：" + result);
			}*/
			
			//创建菜单
			/*int result = WeixinUtil.createMenu(WeixinUtil.getAccessToken().getToken(), 
					  JSONObject.fromObject(WeixinUtil.initMenu()).toString());
			if(result == 0) {
				System.out.println("创建菜单成功");
			}else {
				System.out.println("创建菜单失败，错误代码：" + result);
			}*/
			
			/*int result = WeixinUtil.sendTextToAll(WeixinUtil.getAccessTokenTest(APPID, APPSECRET).getToken(), 
					   JSONObject.fromObject(WeixinUtil.initSendTex()).toString());
			System.out.println(result);*/
			/*int groupID = WeixinUtil.getUserGroupId(WeixinUtil.getAccessTokenTest(APPID, APPSECRET).getToken(), 
					"{\"openid\":\"oGsOGv5WtflhWWS7HUSrwPBH3N4Y\"}");
			System.out.println(groupID);*/
			/*String result = WeixinUtil.uploadNews(token.getToken(), JSONObject.fromObject(WeixinUtil.initArticle()).toString());
			System.out.println(result);*/
			System.out.println(TulingRuboot.reply("上海地交通图"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
