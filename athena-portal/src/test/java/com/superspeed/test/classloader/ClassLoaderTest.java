package com.superspeed.test.classloader;

import java.lang.reflect.Constructor;

public class ClassLoaderTest {

    public static void main(String[] args) {

        try {

            CustomedClassLoader classLoader = new CustomedClassLoader();
            /*Class<?> clazz = Class.forName("People", true, classLoader);*/
            Class<?> clazz = classLoader.findClass("People");
            /*Object obj =  clazz.newInstance();*/
            Constructor constructor = clazz.getConstructor(String.class);
            Object obj = constructor.newInstance("David");

            System.out.println(obj);
            System.out.println(obj.getClass().getClassLoader());
            System.out.println(obj.getClass().getClassLoader().getParent());

            System.out.println("ClassLoader.class权限定名->" + ClassLoader.class.getName());
            System.out.println("ClassLoader获取资源根路径->" + ClassLoader.class.getResource("/").getPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
