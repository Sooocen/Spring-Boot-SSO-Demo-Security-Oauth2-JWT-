package com.xxxx.springsecuritydemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.查看用户名是否为空
        //if (username == null || "".equals(username)) {
        //    throw new RuntimeException("用户不能为空");
        //}

        //2.去数据库查询usernae是否存在 不存在抛异常
        // User user = userservice.getUserByName(username);
        // if (user == null) {
        //    throw new RuntimeException("用户不存在");
        // }
        if(!"admin".equals(username)){
            throw new UsernameNotFoundException("用户名不存在");
        }

        //3.比较库里密码和表单密码
        String password = passwordEncoder.encode("123");
        return new User(username,password,AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normal,ROLE_abc,/main.html"));
    }

}
