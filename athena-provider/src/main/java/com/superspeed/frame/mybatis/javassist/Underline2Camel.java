package com.superspeed.frame.mybatis.javassist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 驼峰法-下划线互转
 * @author xc.yanww
 * @ClassName: Underline2Camel
 * @Description: 驼峰法-下划线互转
 * @date 2018-04-02 15:52
 */
public class Underline2Camel {

    private static final String SEPARATOR = "_";
    private static final String CAMEL_PATTEN = "([A-Za-z\\d]+)(_)?";
    private static final String UNDERLINE_PATTEN = "[A-Z]([a-z\\\\d]+)?";

    /**
     * 下划线转驼峰法
     * @param line       源字符串
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line) {
        return underline2Camel(line, true);
    }

    /**
     * 下划线转驼峰法
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(CAMEL_PATTEN);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0))
                    : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf(SEPARATOR);
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(UNDERLINE_PATTEN);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : SEPARATOR);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String line="I_HAVE_AN_IPANG3_PIG";
        String camel=underline2Camel(line,true);
        System.out.println(camel);
        System.out.println(camel2Underline(camel));
    }

}
