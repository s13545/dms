package com.ssp.controller;

import com.github.pagehelper.PageInfo;
import com.ssp.entity.User;
import com.ssp.service.UserService;
import com.ssp.util.BCryptUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @PostMapping("/login")
    public String login(String username, String password, Model model, HttpSession session) {
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登陆数据
        SimpleHash md5Password = new SimpleHash("MD5", password, username, 3);
        System.out.println(md5Password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, md5Password.toString());
        try {
            subject.login(token);
            session.setAttribute("user",(User) subject.getPrincipal()); //拿到当前对象
            return "index";
        } catch (UnknownAccountException e) {
            //用户名错误
            model.addAttribute("loginMsg", "用户名不存在");
            return "login";
        } catch (IncorrectCredentialsException e) {
            //密码错误
            model.addAttribute("loginMsg", "密码错误");
            return "login";

        }
    }


    //员工修改自身信息
    @PostMapping("/update")
    public String update(@ModelAttribute User user,Model model){
        try{
            userService.updateById(user);
        }catch (Exception e){
            model.addAttribute("updateMsg","修改失败");
            return "user";
        }
        return "index";
    }

    //进入查看所有用户
    @GetMapping("showAll")
    public String toAllUser(Model model){
        List<User> users = userService.findAll();
//        System.out.println(users);
        model.addAttribute("users",users);
//        System.out.println("已查询所有用户");
        return "showAllUser";
    }


    //注销员工账号
    @GetMapping("/delete")
    public String deleteById(String uid){
            userService.deleteById(uid);
            return "redirect:/user/showAll";

    }

    //跳转至注册新员工页面
    @GetMapping("/toRegister")
    public String toRegister(){
        return "register";
    }

    //注册新员工
    @PostMapping("/register")
    public String register(@ModelAttribute User user,Model model){
        if (userService.register(user)){
            model.addAttribute("registerMsg","注册新账号成功！");
            return "redirect:/user/showAll";
        }else{
            model.addAttribute("registerMsg","新账号注册失败，用户名重复！");
            return "register";
        }
    }

    //跳转至管理员修改员工信息的页面
    @GetMapping("/toUpdateUser")
    public String toUpdate(String username, HttpSession session){
        User user = userService.findByUsername(username);
        session.setAttribute("updateUser",user);
        return "updateUser";
    }

    //管理员修改员工信息
    @PostMapping("/updateByAdmin")
    public String updateByAdmin(@ModelAttribute User user,HttpSession session){
        userService.updateById(user);
        session.removeAttribute("updateUser");
        return "redirect:/user/showAll";
    }






}
