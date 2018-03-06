package com.superspeed.common.controller.weixin;

import com.superspeed.common.WxPayment;
import com.superspeed.common.controller.base.BaseController;
import com.superspeed.common.kit.HttpKit;
import com.superspeed.common.kit.StrKit;
import com.superspeed.common.model.AjaxResult;
import com.superspeed.common.model.WxPayOrder;
import com.superspeed.common.model.WxResult;
import com.superspeed.common.weixin.pojo.Authorization;
import com.superspeed.common.weixin.pojo.Userinfo;
import com.superspeed.common.weixin.utils.WeixinUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Enumeration;

/**
 * Created by yanweiwen on 2017/12/6.
 */
@Controller
@RequestMapping(value = "weixin/payment")
public class WxPayController extends BaseController {

    private AjaxResult ajax = new AjaxResult();

    @RequestMapping(value = "login")
    public String login() throws Exception {
        try {
            Object openId = getSession().getAttribute("openId");
            if (null == openId || "".equals(openId.toString().trim())) {
                String code = getRequest().getParameter("code");
                String state = getRequest().getParameter("state");
                if (null == code || "".equals(code.toString().trim())) {
                    return "redirect:" + WeixinUtil.getOauth2AuthorUrl(getRequestUrl());
                } else {
                    Object oauth2State = getSession().getAttribute("OAUTH2_STATE");
                    if (null == oauth2State || null == state || !state.equals(oauth2State)) {
                        throw new IllegalStateException("授权失败");
                    }
                    //获取opendid
                    Authorization authorization = WeixinUtil.getOauth2AccessToken(code);
                    System.out.println("授权成功，openid -> " + authorization.getOpenid());
                    getSession().setAttribute("openId", authorization.getOpenid());
                    getSession().setAttribute("oauth2_authorization", authorization);
                }
            }
        } catch (Exception e) {
            log.error("获取oauth2授权地址失败", e);
            throw e;
        }
        return "redirect:/weixin/payment/index";
    }

    @RequestMapping(value = "index")
    public String index() throws Exception {
        Object openId = getSession().getAttribute("openId");
        if (null == openId || "".equals(openId.toString().trim())) {
            return "redirect:/weixin/payment/login";
        }
        //获取用户信息
        try {
            Userinfo userinfo = WeixinUtil.getUserinfo((String) openId);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            throw e;
        }
        return "weixin/payment/index";
    }

    @ResponseBody
    @RequestMapping(value = "transorder")
    public AjaxResult transorder() {
        try {
            // openId，采用 网页授权获取 access_token API：SnsAccessTokenApi获取
            String openId = (String) getSession().getAttribute("openId");
            String total_fee = getParameter("total_fee");
            if (StrKit.isBlank(total_fee)) {
                ajax.addError("请输入数字金额");
                return ajax;
            }
            System.out.format("openId -> %s, amount -> %s \n", openId, total_fee);
            String out_trade_no = System.currentTimeMillis() + "";
            WxPayOrder order = new WxPayOrder();
            order.setOpenid(openId);
            order.setBody("Javen微信公众号极速开发");
            order.setOutTradeNo(out_trade_no);
            order.setTotalFee("0.1");
            /*order.setAttach(JsonKit.toJson(new PayAttach(out_trade_no, 2, 3)));*/
            order.setSpbillCreateIp("127.0.0.1");
            /*if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
                ajax.addError(return_msg);
                return ajax;
            }
            String result_code = result.get("result_code");
            if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
                ajax.addError(return_msg);
                return ajax;
            }
            // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
            String prepay_id = result.get("prepay_id");*/
            ajax.success("");
        } catch (Exception e) {
            log.error("Payment exception:", e);
            ajax.addError(e.getMessage());
        }
        return ajax;
    }

    @RequestMapping(value = "notify")
    @ResponseBody
    public String pay_notify() {
        try {
            //获取所有的参数
            StringBuffer sbf = new StringBuffer();
            Enumeration<String> en = getParameterNames();
            while (en.hasMoreElements()) {
                Object o = en.nextElement();
                sbf.append(o.toString() + "=" + getParameter(o.toString()));
            }
            log.error("支付通知参数：" + sbf.toString());
            String xmlMsg = HttpKit.readData(getRequest());
            System.out.println("支付通知=" + xmlMsg);
            WxResult wxResult = new WxPayment().paynotify(xmlMsg);
            return wxResult.getData().toString();
        } catch (Exception e) {
            log.error("支付通知异常.", e);
            return "";
        }
    }

}
