package com.qtech.im.auth.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 15:40:08
 * desc   :  登入请求数据模型
 */

@Data
public class AuthLoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String systemName;

    @NotBlank
    private String clientId;

    private String clientSecret;

    private String grantType;

    // Getter & Setter
}
