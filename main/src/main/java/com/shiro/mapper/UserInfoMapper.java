package com.shiro.mapper;

import com.shiro.pojo.UserInfo;
import com.shiro.pojo.re.QueryOfElementUi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserInfoMapper {
    /*通过主键uid查询一个userinfo对象*/
    public UserInfo getUserInfo(Integer uid);
    /*通过username查找一个对象*/
    public UserInfo getUserInfoByUsername(String username);
    /*添加一个用户*/
    public void addUserInfo(UserInfo userInfo);
    /*更新信息*/
    public void updateUserInfo(UserInfo user);
    /*删除用户*/
    public void deleteUserInfo(String username);
    /*分页查询*/
    public ArrayList<UserInfo> queryUserInfoList(@Param("where") String where,
                                                 @Param("startIndex") Integer startIndex,
                                                 @Param("pageSize") int pageSize);

    public ArrayList<UserInfo> queryUserInfo(@Param("where") String where);

    public int queryUserInfoForCount(@Param("where") String where);

    @Select("select token from userInfo where uid = #{uid}")
    public String getToken(@Param("uid") Integer uid);
    @Update("update userInfo set token=#{token} where uid=#{uid}")
    public void setToken(@Param("token") String token,@Param("uid") Integer uid);

    @Select("select username value from userInfo where sid in (select sid from school where name = #{school}) and username like #{username} limit 0,8")
    List<QueryOfElementUi> searchOfLike(@Param("school")String school,@Param("username") String username);
}
