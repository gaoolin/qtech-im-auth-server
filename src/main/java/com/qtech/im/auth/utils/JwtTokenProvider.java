package com.qtech.im.auth.utils;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

@Component
public class JwtTokenProvider {

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
        // 根据密钥字符串生成密钥对象
        this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // 生成用户 JWT Token
    public String generateTokenForUser(String employeeId, String systemName, String clientId) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        Set<Permission> userPermissions = userService.getUserPermissions(employeeId);
        Optional<User> employee = userService.findUserByEmployeeId(employeeId);
        if (employee.isEmpty()) {
            return null;
        }
        String username = employee.get().getUsername();
        Set<Role> roles = employee.get().getRoles();

        return Jwts.builder().setSubject(username).claim("employee_id", employeeId).claim("roles", roles).claim("permissions", userPermissions).claim("system", systemName).claim("client_id", clientId).setIssuedAt(now).setExpiration(expiryDate).signWith(jwtSecretKey, SignatureAlgorithm.HS256).compact();
    }

    // 生成第三方系统调用 Token (client credentials 模式)
    public String generateTokenForClient(String clientId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        OAuthClient client = oAuthClientService.findByClientId(clientId);

        return Jwts.builder().setSubject("system_call").claim("client_id", client.getClientId()).claim("client_name", client.getClientName()).claim("system", client.getSystemName()).setIssuedAt(now).setExpiration(expiryDate).signWith(jwtSecretKey, SignatureAlgorithm.HS256).compact();
    }

    // 从 Token 获取 Claims
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token).getBody();
    }

    // 验证 JWT 是否有效
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 获取用户名
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    // 获取 client_id
    public String getClientIdFromToken(String token) {
        return (String) parseClaims(token).get("client_id");
    }
}
