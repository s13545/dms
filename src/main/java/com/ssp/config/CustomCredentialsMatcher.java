package com.ssp.config;

import com.ssp.util.BCryptUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

//用于匹配密码
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //获得前台传过来的密码
        String originalPassword=(String)token.getCredentials();
        //这是数据库里查出来的密码
        String sqlOriginalPassword=(String)info.getCredentials();
        //进行比对，BCryptUtil工具类辅助匹配
        return BCryptUtil.match(originalPassword,sqlOriginalPassword);
    }

}
