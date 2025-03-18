package com.qtech.im.auth.controller.api;

import com.qtech.im.auth.dto.AuthResponse;
import com.qtech.im.auth.dto.LoginRequest;
import com.qtech.im.auth.utils.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 14:39:38
 * desc   :  登入、注册、刷新 token
 */

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = JwtUtil.generateToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(Authentication auth) {
        return ResponseEntity.ok(auth.getName());
    }
}

