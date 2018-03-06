package com.superspeed.common.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Prop {

    private static Logger logger = LoggerFactory.getLogger(PropKit.class);

    /** 默认读取编码 */
    private static final String DEFAULT_ENCODING = "UTF-8";

    private Properties properties = null;

    /**
     * 加载properties文件
     * @param fileName 文件名
     * @author xc.yanww
     * @date 2017/12/11 16:21
     */
    public Prop(String fileName) {
        this(fileName, DEFAULT_ENCODING);
    }

    /**
     * 加载properties文件
     * @param fileName 文件名
     * @param encoding 编码
     * @author xc.yanww
     * @date 2017/12/11 16:21
     */
    public Prop(String fileName, String encoding) {
        InputStream inputStream = null;
        try {
            inputStream = getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
            }
            properties = new Properties();
            properties.load(new InputStreamReader(inputStream, encoding));
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Error closing inputStream when loading properties file.", e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 获取应用程序上下文类加载器
     * @return {@link ClassLoader}
     * @author xc.yanww
     * @date 2017/12/11 16:22
     */
    private ClassLoader getClassLoader() {
        ClassLoader ret = Thread.currentThread().getContextClassLoader();
        return ret != null ? ret : getClass().getClassLoader();
    }

    public Properties getProperties() {
        return this.properties;
    }

    public String getString(String key) {
        return this.properties.getProperty(key);
    }
}
