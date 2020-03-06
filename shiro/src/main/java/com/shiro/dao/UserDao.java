package com.shiro.dao;

import com.shiro.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {
    @Select("select * from userinfo u where u.username = #{username}")
    public UserInfo getUserInfoByUsername(String username);
}
