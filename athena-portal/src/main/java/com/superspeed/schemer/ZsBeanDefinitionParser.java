package com.superspeed.schemer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ZsBeanDefinitionParser implements BeanDefinitionParser {
    private static Logger logger = LoggerFactory.getLogger(ZsBeanDefinitionParser.class);

    private final Class<?> beanClass;
    private final boolean required;

    public ZsBeanDefinitionParser(Class<?> beanClass, boolean required) {
        this.beanClass = beanClass;
        this.required = required;
    }


    private static BeanDefinition parse(Element element, ParserContext parserContext, Class<?> beanClass, boolean required) {
        logger.info("BeanDefinition parse.");

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        String id = element.getAttribute("id");

        return beanDefinition;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        return parse(element, parserContext, beanClass, required);
    }

}
