package com.superspeed.common;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    private static String EMAIL_PATTRN = "([a-z|A-Z|0-9])+@([a-z|A-Z])+\\.([a-z|A-Z])+";

    private static String WEBSITE_PATTRN = "http://\\w\\.([a-z|A-Z])+";

    public static void main(String[] args) {
        RegexTest regexTest = new RegexTest();
        Pattern pattern = Pattern.compile(WEBSITE_PATTRN);
        Matcher matcher = pattern.matcher("http://baidu.com,http://www.baidu.com");
        while (matcher.find()) {
            System.out.println(matcher.group(0));
        }

    }


}
