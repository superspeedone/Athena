package com.superspeed.frame.mybatis.executor.resultset;

import com.superspeed.frame.mybatis.javassist.Underline2Camel;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 结果集处理
 * @ClassName: DefaultResultSetHandler
 * @Description: 结果集处理，将查询结果集一一映射到实体对象
 * @author xc.yanww
 * @date 2018-04-02 15:34
 */
public class DefaultResultSetHandler implements ResultSetHandler {

    @Override
    public <E> E handleResultSets(ResultSet resultSet, Class returnType) throws SQLException {
        Object obj = null;

        try {
            obj = returnType.newInstance();
            Field[] fields = returnType.getDeclaredFields();

            while (resultSet.next()) {
                for (Field field : fields) {
                    setValue(resultSet, returnType, field, obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (E) obj;
    }

    private void setValue(ResultSet resultSet, Class returnType, Field field, Object obj) throws Exception {
        String columnName = Underline2Camel.camel2Underline(field.getName());
        Method method = returnType.getMethod(Underline2Camel.underline2Camel("set_" + columnName), field.getType());
        Object value = null;

        if (field.getType() == String.class) {
            value = resultSet.getString(columnName);
        } else if (field.getType() == Long.class) {
            value = resultSet.getLong(columnName);
        } else if (field.getType() == Integer.class) {
            value = resultSet.getInt(columnName);
        }
        method.invoke(obj, value);
    }

}
