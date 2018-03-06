package com.superspeed.test;

import com.superspeed.service.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboConsumerTest {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"META-INF/spring/dubbo-zs-portal.xml"});
        context.start();
        DemoService demoService = (DemoService) context.getBean("demoService"); // obtain proxy object for remote invocation
        String hello = demoService.sayHello("world"); // execute remote invocation
        System.out.println(hello); // show the result
    }
}
