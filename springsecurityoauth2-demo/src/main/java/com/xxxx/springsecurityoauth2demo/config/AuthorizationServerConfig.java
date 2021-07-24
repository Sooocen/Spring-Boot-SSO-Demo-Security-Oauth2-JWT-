package com.xxxx.springsecurityoauth2demo.config;

import com.xxxx.springsecurityoauth2demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configuration.ClientDetailsServiceConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer //开启授权服务器
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

//    整合JWT时 删除Redis及相关配置
//    @Autowired
//    @Qualifier("redisTokenStore")
//    private TokenStore tokenStore;


//整合JWT配置
    @Autowired
    @Qualifier("jwtTokenStore")
    private TokenStore tokenStore;

    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private JwtTokenEnhancer jwtTokenEnhancer;







    //授权码模式配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception{
        clients.inMemory()
                //客户端ID
                .withClient("client")
                //密钥
                .secret(passwordEncoder.encode("112233"))
                //重定向地址
                .redirectUris("http://localhost:8081/login")
                //授权范围
                .scopes("all")
                //设置accessToken(访问令牌)失效时间 单位秒(s)
                .accessTokenValiditySeconds(60)
                //refreshToken(刷新令牌)失效时间 单位秒(s)
                .refreshTokenValiditySeconds(86400)
                //自动授权
                .autoApprove(true)
                /**
                 * 授权类型
                 * authorization_code:授权码模式
                 * passwordd:密码模式
                 * refresh_token:刷新令牌
                 */
                //
                .authorizedGrantTypes("authorization_code","password","refresh_token");
    }

    //密码模式配置
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception{

        //添加JWT增强内容 enhance : enhancer info
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(jwtAccessTokenConverter);
        tokenEnhancerChain.setTokenEnhancers(delegates);

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService)
                //整合JWT时 删除Redis及相关配置
                //.tokenStore(tokenStore);

                //整合JWT配置
                //[jwtAccessTokenConverter] AccessToken转成Token
                .tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                //添加自定义JWT增强内容时添加
                .tokenEnhancer(tokenEnhancerChain);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception{
        //必须要身份认证  配置单点登录 必须!
        security.tokenKeyAccess("isAuthenticated()");
    }

}
