package com.superspeed.common;

/**
 * Created by yanweiwen on 2018/4/23.
 */
public class StringTest {

    public static void main(String[] args) {

        String a = "123"; // 对象存在字符串常量区
        String b = new String("123");  // 对象引用存在堆区
        String c = new String("123");
        String d = "123"; // 字符常量区已存在该值，直接返回引用

        System.out.println(a == b);
        System.out.println(b == c);
        System.out.println(a == d);

    }

}
