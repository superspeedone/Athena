package com.superspeed.mybatis;

import com.superspeed.mybatis.mapper.UserMapper;
import com.superspeed.mybatis.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.util.List;

public class Bootstrap {

    public static void main(String[] args) {
        try {
            String resource = "com/superspeed/mybatis/mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession sqlSession = sqlSessionFactory.openSession();
            List<User> userList = sqlSession.selectList("listByUser");
            System.out.println(userList);

            /*// mybatis 绕过UserMapper接口实现类实例，直接用接口调用方法
            UserMapper userMapper = (UserMapper) Proxy.newProxyInstance(UserMapper.class.getClassLoader(),
                    new Class[]{UserMapper.class}, new MapperProxy(sqlSession));
            User user = userMapper.getById(new Long("1"));
            System.out.println(user);*/

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
