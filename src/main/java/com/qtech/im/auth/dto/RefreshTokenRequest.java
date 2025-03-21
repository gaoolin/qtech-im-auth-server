package com.qtech.im.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 17:37:05
 * desc   :
 */

@AllArgsConstructor
@Data
public class RefreshTokenRequest {
    private String refreshToken;
}