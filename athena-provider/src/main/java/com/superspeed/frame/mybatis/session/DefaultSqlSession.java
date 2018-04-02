package com.superspeed.frame.mybatis.session;

import com.superspeed.frame.mybatis.configuration.Configuration;
import com.superspeed.frame.mybatis.executor.Executor;

/**
 * 默认回话
 * @ClassName: DefaultSqlSession
 * @Description: 默认回话，整个思想主线路，代码核心
 * @author xc.yanww
 * @date 2018-04-02 15:15
 */
public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T getMapper(Class clazz) {
        return configuration.getMapper(this, clazz);
    }

    @Override
    public <T> T selectOne(String statement, String parameter, Class clazz) {
        return executor.query(statement, parameter, clazz);
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }
}
