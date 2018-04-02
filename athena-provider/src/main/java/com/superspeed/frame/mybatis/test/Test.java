package com.superspeed.frame.mybatis.test;

import com.superspeed.frame.mybatis.configuration.Configuration;
import com.superspeed.frame.mybatis.executor.SimpleExecutor;
import com.superspeed.frame.mybatis.session.DefaultSqlSession;
import com.superspeed.frame.mybatis.session.SqlSession;

public class Test {

    public static void main(String[] args) {
         SqlSession sqlSession = new DefaultSqlSession(new Configuration(), new SimpleExecutor());
         UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
         User user = userMapper.selectByPrimaryKey(Long.valueOf("1"));
         System.out.println(user.toString());
    }

}
