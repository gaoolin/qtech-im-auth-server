package com.qtech.im.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/01/09 10:48:23
 * desc   :
 */


@Service
public class ApiKeyService {

    // 生成 API Key
    public String generateApiKey(String clientId, long validityInMillis) {
        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + validityInMillis);

        Map<String, Object> claims = new HashMap<>();
        claims.put("clientId", clientId);
        claims.put("expiry", expiryDate);

        // 生成一个足够安全的密钥
        Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // 生成 JWT 并返回
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .signWith(secretKey)  // 使用自动生成的密钥
                .compact();
    }
}