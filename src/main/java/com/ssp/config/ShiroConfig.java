package com.ssp.config;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //ShiroFilterFactoryBean 第三步
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro的内置过滤器
        /*
         * anon：无需认证就可以方法
         * authc：必须认证了才能访问
         * user：必须拥有 记住我 功能才能用
         * perms：拥有对某个资源的权限才能访问
         * role：拥有某个角色权限才能访问
         */
        //登陆拦截
        Map<String, String> filterMap = new LinkedHashMap<>();

        //授权，正常情况下应该跳转至未授权页面
        filterMap.put("/user/index","authc");
        filterMap.put("/user/update","authc");
        filterMap.put("/toUser","authc");
        filterMap.put("/user/toAllUser","authc");
        filterMap.put("/user/toUpdateByAdmin","authc");
        filterMap.put("/user/deleteByUser","authc");
        filterMap.put("/user/toRegister","authc");
        filterMap.put("/user/register","authc");
        filterMap.put("/file/*","authc");

        //filterMap.put("/user/add","perms[user:add]");


        bean.setFilterChainDefinitionMap(filterMap);

        //设置登陆请求
        bean.setLoginUrl("/user/toLogin");
        //设置未授权请求
        bean.setUnauthorizedUrl("/user/noAuth");
        return bean;
    }

    //DefaultWebSecurityManage 第二步
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联realm
        securityManager.setRealm(userRealm);

        return securityManager;
    }

    //创建realm对象 第一步
    @Bean(name = "userRealm")
    public UserRealm userRealm(){
        UserRealm userRealm = new UserRealm();
//        userRealm.setAuthenticationTokenClass(AuthenticationToken.class);
//        userRealm.setCredentialsMatcher(new CustomCredentialsMatcher());//自定义密码匹配
        return userRealm;
    }

}
