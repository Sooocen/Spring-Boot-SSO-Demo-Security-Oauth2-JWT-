package com.xxxx.springsecurityoauth2demo.controller;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getCurrentUser")
    public Object getCurrentUser(Authentication authentication, HttpServletRequest request){
        String header = request.getHeader("Authorization");
        //header里格式为 [bearer ]共7位
        String token = header.substring(header.lastIndexOf("bearer")+7);
        return Jwts.parser()
                //中文密钥 -> 强转为UTF-8  <防止乱码>
                .setSigningKey("test_key".getBytes(StandardCharsets.UTF_8))
                //.parse(token)
                .parseClaimsJws(token)
                .getBody();
    }
}
