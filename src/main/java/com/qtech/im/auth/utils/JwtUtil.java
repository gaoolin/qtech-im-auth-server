package com.qtech.im.auth.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 14:37:35
 * desc   :  JWT 生成与解析工具
 */

public class JwtUtil {
    // 自动生成符合 HS256 要求的密钥
    private static final byte[] SECRET_KEY_BYTES = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();

    /**
     * @param username
     * @return java.lang.String
     * 此方法用于生成token
     * @description
     */
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 天
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY_BYTES) // 使用符合要求的密钥
                .compact();
    }

    /**
     * @param token
     * @return java.lang.String
     * 此方法用于 从token中提取用户名
     * @description
     */
    public static String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY_BYTES) // 使用相同的密钥解析
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
    }
}
