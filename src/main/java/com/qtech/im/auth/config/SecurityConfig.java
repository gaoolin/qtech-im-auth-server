package com.qtech.im.auth.config;

import com.qtech.im.auth.utils.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/01/09 11:07:53
 * desc   :
 * <p>
 * 解释：
 * SecurityFilterChain：Spring Security 3 引入了 SecurityFilterChain 来代替 WebSecurityConfigurerAdapter。我们使用 @Bean 注解创建了一个配置 SecurityFilterChain，并在 http 对象上配置安全策略。
 * addFilterBefore：通过 addFilterBefore 方法，我们在认证过滤器 UsernamePasswordAuthenticationFilter 前添加了 JwtAuthenticationFilter，用来解析和验证 JWT。
 */
@Configuration
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 禁用 CSRF 是必要的吗？ 通常情况下，建议仅在以下场景禁用 CSRF：
     * API 是无状态的（使用 Bearer Token 或 API Key 身份验证）。
     * 接口仅用于服务器间调用。
     * 调试或开发阶段临时禁用。
     * 如果接口需要处理用户登录、表单提交等操作，不建议禁用 CSRF。
     * <p>
     * 如果需要保留 CSRF 保护，请添加 CSRF Token 支持。
     *
     * @Configuration public class SecurityConfig {
     * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     * http
     * .authorizeHttpRequests(auth -> auth
     * .requestMatchers("/im/auth/generate").permitAll()
     * .anyRequest().authenticated()
     * )
     * .csrf(csrf -> csrf.ignoringRequestMatchers("/im/auth/generate")); // 对特定接口忽略 CSRF
     * <p>
     * return http.build();
     * }
     * }
     */
    // 使用 SecurityFilterChain 代替 WebSecurityConfigurerAdapter
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // 禁用 CSRF，因为我们使用 JWT 进行身份验证
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                                "/**",
                                // "/im/auth/generate",
                                // "/im/auth/validate",
                                "/login",
                                "/api/login",
                                "/static/**",
                                "/resources/**")  // 允许特定接口 允许公开访问登录接口

                        .permitAll().anyRequest().authenticated()  // 其他接口需要认证
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // 添加 JWT 认证过滤器;


        return http.build();
    }

    // 密码编码器 使用 PasswordEncoder 加密密码
    // 使用 BCryptPasswordEncoder 进行密码加密，符合 Spring Security 规范
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}