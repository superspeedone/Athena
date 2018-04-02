package com.superspeed.frame.mybatis.executor.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler {

    <E> E handleResultSets(ResultSet resultSet, Class returnType) throws SQLException;

}
