package com.shiro;

import com.shiro.pojo.School;
import com.shiro.pojo.UserInfo;
import com.shiro.pojo.re.ExceptionType;
import com.shiro.pojo.re.MyException;
import com.shiro.service.UserInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class UserInfoTest {
    @Autowired
    UserInfoService userInfoService;

    @Test
    public void test1() throws  Exception{
        Integer uid = 1;
        String username = "",
               realName = "",
               phone="",email="",sex="";
        School school = new School();
        Integer sid = Math.toIntExact(school.getId());
//        userInfoService.queryUserInfo(uid,username,realName,phone,email,sex,sid).forEach(u->System.out.println(u));
//        school.setId(1344);
        uid = 0;
//        sid = Math.toIntExact(school.getId());
//        userInfoService.queryUserInfo(uid,username,realName,phone,email,sex,sid).forEach(u->System.out.println(u));
        school.setId(1);
        sid = Math.toIntExact(school.getId());
        userInfoService.queryTotalPageAndRecordNumber(uid,username,realName,phone,email,sex,sid);
        System.out.println("总页数："+ userInfoService.getTotalPage());
        System.out.println("总条数："+ userInfoService.getRecordNumber());
        System.out.println("每页总数："+ userInfoService.getRows());
        userInfoService.queryUserInfo(uid,username,realName,phone,email,sex,sid).forEach(u->System.out.println(u));
        userInfoService.queryUserInfoList(uid,username,realName,phone,email,sex,sid,2).forEach(u->System.out.println(u));
    }

    @Test
    public void test2() throws  Exception{
        long uid = 31;
        String username = "小小的程序员23312";
        UserInfo userInfo1 = null;
        try {
            userInfo1 = userInfoService.findByUsername(username);//为空会直接抛出
            if (userInfo1.getUid() != uid)//userinfo1不为空若uid等于用户的uid，说明此用户名未修改
                throw new MyException(ExceptionType.update);
        } catch (NullPointerException e) {
            System.out.println("此用户名可以使用");
        }catch (MyException e){
            throw new Exception(e.getMessage());
        }
    }

    @Test
    public void test3() throws  Exception{
        userInfoService.deleteUserInfos("1,2");
    }
}
