package com.qtech.im.auth.controller.api;

import com.qtech.im.auth.service.api.ApiKeyService;
import com.qtech.im.auth.service.api.ApiKeyValidationService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/im/auth")
public class ImAuthController {

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private ApiKeyValidationService validationService;

    // 生成 API Key
    @PostMapping("/generate")
    public Map<String, String> generateApiKey(@RequestParam String clientId, @RequestParam long validityInMillis) {
        String apiKey = apiKeyService.generateApiKey(clientId, validityInMillis);
        Map<String, String> response = new HashMap<>();
        response.put("apiKey", apiKey);
        return response;
    }

    // 校验 API Key

    @PostMapping("/validate")
    public ResponseEntity<?> validateApiKey(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract API Key from Authorization header
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
            }
            String apiKey = authorizationHeader.substring(7); // Remove "Bearer " prefix

            Claims claims = validationService.validateApiKey(apiKey);
            if (claims != null) {
                return ResponseEntity.ok(claims);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
            }
        } catch (Exception e) {
            // 处理异常情况
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}