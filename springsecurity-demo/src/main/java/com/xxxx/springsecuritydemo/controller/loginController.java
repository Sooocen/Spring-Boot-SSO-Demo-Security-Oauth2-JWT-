package com.xxxx.springsecuritydemo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class loginController {


    //@Secured("ROLE_abC")
    //PreAuthorize允许角色以ROLE_开头 也可以不以ROLE_开头 配置类不支持
    @PreAuthorize("hasRole('ROLE_abc')")
    @RequestMapping("/toMain")
    public String toMain(){
        return "redirect:main.html";
    }

    @RequestMapping("/toError")
    public String toError(){
        return "redirect:error.html";
    }

    @GetMapping("/demo")
    @ResponseBody
    public String demo(){
        return "demo";
    }
}
