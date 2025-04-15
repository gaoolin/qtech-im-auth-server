package com.qtech.im.auth.controller.management;

import com.qtech.im.auth.common.JwtTokenProvider;
import com.qtech.im.auth.common.Result;
import com.qtech.im.auth.common.ResultCode;
import com.qtech.im.auth.model.dto.GenerateUserTokenRequest;
import com.qtech.im.auth.model.dto.RefreshTokenRequest;
import com.qtech.im.auth.service.management.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthCenterController {
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
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String systemName,
                          @RequestParam String clientId,
                          HttpServletRequest request) {
        GenerateUserTokenRequest generateUserTokenRequest = new GenerateUserTokenRequest();
        generateUserTokenRequest.setEmployeeId(username);
        generateUserTokenRequest.setSystemName(systemName);
        generateUserTokenRequest.setClientId(clientId);

        if (userService.authenticate(username, password)) {
            String accessToken = jwtTokenProvider.generateTokenForUser(generateUserTokenRequest);
            String refreshToken = jwtTokenProvider.generateRefreshTokenForUser(generateUserTokenRequest);

            // 使用 HTTP Header 传递 Token
            request.getSession().setAttribute("access_token", accessToken);
            request.getSession().setAttribute("refresh_token", refreshToken);

            return "redirect:/auth/home";
        }
        return "redirect:/auth/login?error=true"; // 登录失败重定向到登录页
    }

    @GetMapping("/home")
    public String homePage(HttpServletRequest request, Model model) {
        setToken(request, model);
        return "home";
    }

    @GetMapping("/users")
    private String userPage(HttpServletRequest request, Model model) {
        setToken(request, model);
        return "users";
    }

    @GetMapping("/roles")
    private String rolePage(HttpServletRequest request, Model model) {
        setToken(request, model);
        return "roles";
    }

    @GetMapping("/systems")
    private String systemPage(HttpServletRequest request, Model model) {
        setToken(request, model);
        return "systems";
    }

    @GetMapping("/permissions")
    private String permissionPage(HttpServletRequest request, Model model) {
        setToken(request, model);
        return "permissions";
    }

    @GetMapping("/depts")
    private String deptPage(HttpServletRequest request, Model model) {
        setToken(request, model);
        return "depts";
    }

    @PostMapping("/refresh")
    @ResponseBody
    public Result<?> refreshAccessToken(@CookieValue(value = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            String systemName = jwtTokenProvider.getSystemNameFromToken(refreshToken);
            String clientId = jwtTokenProvider.getClientIdFromToken(refreshToken);

            GenerateUserTokenRequest generateUserTokenRequest = new GenerateUserTokenRequest();
            generateUserTokenRequest.setEmployeeId(username);
            generateUserTokenRequest.setSystemName(systemName);
            generateUserTokenRequest.setClientId(clientId);
            String newAccessToken = jwtTokenProvider.generateTokenForUser(generateUserTokenRequest);

            Cookie newAccessCookie = new Cookie("access_token", newAccessToken);
            newAccessCookie.setHttpOnly(true);
            newAccessCookie.setPath("/");
            newAccessCookie.setMaxAge(60 * 30);
            response.addCookie(newAccessCookie);

            return Result.success(new RefreshTokenRequest(newAccessToken));
        } else {
            return Result.failure(ResultCode.UNAUTHORIZED, "无效的 refresh token");
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 清除 Cookie
        Cookie accessCookie = new Cookie("access_token", null);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("refresh_token", null);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        return "redirect:/auth/login";
    }

    @GetMapping("/test")
    @ResponseBody
    public String testApi() {
        return "接口调用成功，已通过认证！";
    }

    private void setToken(HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute("access_token");
        model.addAttribute("access_token", token);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 从 token 中解析用户名
            String employeeId = jwtTokenProvider.getEmployeeIdFromToken(token);
            model.addAttribute("username", employeeId);
        } else {
            model.addAttribute("username", "访客");
        }
    }
}
