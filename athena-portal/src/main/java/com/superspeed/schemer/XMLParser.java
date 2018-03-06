package com.superspeed.schemer;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 多个xsd文件校验
 */
public final class XMLParser {

    private XMLParser() {
    }

    public static boolean validateWithSingleSchema(File xml, File xsd) {
        boolean legal = false;

        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(xsd);

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));

            legal = true;
        } catch (Exception e) {
            legal = false;
            e.printStackTrace();
        }

        return legal;
    }

    public static boolean validateWithMultiSchemas(InputStream xml, List<File> schemas) {
        boolean legal = false;

        try {
            Schema schema = createSchema(schemas);

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));

            legal = true;
        } catch (Exception e) {
            legal = false;
            e.printStackTrace();
        }

        return legal;
    }

    /**
     * Create Schema object from the schemas file.
     * @param schemas
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private static Schema createSchema(List<File> schemas) throws ParserConfigurationException, SAXException,
            IOException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        SchemaResourceResolver resourceResolver = new SchemaResourceResolver();
        sf.setResourceResolver(resourceResolver);

        Source[] sources = new Source[schemas.size()];

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setValidating(false);
        docFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        for (int i = 0; i < schemas.size(); i++) {
            org.w3c.dom.Document doc = docBuilder.parse(schemas.get(i));
            DOMSource stream = new DOMSource(doc, schemas.get(i).getAbsolutePath());
            sources[i] = stream;
        }

        return sf.newSchema(sources);
    }

    public static void main(String[] args) {
        try {
            InputStream xml = new FileInputStream(new File(XMLParser.class.getClassLoader().getResource("application.xml").getPath()));

            List<File> schemas = new ArrayList<File>();
            schemas.add(new File(XMLParser.class.getClassLoader().getResource(
                    "com/gupaoedu/vip/mvc/framework/context/protal.xsd").getPath()));
            schemas.add(new File(XMLParser.class.getClassLoader().getResource(
                    "com/gupaoedu/vip/mvc/framework/context/zs-protal-context.xsd").getPath()));

            boolean result = XMLParser.validateWithMultiSchemas(xml, schemas);
            System.out.println(result ? "XML文件通过XSD文件校验成功！" : "XML文件通过XSD文件校验失败！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
