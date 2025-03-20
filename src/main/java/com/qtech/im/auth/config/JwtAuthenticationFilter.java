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
 */

/**
 * JWT 过滤器
 * <p>
 * JWT 过滤器用于拦截 HTTP 请求，检查请求头中是否携带有效的 JWT。如果有效，则将用户信息传递给 Spring Security 的上下文。
 * </p>
 * <p>
 * 主要功能：
 * 1. 从请求的 Authorization 头中获取 JWT token。
 * 2. 验证 JWT 是否有效。
 * 3. 如果有效，提取用户名并创建认证对象。
 * 4. 将认证信息放入 SecurityContext 中。
 * </p>
 * <p>
 * 关键方法：
 * - {@link #doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)}: 核心过滤逻辑。
 * - {@link #getJwtFromRequest(HttpServletRequest)}: 从请求头中提取 JWT token。
 * </p>
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 构造函数，注入 JwtTokenProvider。
     *
     * @param jwtTokenProvider JWT 提供者，用于生成、验证和解析 JWT。
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 核心过滤逻辑，拦截每个 HTTP 请求并处理 JWT 认证。
     *
     * @param request     HTTP 请求对象。
     * @param response    HTTP 响应对象。
     * @param filterChain 过滤链对象，用于继续执行后续的过滤器。
     * @throws ServletException 如果发生 Servlet 异常。
     * @throws IOException      如果发生 IO 异常。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 从请求头中获取 JWT token
            String token = getJwtFromRequest(request);
            log.debug(">>>>> JWT Token from request: {}", token != null ? token.substring(0, Math.min(token.length(), 10)) + "..." : "null");

            // 如果 token 不为空且合法，则进行认证
            if (token != null && jwtTokenProvider.validateToken(token)) {
                log.debug(">>>>> JWT Token is valid");

                // 获取用户信息
                String username = jwtTokenProvider.getUsernameFromToken(token);
                log.debug(">>>>> Username extracted from JWT: {}", username);

                // 创建一个认证对象（包括用户名和权限信息）
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);  // 你可以根据需要设置角色

                // 设置细节信息，常见的就是用户的 IP 等信息
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 将认证信息放到 SecurityContext 中
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug(">>>>> Authentication set in SecurityContext for user: {}", username);
            } else {
                log.debug(">>>>> JWT Token is invalid or null");
            }
        } catch (AuthenticationException e) {
            log.warn(">>>>> Authentication failed: {}", e.getMessage(), e);
            response.sendRedirect("/login?error=true");
            return;
        } catch (Exception e) {
            log.error(">>>>> Unexpected error occurred: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error");
            return;
        }

        log.info(">>>>> JwtAuthenticationFilter doFilterInternal");
        // 继续执行后续的过滤链
        filterChain.doFilter(request, response);
    }

    // 从请求头获取 token
    /**
     * 从请求头中获取 JWT token。
     *
     * @param request HTTP 请求对象。
     * @return JWT token 字符串，如果未找到或格式不正确则返回 null。
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // 检查是否满足最小长度要求
            if (bearerToken.length() > 7) {
                return bearerToken.substring(7);  // 去掉 "Bearer " 前缀
            }
            log.warn(">>>>> Invalid Authorization header format: {}", bearerToken);
        }
        return null;
    }
}