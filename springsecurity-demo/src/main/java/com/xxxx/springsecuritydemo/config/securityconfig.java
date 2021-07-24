package com.xxxx.springsecuritydemo.config;

import com.xxxx.springsecuritydemo.handler.MyAccessDeniedHandler;
import com.xxxx.springsecuritydemo.handler.MyAuthenticationFailureHandler;
import com.xxxx.springsecuritydemo.handler.MyAuthenticationSuccessHandler;
import com.xxxx.springsecuritydemo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class securityconfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MyAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;



    @Override
    protected void configure (HttpSecurity http) throws Exception{
        //跨站请求伪造
        http.csrf().disable();

        //表单提交
        http.formLogin()
                //自定义入参
                //.usernameParameter("username1")
                //.passwordParameter("password1")
                //自定义登录页面
                .loginPage("/login.html")
                //自定义登录方法
                .loginProcessingUrl("/login")
                //登陆成功跳转URL 仅接受POST
                .successForwardUrl("/toMain")
                //自定义登录成功逻辑
                //.successHandler(new MyAuthenticationSuccessHandler("/main.html"))
                //登陆失败跳转URL 仅接受POST
                .failureForwardUrl("/toError");
                //自定义登录失败逻辑
                //.failureHandler(new MyAuthenticationFailureHandler("/error.html"));

        http.logout()
                //.logoutUrl("/user/logout")
                //退出登录成功后跳转的页面
                .logoutSuccessUrl("/login.html");
        //授权
        http.authorizeRequests()
                //放行/error.html 不需要认证
                .antMatchers("/error.html").permitAll()
                //放行/login.html 不需要认证
                //.antMatchers("/login").permitAll()
                .antMatchers("/login.html").permitAll()
                //放行静态资源 ant表达式
                .antMatchers("/css/**","/js/**","/images/**").permitAll()//放行特定路径
                //.antMatchers("/**/*.png").permitAll() //放行所有目录下 所有png格式的文件
                //放行静态资源 正则表达式
                //.regexMatchers(".+[.]png").permitAll()
                //.regexMatchers(HttpMethod.POST,"/demo").permitAll()
                //放行路径 Mvc匹配
                //.mvcMatchers("/demo").servletPath("/xxxx").permitAll()


                //权限控制 严格区分大小写
                //基于权限
                //.antMatchers("/main1.html").hasAuthority("Admin")
                //.antMatchers("/main1.html").hasAnyAuthority("Admin","admin")
                //基于角色
                //.antMatchers("/main1.html").hasRole("Abc")
                //.antMatchers("/main1.html").hasAnyRole("abc","Abc")
                //基于IP地址
                //.antMatchers("/main1.html").hasIpAddress("127.0.0.1")


                //拦截所有请求 必须认证
                .anyRequest().authenticated()
                //自定义access
                //.anyRequest().access("@myServiceImpl.hasPermission(request,authentication)")

                //异常处理
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);

                //打开 [记住我] 功能
                http.rememberMe()
                        //设置数据源
                        .tokenRepository(persistentTokenRepository)
                        //参数
                        //.rememberMeParameter()
                        //超时时间
                        .tokenValiditySeconds(60)
                        //自定义登录逻辑
                        .userDetailsService(userDetailsService);





    }

    @Bean
    public PasswordEncoder getPW(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        jdbcTokenRepository.setDataSource(dataSource);
        //自动建表 第一次开启 之后关闭
        //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }
}
