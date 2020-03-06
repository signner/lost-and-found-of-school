package com.shiro.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("admin/test")
    public String test(String test){
        return test;
    }
    @RequestMapping("user/test")
    public String test1(String test){
        return test;
    }
    @RequestMapping("none/test")
    public String test2(String test){
        return test;
    }
}
