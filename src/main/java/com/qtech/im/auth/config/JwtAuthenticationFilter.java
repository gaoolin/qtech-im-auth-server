package com.qtech.im.auth.config;

import com.qtech.im.auth.utils.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 14:40:46
 * desc   :  JWT 过滤器
 * <p>
 * JWT 过滤器用于拦截 HTTP 请求，检查请求头中是否携带有效的 JWT。如果有效，则将用户信息传递给 Spring Security 的上下文。
 * <p>
 * 解释：
 * getJwtFromRequest：
 * <p>
 * 从请求的 Authorization 头中获取 JWT token。如果 Authorization 头的格式是 Bearer <token>，则提取 token 部分。
 * validateToken：
 * <p>
 * 使用 JwtTokenProvider 来验证 JWT 是否有效。如果 JWT 无效或过期，则不继续认证。
 * UsernamePasswordAuthenticationToken：
 * <p>
 * 使用从 JWT 中获取到的用户名创建一个 UsernamePasswordAuthenticationToken。你可以在这个对象中设置角色和权限，或者通过后续的 GrantedAuthority 来处理。
 * WebAuthenticationDetailsSource：
 * <p>
 * 通过 WebAuthenticationDetailsSource 设置额外的认证信息（如用户的 IP 地址）。你可以根据需求进行修改。
 * SecurityContextHolder：
 * <p>
 * 将认证信息放入 SecurityContext 中。Spring Security 会基于 SecurityContext 判断用户是否已认证。
 * doFilter：
 * <p>
 * 调用 filterChain.doFilter(request, response) 以继续执行过滤链的其余部分（如后续的安全过滤器）。
 * <p>
 * <p>
 * 总结：
 * JwtAuthenticationFilter 会在每次请求时从请求头中提取 JWT，验证其有效性，并将用户信息存放在 SecurityContext 中。
 * JwtTokenProvider 负责处理 JWT 的生成、验证和解析。
 * 如果对 JWT 认证机制还不熟悉，建议逐步了解 JWT 的生成和验证过程，理解如何通过它来确保 API 请求的安全性。
 */

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 从请求头中获取 JWT token
            String token = getJwtFromRequest(request);

            // 如果 token 不为空且合法，则进行认证
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 获取用户信息
                String username = jwtTokenProvider.getUsernameFromToken(token);

                // 创建一个认证对象（包括用户名和权限信息）
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);  // 你可以根据需要设置角色

                // 设置细节信息，常见的就是用户的 IP 等信息
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 将认证信息放到 SecurityContext 中
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (AuthenticationException e) {
            response.sendRedirect("/login?error=true");
            return;
        }

        log.info(">>>>> JwtAuthenticationFilter doFilterInternal");
        // 继续执行后续的过滤链
        filterChain.doFilter(request, response);
    }

    // 从请求头获取 token
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // 去掉 "Bearer " 前缀
        }
        return null;
    }
}