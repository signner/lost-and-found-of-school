package com.shiro;


import com.shiro.mapper.LookingForMapper;
import com.shiro.pojo.UserInfo;
import com.shiro.service.LookingForService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class LookingForMapperTest {
    @Autowired
    LookingForService lookingForService;
    @Autowired
    LookingForMapper lookingForMapper;

    @Test
    public void test1() throws Exception {
        System.out.println(lookingForService.getLookingFor(1));
    }

    @Test
    public void test2() throws Exception {
        lookingForService.queryAllLookingFor().forEach(l -> System.out.println(l));
    }

    @Test
    public void test3() throws Exception {
//        String school,String title, String goodsName,
//          String lostTime, String lostPlace, String telephone, UserInfo userObj, int currentPage
        String school = "";
        String title = "";
        String goodsName="";
        Date lostTime = null;
        String lostPlace = "";
        String telephone = "";
        UserInfo userObj = new UserInfo();
        userObj.setUid(1);
        int currentPage = 0;
        if (currentPage==0) currentPage=1;
        int cut = 0,noCut=0;
//        System.out.println("分页1--------------------------------------------------------------------------");
//        cut = lookingForService.queryLookingFor(school,title,goodsName,lostTime,lostPlace,telephone,userObj).size();
//        System.out.println("不分页1-------------------------------------------------------------------------");
//        noCut= lookingForService.queryLookingFor(school,title,goodsName,lostTime,lostPlace,telephone,userObj,currentPage).size();
//        System.out.println(cut+"\t"+noCut);
//        System.out.println("分页2--------------------------------------------------------------------------");
//        school="北京大学";
//        cut = lookingForService.queryLookingFor(school,title,goodsName,lostTime,lostPlace,telephone,userObj).size();
//        System.out.println("不分页2-------------------------------------------------------------------------");
//        noCut= lookingForService.queryLookingFor(school,title,goodsName,lostTime,lostPlace,telephone,userObj,currentPage).size();
//        System.out.println(cut+"\t"+noCut);
//        System.out.println("分页3--------------------------------------------------------------------------");
//        school="";title="1";
//        cut = lookingForService.queryLookingFor(school,title,goodsName,lostTime,lostPlace,telephone,userObj).size();
//        System.out.println("不分页3-------------------------------------------------------------------------");
//        noCut= lookingForService.queryLookingFor(school,title,goodsName,lostTime,lostPlace,telephone,userObj,currentPage).size();
//        System.out.println(cut+"\t"+noCut);
//        System.out.println("分页4--------------------------------------------------------------------------");
//        title="";goodsName="1";
//        cut = lookingForService.queryLookingFor(school,title,goodsName,lostTime,lostPlace,telephone,userObj).size();
//        System.out.println("不分页4-------------------------------------------------------------------------");
//        noCut= lookingForService.queryLookingFor(school,title,goodsName,lostTime,lostPlace,telephone,userObj,currentPage).size();
//        System.out.println(cut+"\t"+noCut);
//        System.out.println("分页4--------------------------------------------------------------------------");
//        goodsName="";lostTime = new Date(2019,11,17);
//        cut = lookingForService.queryLookingFor(school,title,goodsName,lostTime,lostPlace,telephone, Math.toIntExact(userObj.getUid())).size();
//        System.out.println("不分页4-------------------------------------------------------------------------");
//        noCut= lookingForService.queryLookingFor(school,title,goodsName,lostTime,lostPlace,telephone,Math.toIntExact(userObj.getUid()),currentPage).size();
//        System.out.println(cut+"\t"+noCut);
    }

    @Test
    public void test4() throws  Exception{
        lookingForService.deleteLookingFor("1");
    }

    @Test
    public void test5() throws  Exception{
        lookingForMapper.overTwoMonth().forEach(System.out::println);
    }

    @Test
    public void test6() throws  Exception{
        System.out.println(lookingForMapper.test());
    }
}
