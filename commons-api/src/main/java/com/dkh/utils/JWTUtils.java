package com.dkh.utils;

import io.jsonwebtoken.*;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class JWTUtils {
    //过期时间
    private static long tokenExpiration = 24 * 60 * 60 * 1000;
    //签名秘钥
    private static String tokenSignKey = "123456";

    //根据参数生成token
    public static String createToken(Long userId, String userName) {
        String token = Jwts.builder()
                .setSubject("USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    //根据token字符串得到用户id
    public static Long getUserId(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Long userId = (Long) claims.get("userId");
        return userId;
    }

    //根据token字符串得到用户名称
    public static String getUserName(String token) {
        if (StringUtils.isEmpty(token)) {return "";}

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("userName");
    }

    public static void main(String[] args) {
        String token = JWTUtils.createToken(1L, "lucy");
        System.out.println(token);
        System.out.println(JWTUtils.getUserId(token));
        System.out.println(JWTUtils.getUserName(token));
    }
}
