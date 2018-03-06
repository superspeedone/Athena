package com.superspeed.test.DynamicProxy.jdk;

import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 厂家
 */
public class Manufactor implements InvocationHandler {

    private Person target;

    public Object newInstance(Person target) {
        this.target = target;
        Class clazz = target.getClass();
        Object object = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
        return object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理商：" + target.getName());
        method.invoke(target, args);
        System.out.println("生产商批发东西");

        byte[] bytes = ProxyGenerator.generateProxyClass(proxy.getClass().getSimpleName(), proxy.getClass()
                .getInterfaces());

        FileOutputStream fos = new FileOutputStream("C:\\Users\\xc.yanww\\Desktop\\$Proxy0.class");
        fos.write(bytes);
        fos.flush();

        return null;
    }


}
