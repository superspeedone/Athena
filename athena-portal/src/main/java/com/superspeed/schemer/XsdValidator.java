package com.superspeed.schemer;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 单个xsd文件校验
 */
public class XsdValidator {

    private static final String SCHEMA_LANGUAGE_NAME = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String SCHEMA_LANGUAGE_VALUE = "http://www.w3.org/2001/XMLSchema";
    private static final String SCHEMA_SOURCE_VALUE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    private XsdValidator() {}

    public static void xsdValidate(String xmlPath, String xsdPath) {

        try {
            // 错误消息处理类
            XMLErrorHandler errorHandler = new XMLErrorHandler();
            // 获得解析器工厂类
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // 在解析XML是进行验证
            factory.setValidating(true);
            // 支持命名空间
            factory.setNamespaceAware(true);
            // 获得解析器
            SAXParser parser = factory.newSAXParser();
            parser.setProperty(SCHEMA_LANGUAGE_NAME, SCHEMA_LANGUAGE_VALUE);
            parser.setProperty(SCHEMA_SOURCE_VALUE, "file:" + xsdPath);
            SAXReader reader = new SAXReader();
            // 读取XML文件
            Document document = reader.read(new File(xmlPath));
            // 获得校验器
            SAXValidator validator = new SAXValidator(parser.getXMLReader());
            // 发生错误时得到相关信息
            validator.setErrorHandler(errorHandler);
            // 进行校验
            validator.validate(document);
            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            // 通过是否有错误信息判断校验是否匹配
            if (errorHandler.getErrors().hasContent()) {
                System.out.println("XML文件通过XSD文件校验失败！");
                writer.write(errorHandler.getErrors());
            } else {
                System.out.println("XML文件通过XSD文件校验成功！");
            }
        } catch (Exception e) {
            System.out.println("XML文件通过XSD文件校验失败！");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String xmlPath = XsdValidator.class.getClassLoader().getResource("application.xml").getPath();
        String xsdPath = XsdValidator.class.getClassLoader().getResource
                ("com/gupaoedu/vip/mvc/framework/context/protal.xsd").getPath();
        xsdValidate(xmlPath, xsdPath);
    }

}
