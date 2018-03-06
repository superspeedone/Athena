package com.superspeed.test.DynamicProxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

public class Manufactor implements MethodInterceptor {

    public Object getInstance(Class clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("Manufactor start");
        methodProxy.invokeSuper(proxy, args);
        System.out.println("Manufactor end");

        byte[] bytes = ProxyGenerator.generateProxyClass(proxy.getClass().getSimpleName(),
                proxy.getClass().getClasses());
        String classFilePath = "C:\\Users\\xc.yanww\\Desktop\\Merchant$$EnhancerByCGLIB$$cefd0f7.class";
        FileOutputStream fos = new FileOutputStream(classFilePath);
        fos.write(bytes);
        fos.flush();
        return null;
    }


}
