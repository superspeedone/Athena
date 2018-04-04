package com.superspeed.frame.mybatis.binding;

import com.superspeed.frame.mybatis.annotations.Select;
import com.superspeed.frame.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Mapper接口代理类
 * @ClassName: MapperProxy
 * @Description: 用于创建Mapper接口代理对象，
 *      持有SqlSession对象引用，调用SqlSession#selectOne
 *      方法执行sql语句，返回封装后的结果集（结果集一一映射到实体对象）
 * @author xc.yanww
 * @date 2018-04-02 14:38
 */
public class MapperProxy implements InvocationHandler {

    private SqlSession sqlSession;

    public MapperProxy(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Select selectAnnotation = method.getAnnotation(Select.class);
        if (selectAnnotation != null) {
            System.out.println("selectAnnotation:" + Arrays.toString(selectAnnotation.value()));
        }
        MapperRegistry mapperRegistry = sqlSession.getConfiguration().getMapperRegistry();
        if (mapperRegistry.userMapperNamespace.equals(method.getDeclaringClass().getName())) {
            return sqlSession.selectOne(mapperRegistry.methodMapping.get(method.getName()) ,
                    String.valueOf(args[0]), method.getReturnType());
        }
        return method.invoke(this, args);
    }
}
