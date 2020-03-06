package com.shiro;

import com.shiro.pojo.LookingFor;
import com.shiro.pojo.Praise;
import com.shiro.service.PraiseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class PraiseTest {
    @Autowired
    PraiseService praiseService;

    @Test
    public void test1() throws  Exception{
        for (int i = 1;i<=3;i++) {
            Praise praise = new Praise();
            LookingFor lookingFor = new LookingFor();
            lookingFor.setLookingForId(i);
            praise.setLookingFor(lookingFor);
            praise.setContents("非常感谢！非常感谢！非常感谢！非常感谢！非常感谢！非常感谢！非常感谢！非常感谢！");
            praise.setTitle("棒球帽"+i);
            praiseService.addPraise(praise);
        }
    }

    @Test
    public void test2() throws  Exception{
        praiseService.deletePraises("2,3,4");
    }

    @Test
    public void test3() throws  Exception{
        LookingFor lookingFor = null;
        praiseService.queryTotalPageAndRecordNumber(lookingFor,"","", 1,null);
//        praiseService.queryPraise(lookingFor,"","", 1,null).forEach(System.out::println);
//        lookingFor = new LookingFor();
//        lookingFor.setLookingForId(1);
//        System.out.println(praiseService.getTotalPage());
//        System.out.println(praiseService.getRecordNumber());
//        praiseService.queryPraise(lookingFor,"", null,1).forEach(System.out::println);
//        praiseService.queryPraise(null,"棒球帽", null,1).forEach(System.out::println);
//        praiseService.queryPraise(null,"", new Date(119,10,24),1).forEach(System.out::println);
    }

    @Test
    public void test4() throws  Exception{
        Praise praise = new Praise();
        praise.setPraiseId(1);
        LookingFor lookingFor = new LookingFor();
        lookingFor.setLookingForId(2);
        praise.setLookingFor(lookingFor);
        praise.setContents("非常感谢");
        praise.setTitle("棒球帽");
        praiseService.updatePraise(praise);
    }

}
