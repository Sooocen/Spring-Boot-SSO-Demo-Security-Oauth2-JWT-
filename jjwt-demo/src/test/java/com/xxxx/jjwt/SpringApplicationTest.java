package com.xxxx.jjwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import java.util.Date;

@SpringBootTest
public class SpringApplicationTest {
    /**
     * JWT生成
     */
    @Test
    public void testJwt(){
        JwtBuilder jwtBuilder = Jwts.builder()
                //唯一ID { "id":"888"}
                .setId("888")
                //接受的用户 {"sub":"Rose"}
                .setSubject("Rose")
                //签发时间 {"iat":"date"}
                .setIssuedAt(new Date())
                //签名算法 及 密钥
                .signWith(SignatureAlgorithm.HS256,"xxxx");
        //签发Token
        String token = jwtBuilder.compact();
        System.out.println(token);
        System.out.println("------------------------------------------");
        String[] split = token.split("\\.");
        System.out.println(Base64Codec.BASE64.decodeToString(split[0]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[1]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[2]));
    }
    /**
     * 解析JWT
     */
    @Test
    public void testParseToken(){
        String token ="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiJSb3NlIiwiaWF0IjoxNjI3MDA1Mjg3fQ.tE_h0WDxnTBz7s8mmu8SEF-Gn81m2hTiCoom0icYass";
        //解析Token 获取对象 jwt荷载神明的对象
        Claims claims = (Claims)Jwts.parser()
                //必须填写正确的签发密钥
                .setSigningKey("xxxx")
                .parse(token)
                .getBody();
        System.out.println("ID="+claims.getId());
        System.out.println("SUB="+claims.getSubject());
        System.out.println("IAT="+claims.getIssuedAt());
    }






    /**
     * JWT生成(含过期时间)
     */
    @Test
    public void testJwtHasExpire(){
        //失效时间 当前时间 + 60s
        Long expDate = System.currentTimeMillis() + 60 *1000;
        JwtBuilder jwtBuilder = Jwts.builder()
                //唯一ID { "id":"888"}
                .setId("888")
                //接受的用户 {"sub":"Rose"}
                .setSubject("Rose")
                //签发时间 {"iat":"date"}
                .setIssuedAt(new Date())
                //签名算法 及 密钥
                .signWith(SignatureAlgorithm.HS256,"xxxx")
                //设置过期时间
                .setExpiration(new Date(expDate));
        //签发Token
        String token = jwtBuilder.compact();
        System.out.println(token);
        System.out.println("------------------------------------------");
        String[] split = token.split("\\.");
        System.out.println(Base64Codec.BASE64.decodeToString(split[0]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[1]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[2]));
    }
    /**
     * 解析JWT(含过期时间)
     */
    @Test
    public void testParseTokenHasExpire(){
        String token ="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiJSb3NlIiwiaWF0IjoxNjI3MDA2MjY4LCJleHAiOjE2MjcwMDYzMjh9.jDmkYS1oSUiLW27VbVV72rVQ8LhosNr68O7RdzTVGh4";
        //解析Token 获取对象 jwt荷载神明的对象
        Claims claims = (Claims)Jwts.parser()
                //必须填写正确的签发密钥
                .setSigningKey("xxxx")
                .parse(token)
                .getBody();
        System.out.println("ID="+claims.getId());
        System.out.println("SUB="+claims.getSubject());
        System.out.println("EXP="+claims.getExpiration());
        System.out.println("IAT="+claims.getIssuedAt());
    }





    /**
     * JWT生成(含自定义声明)
     */
    @Test
    public void testJwtHasEnhancer(){
        //失效时间 当前时间 + 60s
        Long expDate = System.currentTimeMillis() + 60 *1000;
        JwtBuilder jwtBuilder = Jwts.builder()
                //唯一ID { "id":"888"}
                .setId("888")
                //接受的用户 {"sub":"Rose"}
                .setSubject("Rose")
                //签发时间 {"iat":"date"}
                .setIssuedAt(new Date())
                //签名算法 及 密钥
                .signWith(SignatureAlgorithm.HS256,"xxxx")
                //设置过期时间
                //.setExpiration(new Date(expDate))
                //设置自定义声明 Key + Value
                .claim("name","Sooocen")
                .claim("logo","xxx.jpg");
                //通过Map添加
                //.addClaims(map)
        //签发Token
        String token = jwtBuilder.compact();
        System.out.println(token);
        System.out.println("------------------------------------------");
        String[] split = token.split("\\.");
        System.out.println(Base64Codec.BASE64.decodeToString(split[0]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[1]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[2]));
    }
    /**
     * 解析JWT(含自定义声明)
     */
    @Test
    public void testParseTokenHasEnhancer(){
        String token ="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiJSb3NlIiwiaWF0IjoxNjI3MDA2ODQxLCJuYW1lIjoiU29vb2NlbiIsImxvZ28iOiJ4eHguanBnIn0.FLsXeYI7GOO9oVfBFoOf_itZ2-mY1rtigNPFbQm3SDs";
        //解析Token 获取对象 jwt荷载神明的对象
        Claims claims = (Claims)Jwts.parser()
                //必须填写正确的签发密钥
                .setSigningKey("xxxx")
                .parse(token)
                .getBody();
        System.out.println("ID="+claims.getId());
        System.out.println("SUB="+claims.getSubject());
        //System.out.println("EXP="+claims.getExpiration());
        System.out.println("IAT="+claims.getIssuedAt());
        System.out.println("NAME="+claims.get("name"));
        System.out.println("LOGO="+claims.get("logo"));

    }

}
