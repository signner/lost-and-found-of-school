package com.shiro.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 记录所有的接口的访问
 */
@Aspect
@Component
@Slf4j
public class WebLog {

    @Pointcut("execution (* com.shiro.web..*Controller.*(..))")
    public void pointCut1() {
    }

    @Before("pointCut1()")
    public static void beforeMethod(JoinPoint joinPoint) throws Exception {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 记录下请求内容
            System.out.println("\r\n");
            WebLog.log.info("地址 : " + request.getRequestURL().toString());
            WebLog.log.info("请求方式 : " + request.getMethod());
            WebLog.log.info("IP : " + request.getRemoteAddr());
            WebLog.log.info("执行的方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            Object[] args = joinPoint.getArgs().clone();
            WebLog.log.info("参数 : " + Arrays.toString(args));
        }
    }

    @AfterReturning(returning = "rvt", pointcut = "pointCut1()")
    public static void afterReturing(Object rvt) {
        WebLog.log.info("返回内容 : " + rvt);
//        logger.info("花费时间 : " + (System.currentTimeMillis() - startTime.get()) + "毫秒");
    }

}
