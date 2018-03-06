package com.superspeed.common.weixin.utils;

import com.alibaba.fastjson.JSONObject;
import com.superspeed.common.util.ServletUtils;
import com.superspeed.common.weixin.menu.Button;
import com.superspeed.common.weixin.menu.ClickButton;
import com.superspeed.common.weixin.menu.Menu;
import com.superspeed.common.weixin.menu.ViewButton;
import com.superspeed.common.weixin.pojo.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 微信工具类
 *
 * @author yww
 */
public class WeixinUtil {
    private static final String APPID = Config.getInstance().getAPPID();
    private static final String APPSECRET = Config.getInstance().getAPPSECRET();

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    private static final String SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";

    private static final String QUERY_USER_GROUP_ID = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=ACCESS_TOKEN";

    private static final String UPLOAD_NEWS_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";

    //oauth2授权接口
    private static final String OAUTH2_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=RESPONSE_TYPE&scope=SCOPE&state=STATE#wechat_redirect";

    //oauth2获取token接口
    private static final String OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=GRANT_TYPE";

    private static final String USERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=LANG ";

    /**
     * get请求
     *
     * @param requestUrl
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static JSONObject doGetStr(String requestUrl) throws ParseException,
            IOException, URISyntaxException {
        System.out.println("http get url ->" + requestUrl);
        URL url = new URL(requestUrl);
        URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);
        JSONObject jsonObject = null;
        HttpResponse httpResponse = client.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            System.out.println("http get return -> " + result);
            jsonObject = JSONObject.parseObject(result);
        }
        return jsonObject;
    }

    /**
     * POST请求
     *
     * @param url
     * @param outStr
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static JSONObject doPostStr(String url, String outStr)
            throws ParseException, IOException {
        System.out.println("http post url ->" + url + "\n outStr->" + outStr);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);
        JSONObject jsonObject = null;
        httpost.setEntity(new StringEntity(outStr, "UTF-8"));
        HttpResponse response = client.execute(httpost);
        String result = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println("http post return -> " + result);
        jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    /**
     * 文件上传
     *
     * @param filePath
     * @param accessToken
     * @param type
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws KeyManagementException
     */
    public static String upload(String filePath, String accessToken, String type)
            throws IOException, NoSuchAlgorithmException,
            NoSuchProviderException, KeyManagementException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在");
        }

        String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace(
                "TYPE", type);

        URL urlObj = new URL(url);
        // 连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);

        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");

        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
                + BOUNDARY);

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
                + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head = sb.toString().getBytes("utf-8");

        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);

        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线

        out.write(foot);

        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        JSONObject jsonObj = JSONObject.parseObject(result);
        System.out.println(jsonObj);
        String typeName = "media_id";
        if (!"image".equals(type)) {
            typeName = type + "_media_id";
        }
        String mediaId = jsonObj.getString(typeName);
        return mediaId;
    }

    /**
     * 获取oauth2授权地址
     *
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static String getOauth2AuthorUrl(String redirectUrl) throws ParseException,
            IOException {
        //snsapi_userinfo：需要用户授权  snsapi_base：静默授权，只能获取部分用户信息
        HttpSession session = ServletUtils.getRequest().getSession();
        String state = session.getId() + System.currentTimeMillis();
        String authorUrl = OAUTH2_AUTHORIZE_URL.replace("APPID", APPID).replace(
                "RESPONSE_TYPE", "code").replace("REDIRECT_URI", redirectUrl).
                replace("SCOPE", "snsapi_base").replace("STATE", state);
        ServletUtils.getRequest().getSession().setAttribute("OAUTH2_STATE", state);
        System.out.println("oauth2用户授权链接 -> " + authorUrl);
        return authorUrl;
    }

    /**
     * 获取oauth2授权地址
     *
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static Authorization getOauth2AccessToken(String code) throws ParseException,
            IOException, URISyntaxException {
        Authorization authorization = new Authorization();
        String url = OAUTH2_ACCESS_TOKEN_URL.replace("APPID", APPID).replace(
                "SECRET", APPSECRET).replace("CODE", code).replace("GRANT_TYPE", "authorization_code");
        JSONObject jsonObject = doGetStr(url);
        if (jsonObject != null) {
            //{"access_token":"4_IdPnexxAhYVcDUL2r_pCNMpGNSfl8HsvaKvChl7qJQY7Ww_iy-kDzt0nEPVqEeXM_HYInvkLLnfgTtRSEPLqB-GX4Bn4FElH_Iz1wFAdPK0","expires_in":7200,"refresh_token":"4_-Bxo7AQ_jQbjXtM-h1t5ZxlSwz1v4ic49nTcprCqF6iWnEB2hLX_iW3ZHVrVx17-ir7WRm97csPiH6hiD8vuDTZRrZTmjlY5OnWpiWDl1zc","openid":"oGsOGv5WtflhWWS7HUSrwPBH3N4Y","scope":"snsapi_base"}
            authorization.setAccessToken(jsonObject.getString("access_token"));
            authorization.setExpiresin(jsonObject.getInteger("expires_in"));
            authorization.setRefreshToken(jsonObject.getString("refresh_token"));
            authorization.setOpenid(jsonObject.getString("openid"));
            authorization.setScope(jsonObject.getString("scope"));
            //当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。
            if (jsonObject.containsKey("unionid")) {
                authorization.setUnionid(jsonObject.getString("unionid"));
            }
        }
        return authorization;
    }

    /**
     * 获取oauth2授权地址
     *
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static Userinfo getUserinfo(String openid) throws ParseException,
            IOException, URISyntaxException {
        Userinfo userinfo = new Userinfo();
        String url = USERINFO_URL.replace("ACCESS_TOKEN", getAccessToken().getToken())
                .replace("OPENID", openid).replace("LANG", "zh_CN");
        JSONObject jsonObject = doGetStr(url);
        if (jsonObject != null) {
            userinfo.setSubscribe(jsonObject.getInteger("subscribe"));
            userinfo.setOpenid(jsonObject.getString("openid"));
            userinfo.setNickname(jsonObject.getString("nickname"));
            userinfo.setSex(jsonObject.getInteger("sex"));
            userinfo.setCity(jsonObject.getString("city"));
            userinfo.setCountry(jsonObject.getString("country"));
            userinfo.setProvince(jsonObject.getString("province"));
            userinfo.setLanguage(jsonObject.getString("language"));
            userinfo.setHeadimgurl(jsonObject.getString("headimgurl"));
            userinfo.setSubscribeTime(jsonObject.getLong("subscribe_time"));
            if (jsonObject.containsKey("unionid")) {
                userinfo.setUnionid(jsonObject.getString("unionid"));
            }
            userinfo.setRemark(jsonObject.getString("remark"));
            userinfo.setGroupid(jsonObject.getInteger("groupid"));
            userinfo.setTagidList(jsonObject.getString("tagid_list"));
        }
        return userinfo;
    }


    /**
     * 获取accessToken
     *
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static AccessToken getAccessToken() throws ParseException,
            IOException, URISyntaxException {
        AccessToken token = new AccessToken();
        if ((Config.getInstance().getLastTime()) == null || Config.getInstance().getAccessToken().equals("") ||
                ((System.currentTimeMillis() - Config.getInstance().getLastTime().getTime()) / (1000 * 60) > 100)) {
            String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace(
                    "APPSECRET", APPSECRET);
            JSONObject jsonObject = doGetStr(url);
            if (jsonObject != null) {
                token.setToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInteger("expires_in"));
                Config.getInstance().setAccessToken(jsonObject.getString("access_token"));
                Config.getInstance().setLastTime(new Date());
            }
        } else {
            token.setToken(Config.getInstance().getAccessToken());
            token.setExpiresIn(7200);
        }
        return token;
    }

    /**
     * 测试时获取token方法，每次获取一个新的access_token
     *
     * @param APPID
     * @param APPSECRET
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static AccessToken getAccessTokenTest(String APPID, String APPSECRET) throws ParseException,
            IOException, URISyntaxException {
        AccessToken token = new AccessToken();
        String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace(
                "APPSECRET", APPSECRET);
        JSONObject jsonObject = doGetStr(url);
        if (jsonObject != null) {
            System.out.println(JSONObject.toJSONString(jsonObject).toString());
            token.setToken(jsonObject.getString("access_token"));
            token.setExpiresIn(jsonObject.getInteger("expires_in"));
        }
        return token;
    }

    /**
     * 组装菜单
     *
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    @SuppressWarnings("unchecked")
    public static Menu initMenu() throws IOException, DocumentException {
        Menu menu = new Menu();
        // 读取menu.xml菜单内容
        InputStream in = WeixinUtil.class.getClassLoader().getResourceAsStream(
                "menu.xml");
        SAXReader reader = new SAXReader();
        Document doc = reader.read(in);
        // 获取所有button节点
        Element root = doc.getRootElement();
        List<Element> list = root.elements();
        // 所有菜单对象
        Button[] buttons = new Button[list.size()];
        ClickButton cButton = null;
        ViewButton vButton = null;
        Button button = null;
        // 遍历所有button节点
        for (int i = 0; i < list.size(); i++) {
            Element e = list.get(i);
            if (e.element("sub_button") == null) {
                //解析一级菜单
                parseMenu(buttons, e, i, cButton, vButton);
            } else {
                button = new Button();
                button.setName(e.element("name").getTextTrim());
                // 获取子菜单节点
                List<Element> subList = e.element("sub_button").elements();
                Button[] subButtons = new Button[subList.size()];
                for (int j = 0; j < subList.size(); j++) {
                    Element se = subList.get(j);
                    //解析二级菜单
                    parseMenu(subButtons, se, j, cButton, vButton);
                }
                //将子菜单添加到父菜单中
                button.setSub_button(subButtons);
                //添加到一级菜单
                buttons[i] = button;
            }
        }
        menu.setButton(buttons);
        in.close();
        return menu;
    }

    /**
     * 解析xml中button节点
     *
     * @param buttons
     * @param e
     * @param index
     * @param cButton
     * @param vButton
     */
    public static void parseMenu(Button[] buttons, Element e, int index,
                                 ClickButton cButton, ViewButton vButton) {
        if (e.element("type").getTextTrim().equals("click")) {
            cButton = new ClickButton();
            cButton.setName(e.element("name").getTextTrim());
            cButton.setType(e.element("type").getTextTrim());
            cButton.setKey(e.element("key").getTextTrim());
            buttons[index] = cButton;
        } else if (e.element("type").getTextTrim().equals("view")) {
            vButton = new ViewButton();
            vButton.setName(e.element("name").getTextTrim());
            vButton.setType(e.element("type").getTextTrim());
            vButton.setUrl(e.element("url").getTextTrim());
            buttons[index] = vButton;
        }
    }

    /**
     * 创建菜单
     *
     * @param token
     * @param menu
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static int createMenu(String token, String menu)
            throws ParseException, IOException {
        int result = 0;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doPostStr(url, menu);
        if (jsonObject != null) {
            result = jsonObject.getInteger("errcode");
        }
        return result;
    }

    /**
     * 查询菜单
     *
     * @param token
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static JSONObject queryMenu(String token) throws ParseException,
            IOException, URISyntaxException {
        String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doGetStr(url);
        return jsonObject;
    }

    /**
     * 删除菜单
     *
     * @param token
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static int deleteMenu(String token) throws ParseException,
            IOException, URISyntaxException {
        String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doGetStr(url);
        int result = 0;
        if (jsonObject != null) {
            result = jsonObject.getInteger("errcode");
        }
        return result;
    }

    /**
     * 推送文本消息（发给所有人或分组发送）
     *
     * @param token
     * @param sendTextMessage
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static int sendTextToAll(String token, String sendTextMessage) throws ParseException,
            IOException {
        System.out.println(sendTextMessage);
        String url = SEND_MESSAGE_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doPostStr(url, sendTextMessage);
        int result = 0;
        if (jsonObject != null) {
            result = jsonObject.getInteger("errcode");
            System.out.println(JSONObject.toJSONString(jsonObject).toString());
        }
        return result;
    }

    /**
     * 推送文本消息拼接
     *
     * @return
     */
    public static SendTextMessage initSendTex() {
        Text text = new Text();
        text.setContent("测试群发文本消息2");
        Filter filter = new Filter();
        filter.setIs_to_all(false);
        filter.setGroup_id(0);
        SendTextMessage textMessage = new SendTextMessage();
        textMessage.setFilter(filter);
        textMessage.setText(text);
        textMessage.setMsgtype("text");
        return textMessage;
    }

    /**
     * 查询用户分组ID
     *
     * @param token
     * @param OpenID
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static int getUserGroupId(String token, String OpenID)
            throws ParseException, IOException {
        String url = QUERY_USER_GROUP_ID.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doPostStr(url, OpenID);
        int result = 0;
        if (jsonObject != null) {
            if (jsonObject.containsKey("groupid")) {
                result = jsonObject.getInteger("groupid");
            } else {
                result = jsonObject.getInteger("errcode");
            }
        }
        return result;
    }

    /**
     * 上传图文消息
     *
     * @param token
     * @param articleMessage
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static String uploadNews(String token, String articleMessage)
            throws ParseException, IOException {
        String url = UPLOAD_NEWS_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doPostStr(url, articleMessage);
        String result = "";
        if (jsonObject != null) {
            System.out.println(jsonObject.toString());
            if (jsonObject.containsKey("media_id")) {
                result = jsonObject.getString("media_id");
            } else {
                result = String.valueOf(jsonObject.getInteger("errcode"));
            }
        }
        return result;
    }

    /**
     * 图文消息素材组拼
     *
     * @return
     */
    public static ArticleMessage initArticle() {
        ArticleMessage articleMessage = new ArticleMessage();
        List<Articles> list = new ArrayList<Articles>();
        Articles articles = new Articles();
        articles.setThumb_media_id("uT1Scf7ADM7YfZ_jyMBpcb9hOVYXUmuBvdf5hnNAepQf206fSx0ZPfdXdHz8mfw3");
        articles.setTitle("有奖问答报名申请");
        articles.setAuthor("公益活动");
        articles.setContent_source_url("https://jinshuju.net/f/jpb4eJ");
        articles.setContent("”来赛不来赛“知识问答请设计30题趣味题和先进智能制造方面的（答题对象体验活动的家长和学生），每次自动产生8个题目，回答正确超过6题的凭判分页面可以领取小奖品一份");
        articles.setDigest("来赛不来赛");
        articles.setShow_cover_pic(1);
        list.add(articles);
        articleMessage.setArticles(list);
        return articleMessage;
    }

    /**
     * 推送图文消息（发给所有人或分组发送）
     *
     * @param token
     * @param news
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static int sendNewsMessage(String token, String news)
            throws ParseException, IOException {
        String url = SEND_MESSAGE_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doPostStr(url, news);
        int result = 0;
        if (jsonObject != null) {
            System.out.println(jsonObject.toString());
            result = jsonObject.getInteger("errcode");
        }
        return result;
    }

    /**
     * 组拼推送图文消息
     *
     * @return
     */
    public static SendNewsMessage initSendNews() {
        SendNewsMessage newsMessage = new SendNewsMessage();
        Filter filter = new Filter();
        filter.setIs_to_all(false);
        filter.setGroup_id(0);
        MpNews mpnews = new MpNews();
        mpnews.setMedia_id("JfZOHDdrpUyyaS2E9XqKU-eKRg321VJljQy_98Ug8dvLO2cl7vPsWHFkdq5fSi8w");
        newsMessage.setFilter(filter);
        newsMessage.setMpnews(mpnews);
        newsMessage.setMsgtype("mpnews");
        System.out.println(JSONObject.toJSONString(newsMessage).toString());
        return newsMessage;
    }
}
