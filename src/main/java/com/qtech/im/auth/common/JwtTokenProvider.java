package com.qtech.im.auth.common;

import com.qtech.im.auth.exception.biz.ParamIllegalException;
import com.qtech.im.auth.exception.biz.TokenGenerationException;
import com.qtech.im.auth.model.dto.GenerateUserTokenRequest;
import com.qtech.im.auth.model.entity.primary.OAuthClient;
import com.qtech.im.auth.model.entity.primary.Permission;
import com.qtech.im.auth.model.entity.primary.Role;
import com.qtech.im.auth.model.entity.primary.User;
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
    private long jwtRefreshExpiration; // refresh_token 有效期

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
        validateRequest(request);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        Optional<User> employeeOpt = userService.findUserByEmpId(request.getEmployeeId());
        if (employeeOpt.isEmpty()) {
            log.error(">>>>> User not found by employeeId: {}", request.getEmployeeId());
            return null;
        }

        User user = employeeOpt.get();
        Set<Permission> permissions = userService.getUserPerms(request.getEmployeeId());
        Set<Role> roles = user.getRoles();

        try {
            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .claim(CLAIM_EMPLOYEE_ID, request.getEmployeeId())
                    .claim(CLAIM_USERNAME, request.getUsername())
                    .claim(CLAIM_ROLES, roles)
                    .claim(CLAIM_PERMISSIONS, permissions)
                    .claim(CLAIM_SYSTEM, request.getSystemName())
                    .claim(CLAIM_CLIENT_ID, request.getClientId())
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error(">>>>> Error generating access token for user: {}", request.getEmployeeId(), e);
            throw new TokenGenerationException("Failed to generate access token");
        }
    }

    // 生成 refresh_token
    public String generateRefreshTokenForUser(GenerateUserTokenRequest request) {
        validateRequest(request);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshExpiration);

        try {
            return Jwts.builder()
                    .setSubject(request.getEmployeeId())
                    .claim(CLAIM_EMPLOYEE_ID, request.getEmployeeId())
                    .claim(CLAIM_USERNAME, request.getUsername())
                    .claim(CLAIM_SYSTEM, request.getSystemName())
                    .claim(CLAIM_CLIENT_ID, request.getClientId())
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error(">>>>> Error generating refresh token for user: {}", request.getEmployeeId(), e);
            throw new TokenGenerationException("Failed to generate refresh token");
        }
    }

    // 生成 client_token
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
            throw new TokenGenerationException("Failed to generate client token");
        }
    }

    // 公共解析方法
    private Claims parseClaims(String token) throws ExpiredJwtException {
        if (token == null || token.isEmpty()) {
            log.error(">>>>> Provided token is null or empty.");
            throw new IllegalArgumentException("Token cannot be null or empty.");
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .setAllowedClockSkewSeconds(300) // 允许5分钟的时钟偏差
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn(">>>>> JWT token expired: {}", e.getMessage());
            throw e; // 明确抛出过期异常
        } catch (Exception e) {
            log.error(">>>>> Failed to parse JWT token: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Invalid token.", e);
        }
    }

    // 验证 Token
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn(">>>>> Token has expired: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.warn(">>>>> Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    // 获取用户名
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getSubject();
        } catch (Exception e) {
            log.warn(">>>>> Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    // 获取 Client ID
    public String getClientIdFromToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return (String) claims.get(CLAIM_CLIENT_ID);
        } catch (Exception e) {
            log.warn(">>>>> Failed to extract client ID from token: {}", e.getMessage());
            return null;
        }
    }

    // 获取 Employee ID
    public String getEmployeeIdFromToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return (String) claims.get(CLAIM_EMPLOYEE_ID);
        } catch (Exception e) {
            log.warn(">>>>> Failed to extract employee ID from token: {}", e.getMessage());
            return null;
        }
    }

    // 获取角色列表
    public List<String> getRolesFromToken(String token) {
        try {
            Claims claims = parseClaims(token);
            Object roles = claims.get(CLAIM_ROLES);
            if (roles instanceof List<?>) {
                return ((List<?>) roles).stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        } catch (Exception e) {
            log.warn(">>>>> Failed to extract roles from token: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // 获取过期时间
    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration();
        } catch (Exception e) {
            log.warn(">>>>> Failed to extract expiration date from token: {}", e.getMessage());
            return null;
        }
    }

    // 获取用户详情
    public CustomUserDetails getUserDetailsFromToken(String token) {
        try {
            Claims claims = parseClaims(token);
            Long id = claims.getId() != null ? Long.parseLong(claims.getId()) : null;
            String employeeId = claims.get(CLAIM_EMPLOYEE_ID) != null ? claims.get(CLAIM_EMPLOYEE_ID).toString() : null;
            String username = claims.getSubject() != null ? claims.getSubject() : null;

            return new CustomUserDetails(id, employeeId, username, null, true, Collections.emptySet());
        } catch (Exception e) {
            log.warn(">>>>> Failed to extract user details from token: {}", e.getMessage());
            return null;
        }
    }

    // 获取系统名称
    public String getSystemNameFromToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return (String) claims.get(CLAIM_SYSTEM);
        } catch (Exception e) {
            log.warn(">>>>> Failed to extract system name from token: {}", e.getMessage());
            return null;
        }
    }

    // 请求校验
    private void validateRequest(GenerateUserTokenRequest request) {
        if ((request.getUsername() == null && request.getEmployeeId() == null) ||
                request.getSystemName() == null || request.getClientId() == null) {
            log.error(">>>>> Invalid parameters for token generation: {}", request);
            throw new IllegalArgumentException("Invalid parameters for token generation.");
        }
    }
}