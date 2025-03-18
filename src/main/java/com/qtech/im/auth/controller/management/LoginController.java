package com.qtech.im.auth.controller.management;

import com.qtech.im.auth.dto.LoginRequest;
import com.qtech.im.auth.utils.JwtTokenProvider;
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
public class LoginController {

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        // 模拟校验，实际应根据数据库或其他认证方式
        if ("admin".equals(username) && "admin123".equals(password)) {
            String token = jwtTokenProvider.generateToken(username);
            return "redirect:/home?token=" + token;
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/home")
    public String homePage(@RequestParam(required = false) String token, Model model) {
        model.addAttribute("token", token);
        return "home";
    }

    @GetMapping("/test")
    @ResponseBody
    public String testApi() {
        return "接口调用成功，已通过认证！";
    }
}
