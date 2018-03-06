package com.superspeed.test.jvm;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

public class RuntimeConstantPoolOOM {

    public static void main(final String[] args) {

        /*int i = 10000000;

        List<String> stringList = new ArrayList<>();

        while (true) {
            stringList.add(String.valueOf(i++).intern());
        }*/

        /*String str1 = new StringBuilder().append("范德萨发的").append("地方").toString();
        System.out.println(str1 == str1.intern());

        String str2 = new StringBuilder().append("fdsfd").append("sfdsfd").toString();
        System.out.println(str1.intern() == str2);*/

        //cglib动态加载类
       /* while (true) {*/
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws
                        Throwable {
                    System.out.println("call start");
                    Object result = methodProxy.invokeSuper(proxy, args);
                    System.out.println("call end");

                    byte[] bytes = ProxyGenerator.generateProxyClass(proxy.getClass().getSimpleName(), proxy.getClass()
                            .getInterfaces());

                    FileOutputStream fos = new FileOutputStream("C:\\Users\\xc.yanww\\Desktop\\$Proxy0.class");
                    fos.write(bytes);
                    fos.flush();

                    return result;
                }
            });
        OOMObject oomObject = (OOMObject) enhancer.create();
        oomObject.sayHello("cglib");
        /*}*/

    }

    /**
     * 被代理类
     */
    static class OOMObject {

        public void sayHello(String name) {
            System.out.println("hello " + name);
        }

    }

}
