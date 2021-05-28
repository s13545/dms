package com.ssp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {


    @RequestMapping("/toUser")
    public String toUser(){
        return "user";
    }





}
