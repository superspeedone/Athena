package com.superspeed.test.dubbo.provider;

public class Provider {
    public static void main(String[] args) throws Exception {
        /*ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"META-INF/spring/dubbo-zs-provider.xml"});
        context.start();
        System.in.read(); // press any key to exit*/
        //dubbo自带容器
        com.alibaba.dubbo.container.Main.main(args);
    }
}
