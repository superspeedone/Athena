package com.superspeed.service.impl;

import com.superspeed.service.DemoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {

    private Logger logger = LogManager.getLogger(DemoServiceImpl.class);

    @Override
    public String sayHello(String name) {
        logger.info("Hello " + name + "!");

        return "Hello " + name + "!";
    }
}
