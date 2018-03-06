package com.superspeed.schemer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class ZsNamespaceHandler extends NamespaceHandlerSupport {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init() {

        logger.info("ZsNamespaceHandler");

        registerBeanDefinitionParser("beans", new ZsBeanDefinitionParser(ServiceBean.class, true));

    }

}
