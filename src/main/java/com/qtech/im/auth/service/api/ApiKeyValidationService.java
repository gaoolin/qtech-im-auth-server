package com.qtech.im.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/01/09 10:49:23
 * desc   :
 */

@Service
public class ApiKeyValidationService {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    // 校验 API Key
    public Claims validateApiKey(String apiKey) {
        try {
            // 使用 JJWT 解析 JWT，自动处理 Base64Url 编码
            return Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes())  // 设置签名密钥
                    .build()
                    .parseClaimsJws(apiKey)  // 解析 JWT
                    .getBody();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid API Key", e);
        }
    }
}