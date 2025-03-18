package com.qtech.im.auth.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 14:37:35
 * desc   :  JWT 生产与解析工具
 */

public class JwtUtil {
    private static final String SECRET_KEY = "im-secret-key";

    /**
     * @description
     * @param username
     * @return java.lang.String
     * 此方法用于生成token
     */
    public static String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 天
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    /**
     * @description
     * @param token
     * @return java.lang.String
     * 此方法用于 从token中提取用户名
     */
    public static String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
