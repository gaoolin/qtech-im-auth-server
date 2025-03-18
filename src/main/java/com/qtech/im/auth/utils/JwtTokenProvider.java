package com.qtech.im.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/14 11:17:13
 * desc   :  生成和解析 JWT
 * <p>
 * 说明：
 * generateToken(String username)：生成 JWT，并通过用户名生成一个包含该信息的 Token，同时设置过期时间。
 * getUsername(String token)：从 JWT 中提取用户名（Subject）。
 * parseClaims(String token)：解析 JWT，并返回其中的 Claims 信息。
 * validateToken(String token)：验证 JWT 是否有效，主要通过检查是否过期来判断。
 * JwtTokenProvider 类中的 validateToken 和 getUsernameFromToken 方法。这些方法主要是验证 JWT 的有效性以及从中提取用户信息。
 *
 * getUsernameFromToken：
 *
 * 从 token 中解析出 username，这通常是 JWT 中的 subject 部分。
 * validateToken：
 *
 * 通过调用 getClaimsFromToken 来验证 JWT 的有效性。如果 JWT 解析成功，则认为它是有效的。
 * getClaimsFromToken：
 *
 * 使用 Jwts.parser() 方法来解析 JWT 并获取其中的声明（Claims）。如果解析失败，抛出异常。
 * generateToken：
 *
 * 用于生成一个新的 JWT token。包括设置 username、issuedAt（发行时间）和 expiration（过期时间）。
 */

@Component
public class JwtTokenProvider {

    // 密钥，用于加密和解密 JWT
    // 使用 Keys.secretKeyFor 方法生成符合 HS256 要求的密钥
    private final Key JWT_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final String JWT_SECRET = "yourSecretKey";  // 请替换为你自己的密钥
    private static final long JWT_EXPIRATION = 86400000; // 令牌有效期（毫秒）

    // JWT有效期（以毫秒为单位，设置为1小时）
    private final long validityInMilliseconds = 3600000L;

    // 生成 JWT Token
    // public String generateToken(String username) {
    //     // 设置过期时间
    //     Date now = new Date();
    //     Date validity = new Date(now.getTime() + validityInMilliseconds);
    //
    //     // 通过用户名创建 JWT，并设置有效期，签名算法以及密钥
    //     return Jwts.builder().setSubject(username) // 存放用户名作为主体
    //             .setIssuedAt(now) // 设置发放时间
    //             .setExpiration(validity) // 设置过期时间
    //             .signWith(SignatureAlgorithm.HS256, secretKey) // 使用HS256加密算法进行签名
    //             .compact(); // 生成 Token
    // }

    // 从 Token 获取用户名
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // 从 Token 获取到 Claims（即存储在 JWT 中的其他信息）
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(JWT_SECRET_KEY) // 设置密钥进行解析
                .build().parseClaimsJws(token).getBody(); // 返回 JWT 内部的 Claims
    }

    // 从 JWT 中提取用户名
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    // 验证 JWT 是否有效
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);  // 如果解析成功，表示 token 有效
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;  // 解析失败，表示 token 无效
        }
    }

    // 从 JWT 中提取所有的声明信息
    // 解析 JWT 并获取 Claims
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET_KEY) // 使用密钥对象进行解析
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // 生成 JWT
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(JWT_SECRET_KEY) // 使用密钥对象进行签名
                .compact();
    }
}
