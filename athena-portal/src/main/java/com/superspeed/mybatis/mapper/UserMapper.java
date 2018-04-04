package com.superspeed.mybatis.mapper;

import com.superspeed.mybatis.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
	
	/**
	 * 根据管理员ID查找管理员信息
	 * @param userId 管理员ID
	 * @return
	 */
	@Select("select * from cms_user u where user_id = #{userid}")
	User getById(@Param("userId") Long userId);
	
	/**
	 * 根据管理员信息查找管理员列表
	 * @return
	 */
	List<User> listByUser();

}