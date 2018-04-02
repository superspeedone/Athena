package com.superspeed.frame.mybatis.executor;

import com.superspeed.frame.mybatis.executor.resultset.DefaultResultSetHandler;
import com.superspeed.frame.mybatis.executor.resultset.ResultSetHandler;
import java.sql.*;

/**
 * Sql语句执行器
 * @ClassName: SimpleExecutor
 * @Description: 执行Sql语句
 * @author xc.yanww
 * @date 2018-04-02 15:31
 */
public class SimpleExecutor implements Executor {

    private ResultSetHandler resultSetHandler = new DefaultResultSetHandler();

    @Override
    public <T> T query(String statement, String parameter, Class returnType) {
        Connection connection = null;
        PreparedStatement preparedStatement;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pussinboots_morning?useUnicode=true&characterEncoding=utf-8&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "root");
            preparedStatement = connection.prepareStatement(String.format(statement, Integer.valueOf(parameter)));
            ResultSet rs = preparedStatement.executeQuery();
            return resultSetHandler.handleResultSets(rs, returnType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
