package com.superspeed.test.dubbo.provider;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.superspeed.service.DemoService;
import com.superspeed.service.impl.DemoServiceImpl;

import java.io.IOException;

/**
 * 编程式方式启动dubbo服务
 */
public class Bootstap {

    public static void main(String[] args) {

        // 服务提供
        DemoService demoService = new DemoServiceImpl();

        // Application dubbo服务应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("zs-provider");

        // 注册中心Registry注册配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://10.7.187.217:2181");

        // 调用协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(20880);
        protocolConfig.setThreads(200);

        // 服务注册于发布暴漏
        ServiceConfig<DemoService> service = new ServiceConfig<DemoService>();
        service.setApplication(applicationConfig);
        service.setRegistry(registryConfig);
        service.setProtocol(protocolConfig);
        service.setInterface(DemoService.class);
        service.setRef(demoService);
        service.setVersion("1.0.0");

        service.export();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
