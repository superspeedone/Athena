package com.superspeed.test;

import com.superspeed.service.DemoService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RMIInvoke {

    public static void main(String[] args) {

        try {
            Context namingContext = new InitialContext();// 初始化命名内容
            DemoService demoService = (DemoService) namingContext
                    .lookup("rmi://127.0.0.1:1099/zs-provider");//获得远程对象的存根对象
            System.out.println(demoService.sayHello("haha"));//通过远程对象，调用doSomething方法
        } catch (NamingException e) {
            e.printStackTrace();
        }

    }
}
