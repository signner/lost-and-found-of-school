package com.shiro;

import com.shiro.mapper.SchoolMapper;
import com.shiro.pojo.Images;
import com.sun.prism.Image;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class SchoolTest {
    @Autowired
    SchoolMapper schoolMapper;

    @Test
    public void addImageTest()throws Exception{
        test("北京大学");
        test("清华大学");
        test("景德镇陶瓷大学");
    }

    public void test(String school){
        for (int i=1;i<=3;i++){
            Images images = new Images();
            images.setSchool(school);
            images.setImg("localhost/temp/images/"+school+"/"+i+".jpg");
            schoolMapper.addImages(images);
        }
    }
}
