package com.superspeed.frame.mybatis.annotations;

import org.apache.ibatis.mapping.StatementType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Select {
    String[] value();
}
