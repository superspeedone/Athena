package com.superspeed.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 * @ClassName: DigestUtils
 * @Description: 提供各种加密方式
 * @author xc.yanww
 * @date 2018/3/20 15:40
 */
public class DigestUtils {

    private DigestUtils() {}

    /**
     * 生成32位md5码（jdk自带）  加盐方式加密
     * @author xc.yanww
     * @date 2018/3/20 15:46
     * @param sourceStr 要加密的字符串
     * @return String 加密字符串
     */
    public static String md5WithSalt(String sourceStr) {

        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(sourceStr.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 生成32位md5码（jdk自带） 普通方式加密
     * @author xc.yanww
     * @date 2018/3/20 15:42
     * @param sourceStr 要加密的字符串
     * @return String 加密字符串
     */
    public static String md5(String sourceStr) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = sourceStr.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("DigestUtils");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

}
