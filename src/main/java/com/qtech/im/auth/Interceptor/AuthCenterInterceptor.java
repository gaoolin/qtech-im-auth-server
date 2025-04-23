package com.qtech.im.auth.Interceptor;

import com.qtech.im.auth.common.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/22 16:14:39
 * desc   :  用一个 拦截器（Interceptor） 来在所有视图访问之前做一次 Token 检查
 */

@Component
public class AuthCenterInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthCenterInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        // 登录页、静态资源和公开接口不拦截
        /*
         * @description 已通过SecurityConfig配置的认证过滤器，所有请求都会被拦截，所以不需要以下过滤功能，除非需要添加特殊放行路径
         */

        // String path = request.getRequestURI();

        // if (path.contains("/login") ||
        //         path.contains("/error") ||
        //         path.contains("/refresh") ||
        //         path.contains("/static") ||
        //         path.contains("/public") ||
        //         path.contains("/template")) {
        //     return true;
        // }

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && jwtTokenProvider.validateToken(token)) {
            return true;
        }

        response.sendRedirect("/admin/login");
        return false;
    }
}