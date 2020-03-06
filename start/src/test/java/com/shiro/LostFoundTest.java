package com.shiro;

import com.shiro.service.LostFoundService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class LostFoundTest{
    @Autowired
    LostFoundService lostFoundService;

    @Test
    public void test1() throws  Exception{
        lostFoundService.deleteLostFounds("1");
    }
}
