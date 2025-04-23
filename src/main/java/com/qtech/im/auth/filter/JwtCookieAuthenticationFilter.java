package com.qtech.im.auth.filter;

import com.qtech.im.auth.common.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/22 14:15:58
 * desc   :  用于管理系统后台，解析 Cookie 中的 access_token。
 */
public class JwtCookieAuthenticationFilter extends AbstractJwtAuthenticationFilter {

    public JwtCookieAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        super(jwtTokenProvider);
    }

    @Override
    protected String resolveToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
