package com.shiro.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {
    
    private long uid;   // uid,user主键

    private String avatar ; // 头像
    
    private String username; // 登录名
    
    private String password; // 密码
    
    private String realName; // 真实姓名
    
    private String phone; // 电话
    
    private String email; // 邮箱
    
    private String sex; // 性别
    
    private String role; // 角色
    
    private School school;//   sid 外键对应school
    
    private Date birth; // 出生日期

    private String token=""; // token
}
