package com.ssp.config;

import com.ssp.entity.User;
import com.ssp.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

//自定义的一个realm 就需要继承AuthorizingRealm
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权doGetAuthorizationInfo");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //info.addStringPermission("user:add");
        //拿到当前登陆的对象
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal(); //拿到user对象
        info.addStringPermission(currentUser.getPerm());
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //System.out.println("执行了认证doGetAuthenticationInfo");

        UsernamePasswordToken userToken = (UsernamePasswordToken)authenticationToken;

        //用户名信息
        User loginUser = userService.findByUsername(userToken.getUsername());

        //用户名判断
        if (loginUser == null){
            return null; //抛出异常
        }

        //密码判断，由shiro完成
        return new SimpleAuthenticationInfo(loginUser,loginUser.getPassword(),"userRealm");
    }
}
