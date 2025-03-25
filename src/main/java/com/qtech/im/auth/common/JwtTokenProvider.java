package com.qtech.im.auth.common;

import com.qtech.im.auth.dto.GenerateUserTokenRequest;
import com.qtech.im.auth.exception.biz.ParamIllegalException;
import com.qtech.im.auth.exception.biz.TokenGenerationException;
import com.qtech.im.auth.model.OAuthClient;
import com.qtech.im.auth.model.Permission;
import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.User;
import com.qtech.im.auth.security.CustomUserDetails;
import com.qtech.im.auth.service.api.IOAuthClientService;
import com.qtech.im.auth.service.management.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import static com.qtech.im.auth.utils.SysConstants.*;

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
 * <p>
 * getUsernameFromToken：
 * <p>
 * 从 token 中解析出 username，这通常是 JWT 中的 subject 部分。
 * validateToken：
 * <p>
 * 通过调用 getClaimsFromToken 来验证 JWT 的有效性。如果 JWT 解析成功，则认为它是有效的。
 * getClaimsFromToken：
 * <p>
 * 使用 Jwts.parser() 方法来解析 JWT 并获取其中的声明（Claims）。如果解析失败，抛出异常。
 * generateToken：
 * <p>
 * 用于生成一个新的 JWT token。包括设置 username、issuedAt（发行时间）和 expiration（过期时间）。
 */

@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration; // access_token 有效期

    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpiration; // refresh_token 有效期（建议在 yml 里配置）

    private Key jwtSecretKey;

    @Autowired
    private IUserService userService;
    @Autowired
    private IOAuthClientService oAuthClientService;

    @PostConstruct
    public void init() {
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            log.error(">>>>> JWT secret is not configured properly.");
            throw new ParamIllegalException("JWT secret must be provided.");
        }
        this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // 生成 access_token
    public String generateTokenForUser(GenerateUserTokenRequest request) {
        String employeeId = request.getEmployeeId();
        String username = request.getUsername();
        String systemName = request.getSystemName();
        String clientId = request.getClientId();
        if ((request.getUsername() == null && request.getEmployeeId() == null) || systemName == null || clientId == null) {
            log.error(">>>>> Invalid parameters for access token: username={}, employeeId={}, systemName={}, clientId={}", username, employeeId, systemName, clientId);
            throw new TokenGenerationException("Invalid parameters for access token");
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        Optional<User> employeeOpt = userService.findUserByEmployeeId(employeeId);
        if (employeeOpt.isEmpty()) {
            log.error(">>>>> User not found by employeeId: {}", employeeId);
            return null;
        }

        User user = employeeOpt.get();
        Set<Permission> permissions = userService.getUserPermissions(employeeId);
        Set<Role> roles = user.getRoles();

        try {
            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .claim(CLAIM_EMPLOYEE_ID, employeeId)
                    .claim(CLAIM_USERNAME, username)
                    .claim(CLAIM_ROLES, roles)
                    .claim(CLAIM_PERMISSIONS, permissions)
                    .claim(CLAIM_SYSTEM, systemName)
                    .claim(CLAIM_CLIENT_ID, clientId)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error(">>>>> Error generating access token for user: {}", employeeId, e);
            return null;
        }
    }

    // ⭐ 生成 refresh_token（只保存 employeeId、system、clientId 等关键信息）
    public String generateRefreshTokenForUser(GenerateUserTokenRequest request) {
        String employeeId = request.getEmployeeId();
        String username = request.getUsername();
        String systemName = request.getSystemName();
        String clientId = request.getClientId();

        if ((request.getUsername() == null && request.getEmployeeId() == null) || systemName == null || clientId == null) {
            log.error(">>>>> Invalid parameters for refresh token: username={} employeeId={}, systemName={}, clientId={}", username, employeeId, systemName, clientId);
            return null;
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshExpiration);

        try {
            return Jwts.builder()
                    .setSubject(employeeId)
                    .claim(CLAIM_EMPLOYEE_ID, employeeId)
                    .claim(CLAIM_USERNAME, username)
                    .claim(CLAIM_SYSTEM, systemName)
                    .claim(CLAIM_CLIENT_ID, clientId)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error(">>>>> Error generating refresh token for user: {}", employeeId, e);
            return null;
        }
    }

    public String generateTokenForClient(String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            log.error(">>>>> Invalid clientId for client credentials token");
            return null;
        }

        OAuthClient client = oAuthClientService.findByClientId(clientId);
        if (client == null) {
            log.error(">>>>> OAuth client not found: {}", clientId);
            return null;
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        try {
            return Jwts.builder()
                    .setSubject("system_call")
                    .claim(CLAIM_CLIENT_ID, clientId)
                    .claim("client_name", client.getClientName())
                    .claim(CLAIM_SYSTEM, client.getSystemName())
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error(">>>>> Error generating client token: {}", clientId, e);
            return null;
        }
    }

    // 通用解析
    private Claims parseClaims(String token) {
        if (token == null || token.isEmpty()) {
            log.error(">>>>> Provided token is null or empty.");
            return null;
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn(">>>>> JWT token expired: {}", e.getMessage());
            return e.getClaims(); // 注意：过期也可以解析出来 claims
        } catch (Exception e) {
            log.error(">>>>> Failed to parse JWT token: {}", e.getMessage(), e);
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims != null;
        } catch (Exception e) {
            log.warn(">>>>> Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    public String getClientIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims != null ? (String) claims.get(CLAIM_CLIENT_ID) : null;
    }

    public String getEmployeeIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims != null ? (String) claims.get(CLAIM_EMPLOYEE_ID) : null;
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = parseClaims(token);
        if (claims == null) return Collections.emptyList();

        Object roles = claims.get(CLAIM_ROLES);
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims != null ? claims.getExpiration() : null;
    }

    public CustomUserDetails getUserDetailsFromToken(String token) {
        Claims claims = parseClaims(token);
        if (claims == null) return null;

        Long id = claims.getId() != null ? Long.parseLong(claims.getId()) : null;
        String employeeId = claims.get(CLAIM_EMPLOYEE_ID) != null ? claims.get(CLAIM_EMPLOYEE_ID).toString() : null;
        String username = claims.getSubject() != null ? claims.getSubject() : null;

        return new CustomUserDetails(id, employeeId, username, null, true, Collections.emptySet());
    }

    public String getSystemNameFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims != null ? (String) claims.get(CLAIM_SYSTEM) : null;
    }
}
