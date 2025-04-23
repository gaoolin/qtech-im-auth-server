package com.qtech.im.auth.config;

import com.qtech.im.auth.common.JwtTokenProvider;
import com.qtech.im.auth.filter.PathMatchingJwtFilter;
import com.qtech.im.auth.security.resolver.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/22 14:22:57
 * desc   :
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenResolver headerOnly = new HeaderTokenResolver();
        JwtTokenResolver cookieOnly = new CookieTokenResolver("ACCESS_TOKEN");

        JwtTokenResolver globalFallback = new CompositeTokenResolver(List.of(cookieOnly, headerOnly));

        Map<String, JwtTokenResolver> strategyMap = new LinkedHashMap<>();
        strategyMap.put("/admin/.*", cookieOnly);
        strategyMap.put("/api/.*", headerOnly);

        JwtTokenResolver pathResolver = new PathBasedTokenResolver(strategyMap, globalFallback);

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/css/**", "/js/**", "/images/**", "/templates/**", "/public/**", "/admin/login", "/admin/logout")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(new PathMatchingJwtFilter(jwtTokenProvider, pathResolver), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}