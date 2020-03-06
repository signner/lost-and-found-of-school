package com.shiro.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CheckUsersMapper {
    @Select("SELECT COUNT(*) logon FROM userInfo WHERE username = #{0}")
    int checkUsername(String username);

    @Select("SELECT COUNT(*) logon FROM userInfo WHERE phone = #{0}")
    int checkPhone(String phone);

    @Select("SELECT COUNT(*) logon FROM userInfo WHERE email = #{0}")
    int checkEmail(String email);
}