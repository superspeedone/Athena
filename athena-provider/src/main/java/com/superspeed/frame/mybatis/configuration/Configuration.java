package com.superspeed.frame.mybatis.configuration;

import com.superspeed.frame.mybatis.binding.MapperProxy;
import com.superspeed.frame.mybatis.binding.MapperRegistry;
import com.superspeed.frame.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

public class Configuration {

    private MapperRegistry mapperRegistry = new MapperRegistry();

    public <T> T getMapper(SqlSession sqlSession, Class clazz) {
        return (T) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[] {clazz},
                new MapperProxy(sqlSession));
    }

    public MapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }
}




