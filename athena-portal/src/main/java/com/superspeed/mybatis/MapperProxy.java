package com.superspeed.mybatis;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class MapperProxy implements InvocationHandler {

    private SqlSession sqlSession;

    public MapperProxy(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List userList;
        if (args == null) {
            userList = sqlSession.selectList(method.getName());
        } else {
            if (method.getParameterTypes().length > 1) {
                userList = sqlSession.selectList(method.getName(), Arrays.asList(args));
            } else {
                userList = sqlSession.selectList(method.getName(), args[0]);
            }
        }

        if (method.getReturnType().equals(List.class)) {
            return userList;
        } else {
            return userList.get(0);
        }
    }
}
