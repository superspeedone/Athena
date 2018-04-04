package com.superspeed.frame.mybatis.test;

import com.superspeed.frame.mybatis.annotations.Select;

public interface UserMapper {

    @Select("select * from cms_user where user_id = %d")
    User selectByPrimaryKey(Long userId);


}
