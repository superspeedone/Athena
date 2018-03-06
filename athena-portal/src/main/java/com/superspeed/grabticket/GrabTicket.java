package com.superspeed.grabticket;

import com.superspeed.grabticket.factory.GrabTicketFactory;
import com.superspeed.grabticket.pojo.AreaCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrabTicket {

    private Logger logger = Logger.getLogger(GrabTicket.class.getName());

    private TreeMap<String, AreaCode> areaCodesMap;

    public GrabTicket() {
        // 初始化地区信息
        initAreaCode();

    }

    private void initAreaCode() {
        try {
            String areaConfig = GrabTicketFactory.class.getClassLoader().getResource("district.config").getPath();
            FileReader reader = new FileReader(new File(areaConfig));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String areaCodeStr = bufferedReader.readLine();
            if (areaCodeStr != null) {
                areaCodesMap = new TreeMap<String, AreaCode>();
                String[] areaCodeArr = areaCodeStr.split("\\@");
                if (areaCodeStr != null && areaCodeArr.length > 0) {
                    AreaCode areaCode;
                    for (int i = 0; i < areaCodeArr.length; i++) {
                        // 遍历循环解析区域信息
                        String[] areainfo =  areaCodeArr[i].split("\\|");
                        areaCode = new AreaCode(areainfo[0], areainfo[1], areainfo[2], areainfo[5]);
                        areaCodesMap.put(areainfo[1], areaCode);
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "initializing area config errors.", e);
        }
    }

    public TreeMap<String, AreaCode> getAreaCodesMap() {
        return areaCodesMap;
    }
}
