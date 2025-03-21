package com.qtech.im.auth.controller.management;

import com.qtech.im.auth.common.JwtTokenProvider;
import com.qtech.im.auth.common.Result;
import com.qtech.im.auth.common.ResultCode;
import com.qtech.im.auth.dto.RefreshTokenRequest;
import com.qtech.im.auth.model.User;
import com.qtech.im.auth.service.management.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 13:52:34
 * desc   :  登录控制器
 */

@Controller
@Slf4j
@RequestMapping("/auth")
public class AuthCenterController {
    private static final String THIS_CLIENT_ID = "AUTH-CENTER";
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserService userService;

    public AuthCenterController(IUserService userService, JwtTokenProvider jwtTokenProvider) {
        log.info(">>>>> AuthCenterController initialized");
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        // 调用认证服务进行校验
        if (userService.authenticate(username, password)) {
            String token = jwtTokenProvider.generateTokenForUser(username, "认证中心", THIS_CLIENT_ID);
            if (token == null) {
                return "redirect:/auth/login?error=true";
            }
            // 使用 HTTP Header 传递 Token
            request.getSession().setAttribute("token", token);
            return "redirect:/auth/home";
        }
        return "redirect:/auth/login?error=true"; // 登录失败重定向到登录页
    }

    @GetMapping("/home")
    public String homePage(HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute("token");
        model.addAttribute("token", token);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 从 token 中解析用户名
            String username = jwtTokenProvider.getUsernameFromToken(token);
            model.addAttribute("username", username);
        } else {
            model.addAttribute("username", "访客");
        }

        return "home";
    }

    @GetMapping("/users")
    public String getUserInfo(Model model) {
        List<?> users = userService.findAllUsers();
        model.addAttribute("users", users); // 一次性添加所有用户信息
        return "user-list";
    }

    @GetMapping("/users/list")
    @ResponseBody
    public List<User> getUserInfo(@RequestParam(required = false) String keyword) {
        List<User> users = userService.findAllUsers();
        if (keyword != null && !keyword.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getEmployeeId().contains(keyword) || u.getUsername().contains(keyword))
                    .collect(Collectors.toList());
        }
        return users;
    }


    @PostMapping("/refresh")
    public Result<?> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            String refreshToken = refreshTokenRequest.getRefreshToken();
            // 验证 refresh token
            if (jwtTokenProvider.validateToken(refreshToken)) {
                String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
                String systemName = jwtTokenProvider.getSystemNameFromToken(refreshToken);
                String appName = jwtTokenProvider.getClientIdFromToken(refreshToken);
                // 根据 refreshToken 生成新的 accessToken
                String newAccessToken = jwtTokenProvider.generateTokenForUser(username, systemName, appName);
                return Result.success(new RefreshTokenRequest(newAccessToken));
            } else {
                return Result.failure(ResultCode.CUSTOM_ERROR, "无效的 refresh token");
            }
        } catch (IllegalArgumentException e) {
            log.error("刷新 access token 出错 - 参数非法", e);
            return Result.failure(ResultCode.PARAM_ILLEGAL, "参数非法");
        } catch (Exception e) {
            log.error("刷新 access token 出错", e);
            return Result.failure(ResultCode.CUSTOM_ERROR, "刷新 access token 出错");
        }
    }

    @GetMapping("/test")
    @ResponseBody
    public String testApi() {
        return "接口调用成功，已通过认证！";
    }
}
