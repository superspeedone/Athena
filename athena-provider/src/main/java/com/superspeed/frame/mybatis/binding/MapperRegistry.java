package com.superspeed.frame.mybatis.binding;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper注册
 * @ClassName: MapperRegistry
 * @Description: Mapper注册，保存mapper命名空间，方法对应的sql语句
 * @author xc.yanww
 * @date 2018-04-02 15:14
 */
public class MapperRegistry {

    public String userMapperNamespace = "com.superspeed.frame.mybatis.test.UserMapper";

    public Map<String, String> methodMapping = new HashMap<String, String>();

    public MapperRegistry() {
        this.methodMapping.put("selectByPrimaryKey", "select * from cms_user where user_id = %d");
    }

}
