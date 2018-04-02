package com.superspeed.frame.mybatis.session;

import com.superspeed.frame.mybatis.configuration.Configuration;

public interface SqlSession {

    public <T> T getMapper(Class clazz);

    public <T> T selectOne(String statement, String parameter, Class returnType);

    public Configuration getConfiguration();

}
