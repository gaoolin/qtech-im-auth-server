package com.qtech.im.auth.common;

import com.qtech.im.auth.exception.authentication.AuthenticationException;
import com.qtech.im.auth.exception.authentication.CredentialsExpiredException;
import com.qtech.im.auth.exception.biz.ParamIllegalException;
import com.qtech.im.auth.model.OAuthClient;
import com.qtech.im.auth.model.Permission;
import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.model.User;
import com.qtech.im.auth.service.api.*;
import io.jsonwebtoken.Claims;
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
import java.util.Date;
import java.util.Optional;
import java.util.Set;

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

    // 定义常量，避免硬编码
    private static final String CLAIM_EMPLOYEE_ID = "employee_id";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_PERMISSIONS = "permissions";
    private static final String CLAIM_SYSTEM = "system";
    private static final String CLAIM_CLIENT_ID = "client_id";

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    private Key jwtSecretKey;
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IPermissionService permissionService;
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

    // 生成用户 JWT Token
    public String generateTokenForUser(String employeeId, String systemName, String clientId) {
        if (employeeId == null || employeeId.isEmpty() || systemName == null || systemName.isEmpty() || clientId == null || clientId.isEmpty()) {
            log.error(">>>>> Invalid input parameters for generating user token: employeeId={}, systemName={}, clientId={}", employeeId, systemName, clientId);
            return null;
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        Optional<User> employee = userService.findUserByEmployeeId(employeeId);
        if (employee.isEmpty()) {
            log.error(">>>>> User not found with employeeId: {}", employeeId);
            return null;
        }

        User user = employee.get();
        Set<Permission> userPermissions = userService.getUserPermissions(employeeId);
        Set<Role> roles = user.getRoles();

        try {
            return Jwts.builder().setSubject(user.getUsername()).claim(CLAIM_EMPLOYEE_ID, employeeId).claim(CLAIM_ROLES, roles).claim(CLAIM_PERMISSIONS, userPermissions).claim(CLAIM_SYSTEM, systemName).claim(CLAIM_CLIENT_ID, clientId).setIssuedAt(now).setExpiration(expiryDate).signWith(jwtSecretKey, SignatureAlgorithm.HS256).compact();
        } catch (Exception e) {
            log.error(">>>>> Failed to generate JWT token for user: {}", employeeId, e);
            return null;
        }
    }

    // 生成第三方系统调用 Token (client credentials 模式)
    public String generateTokenForClient(String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            log.error(">>>>> Invalid client ID provided for generating client token: clientId={}", clientId);
            return null;
        }

        OAuthClient client = oAuthClientService.findByClientId(clientId);
        if (client == null) {
            log.error(">>>>> OAuth client not found with clientId: {}", clientId);
            return null;
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        try {
            return Jwts.builder().setSubject("system_call").claim(CLAIM_CLIENT_ID, client.getClientId()).claim("client_name", client.getClientName()).claim(CLAIM_SYSTEM, client.getSystemName()).setIssuedAt(now).setExpiration(expiryDate).signWith(jwtSecretKey, SignatureAlgorithm.HS256).compact();
        } catch (Exception e) {
            log.error(">>>>> Failed to generate JWT token for client: {}", clientId, e);
            return null;
        }
    }

    // 从 Token 获取 Claims
    private Claims parseClaims(String token) {
        if (token == null || token.isEmpty()) {
            log.error(">>>>> Invalid token provided for parsing claims.");
            return null;
        }

        try {
            return Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token).getBody();
        } catch (CredentialsExpiredException e) {
            log.warn(">>>>> JWT token has expired: {}", token, e);
            return null;
        } catch (AuthenticationException e) {
            log.error(">>>>> Failed to parse JWT token: {}", token, e);
            return null;
        }
    }

    // 验证 JWT 是否有效
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            log.error(">>>>> Invalid token provided for validation.");
            return false;
        }

        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            log.warn(">>>>> Validation failed for token: {}", token, e);
            return false;
        }
    }

    // 获取用户名
    public String getUsernameFromToken(String token) {
        Claims claims = parseClaims(token);
        if (claims == null) {
            return null;
        }
        return claims.getSubject();
    }

    // 获取 client_id
    public String getClientIdFromToken(String token) {
        Claims claims = parseClaims(token);
        if (claims == null) {
            return null;
        }
        return (String) claims.get(CLAIM_CLIENT_ID);
    }
}
