package com.superspeed.test.springframework;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;

/**
 * spring Ioc 启动源码分析
 *
 * @author xc.yanww
 * @date 2017/11/3 15:43
 */
public class SpringIocBootstrap {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("classpath:model.xml");
        System.out.println(applicationContext.getBeanDefinitionCount());
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
        /*DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        ClassPathResource classPathResource = new ClassPathResource("model.xml");
        beanDefinitionReader.loadBeanDefinitions(classPathResource);
        System.out.println(beanFactory.getBeanDefinitionCount());
        System.out.println(Arrays.toString(beanFactory.getBeanDefinitionNames()));*/

    }

}
