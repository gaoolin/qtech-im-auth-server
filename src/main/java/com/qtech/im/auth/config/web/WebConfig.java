package com.qtech.im.auth.config.web;

import com.qtech.im.auth.Interceptor.AuthCenterInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/22 16:16:31
 * desc   :  注册拦截器
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthCenterInterceptor authCenterInterceptor;

    public WebConfig(AuthCenterInterceptor authCenterInterceptor) {
        this.authCenterInterceptor = authCenterInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authCenterInterceptor)
                .addPathPatterns("/auth/**")
                .excludePathPatterns("/auth/login", "/auth/refresh", "/auth/error", "/auth/404", "/auth/test");
    }
}
