package com.shiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 如果数据层运行出错，拦截请求会推送给设置好的403，
 * 数据池出错大多时候并不会直接体现出来
 *
 * 寻物对应表扬，你要招的东西别人还你了  lostFound------claim
 * 失物对应认领，丢失的东西取认领回来    lookingFor-----praise
 */
@SpringBootApplication(scanBasePackages = "com.shiro")
@MapperScan(value = {"com.shiro.mapper","com.shiro.dao"})
@EnableScheduling // 打开任务调度
@EnableSwagger2 // 开启swagger
public class ShiroApplication {
    /**
     * run 方法内部
     * 1. 加载环境变量
     * 2. 创建上下文 ConfigurableApplicationContext 对象
     * 3. 准备上下文 prepareContext()
     * 4. 刷新上下文 refreshContext()
     * 5. 刷新之后的处理 afterRefresh()
     *
     * 通过断点调试，发现执行完refreshContext()方法执行完毕之后，
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(SpringApplication.run(ShiroApplication.class));
    }
}
