package com.superspeed.grabticket;

import com.superspeed.grabticket.pojo.HttpRequestResult;
import com.superspeed.util.HttpClientProxy;

public class GrabticketTest {

    public static void main(String[] args) {
        /*GrabTicket grabTicket = GrabTicketFactory.getInstance();
        TreeMap<String, AreaCode> areaCodesMap = grabTicket.getAreaCodesMap();
        Set<Map.Entry<String,AreaCode>> entries = areaCodesMap.entrySet();
        for (Map.Entry<String,AreaCode> entry: entries) {
            System.out.println("Area : " + entry.getKey() + "  " + entry.getValue());
        }*/

        try {
            HttpRequestResult httpRequestResult = HttpClientProxy.requestByGetMethod("https://kyfw.12306.cn/otn/login/init");
            System.out.println(httpRequestResult.getData().indexOf("/otn/dynamicJs/"));

        } catch (Exception e) {
            e.printStackTrace();
        }




    }

}
