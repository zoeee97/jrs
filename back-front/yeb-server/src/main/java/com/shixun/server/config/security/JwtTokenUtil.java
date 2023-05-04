package com.shixun.server.config.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*JwtToken工具类*/
@Component
public class JwtTokenUtil {

    /*用户名*/
    private static final String CLAIM_KEY_USERNAME = "sub";
    /*jwt创建时间*/
    private static final String CLAIM_KEY_CREATED = "created";

    /*jwt的密钥以及过期时间通过value注解从配置文件里拿*/
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /*根据用户信息生成token*/
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

    /**
     * 根据token获取登录用户名
     */
    public String getUserNameFromToken(String token){
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 判断token是否有效：
     * 1.判断token是否过期 2.token荷载里的用户名是否和UserDetails中的用户名一致
     */
    public boolean validateToken(String token,UserDetails userDetails){
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否过期
     */
    private boolean isTokenExpired(String token){
        Date expireDate = getExpiredDateFromToken(token); // 获取 token 失效时间，本类内新建此方法
        return expireDate.before(new Date()); // 如果 token 过期时间在当前时间前面，有效
    }


    /**
     * 从 token 中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token); // 根据 token 获取荷载
        return claims.getExpiration(); // 从荷载中获取过期时间
    }

    /**
     * 判断token是否可以被刷新
     * 过期了，可以刷新。获取有效时间方法取反为过期
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token过期时间
     */
    public String refreshToken(String token){
        Claims claims = getClaimsFromToken(token); // 获取荷载
        claims.put(CLAIM_KEY_CREATED,new Date()); // 通过荷载设置创建时间改为当前时间 = 刷新 token 过期时间
        return generateToken(claims); // 生成 token
    }

    /**
     * 从token中获取荷载
     */
    private Claims getClaimsFromToken(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 根据荷载生成JWT token
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpiration())//过期时间
                .signWith(SignatureAlgorithm.HS512,secret)//签名
                .compact();
    }

    /**
     * 生成token失效时间
     */
    private Date generateExpiration(){
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }
}
