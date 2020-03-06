package com.shiro.config;

import com.shiro.filter.AnyRolesAuthorizationFilter;
import com.shiro.realm.MyRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 1.通过工厂配置shirofilter
 * 1）.拦截器实质就是一个map<String,String>
 * 2.配置密码加密方式
 * 3.配置realm
 * 1）.设置加密方式
 * 4.把realm注册进shiro的安全管理
 * *5.开启shiro的注解支持
 * *6.配置shiro的异常解析
 */
@Component
@Configuration
public class ShiroConfig {

    //    shiro的Session接口提供了一个touch方法，负责session的刷新；session的代理对象最终会调用SimpleSession的touch()：
//
//    public void touch() {
//        this.lastAccessTime = new Date();        // 更新最后被访问时间为当前时间
//    }
//　　但是touch方法是什么时候被调用的呢？JavaSE需要我们自己定期的调用session的touch()
//   去更新最后访问时间；如果是Web应用，每次进入ShiroFilter都会自动调用session.touch()来更新最后访问时间
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
//        System.out.println("------------->shiroFilter");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //注册安全管理
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //配置自定义的权限策略，hasRoles，有一者即可访问
        Map<String, Filter> filters = new HashMap<>();
        filters.put("hasRoles", new AnyRolesAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filters);
        //配置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();


        filterChainDefinitionMap.put("/pobstyle.css", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/statics/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/captcha.jpg", "anon");

        //swagger请求不拦截
        filterChainDefinitionMap.put("/swagger/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");


        //配置记住我或认证通过可以访问的地址
        //配置游客也能访问的接口请求,anon是不拦截
        filterChainDefinitionMap.put("/api-docs", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/logout", "anon");
        filterChainDefinitionMap.put("/zhuce", "anon"); // 带图片注册
        filterChainDefinitionMap.put("/zhuce1", "anon");// 不带图片，默认头像注册
        //common/**，允许所有的人访问，公开
        filterChainDefinitionMap.put("/none/**", "anon");
        //user/**,需要user以上角色才能访问
        filterChainDefinitionMap.put("/user/**", "hasRoles[user,admin,guest]");
        //admin/**,需要admin以上的角色才能访问
        filterChainDefinitionMap.put("/admin/**", "hasRoles[admin,guest]");
        //guest/**,需要guest角色才能访问
        filterChainDefinitionMap.put("/guest/**", "hasRoles[guest]");

        filterChainDefinitionMap.put("/**", "authc");
        //无权限跳转到403而不是登录页面
        shiroFilterFactoryBean.setLoginUrl("/403");
        //配置权限不足的接口
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

//        System.out.println(filterChainDefinitionMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }


    @Bean
    public SecurityManager securityManager(SessionManager sessionManager,
                                           EhCacheManager ehCacheManager,
                                           MyRealm myRealm) {
//        System.out.println("------------->1、securityManager DefaultWebSecurityManager");
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //配置realm
        securityManager.setRealm(myRealm);
        //这个一定需要配，他会从前端获取到sessionid控制当前用户的角色和权限，从而实现权限控制
        securityManager.setSessionManager(sessionManager);
        //自定义缓存
        securityManager.setCacheManager(ehCacheManager);
        return securityManager;
    }

    @Bean
    public MyRealm myRealm(HashedCredentialsMatcher hashedCredentialsMatcher) {
//        System.out.println("------------->2、myRealm 设置realm");
        MyRealm myRealm = new MyRealm();
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return myRealm;
    }

    /**
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了 ）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
//        System.out.println("------------->realm加密");
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5"); // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2); // 散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    @Bean
    public SessionManager sessionManager() {
//        System.out.println("------------->3、sessionManager 自定义");
        SessionManager shiroSessionManager = new SessionManager();
        shiroSessionManager.setGlobalSessionTimeout(1000*60*24*10);
        //关闭url后面的JSESSIONID
        shiroSessionManager.setSessionIdUrlRewritingEnabled(false);
        shiroSessionManager.setDeleteInvalidSessions(true);
        return shiroSessionManager;
    }

    /**
     * 避免过多的重复操作（验证权限）
     *
     * @return
     */
    @Bean
    public EhCacheManager ehCacheManager() {
//        System.out.println("------------->4、EhCacheManager()");
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:shiro-ehcache.xml");
        return ehCacheManager;
    }


    @Bean(name = "simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        mappings.setProperty("java.lang.Exception", "/403");
        resolver.setExceptionMappings(mappings);
        return resolver;
    }
}
