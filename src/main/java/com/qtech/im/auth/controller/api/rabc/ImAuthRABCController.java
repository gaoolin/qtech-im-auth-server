package com.qtech.im.auth.controller.api.rabc;

import com.qtech.im.auth.common.JwtTokenProvider;
import com.qtech.im.auth.dto.GenerateUserTokenRequest;
import com.qtech.im.auth.service.management.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/01/09 10:50:01
 * desc   :
 */
@RestController
@RequestMapping("/im/auth/rbac")
public class ImAuthRABCController {
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserService userService;

    public ImAuthRABCController(JwtTokenProvider jwtTokenProvider, IUserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/revoke")
    public ResponseEntity<?> revokeApiKey(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract API Key from Authorization header
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
            }
            String apiKey = authorizationHeader.substring(7); // Remove "Bearer " prefix

            boolean b = jwtTokenProvider.validateToken(apiKey);
            if (!b) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
            }
        } catch (Exception e) {
            // 处理异常情况
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
        return ResponseEntity.ok("API Key is valid");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract API Key from Authorization header
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
            }
            String apiKey = authorizationHeader.substring(7); // Remove "Bearer " prefix

            boolean b = jwtTokenProvider.validateToken(apiKey);
            if (!b) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
            }
        } catch (Exception e) {
            // 处理异常情况
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
        return ResponseEntity.ok("API Key is valid");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password, @RequestParam String systemName, @RequestParam String clientId, HttpServletRequest request) {
        try {
            GenerateUserTokenRequest generateUserTokenRequest = new GenerateUserTokenRequest();
            generateUserTokenRequest.setEmployeeId(username);
            generateUserTokenRequest.setSystemName(systemName);
            generateUserTokenRequest.setClientId(clientId);

            if (userService.authenticate(username, password)) {
                String accessToken = jwtTokenProvider.generateTokenForUser(generateUserTokenRequest);

                Map<String, String> response = new HashMap<>();
                response.put("access_token", accessToken);
                return ResponseEntity.ok(response);
            }

            String accessToken = jwtTokenProvider.generateTokenForUser(generateUserTokenRequest);

            Map<String, String> response = new HashMap<>();
            response.put("access_token", accessToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}