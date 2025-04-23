package com.qtech.im.auth.security.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/22 15:02:13
 * desc   :  Cookie解析
 */
public class CookieTokenResolver implements JwtTokenResolver {
    private final String cookieName;

    public CookieTokenResolver(String cookieName) {
        this.cookieName = cookieName;
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(StringUtils.upperCase(cookie.getName()))) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}