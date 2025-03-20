package com.qtech.im.auth.controller.management;

import com.qtech.im.auth.common.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 13:52:34
 * desc   :  登录控制器
 */

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password) {
        // 模拟校验，实际应根据数据库或其他认证方式
        if ("admin".equals(username) && "admin123".equals(password)) {
            String token = jwtTokenProvider.generateTokenForUser("admin", "认证中心", "auth-management");
            if (token == null) {
                return "redirect:/login?error=true";
            }
            return "redirect:/home?token=" + token; // 将 Token 作为 URL 参数传递
        }
        return "redirect:/login?error=true"; // 登录失败重定向到登录页
    }

    @GetMapping("/home")
    public String homePage(@RequestParam(required = false) String token, Model model) {
        model.addAttribute("token", token);

        if (token != null) {
            // 从 token 中解析用户名（假设你的 jwtTokenProvider 有解析方法）
            String username = jwtTokenProvider.getUsernameFromToken(token);
            model.addAttribute("username", username);
        } else {
            model.addAttribute("username", "访客");
        }

        return "home";
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            String refreshToken = refreshTokenRequest.getRefreshToken();
            // 验证 refresh token
            if (jwtTokenProvider.validateToken(refreshToken)) {
                String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
                String systemName = jwtTokenProvider.getSystemNameFromToken(refreshToken);
                String appName = jwtTokenProvider.getAppNameFromToken(refreshToken);
                // 根据 refreshToken 生成新的 accessToken
                String newAccessToken = jwtTokenProvider.generateTokenForUser(username, systemName, appName);
                return ResponseEntity.ok(new TokenResponse(newAccessToken));
            } else {
                return ResponseEntity.status(401).build();
            }
        } catch (Exception e) {
            log.error("刷新 access token 出错", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/test")
    @ResponseBody
    public String testApi() {
        return "接口调用成功，已通过认证！";
    }
}
