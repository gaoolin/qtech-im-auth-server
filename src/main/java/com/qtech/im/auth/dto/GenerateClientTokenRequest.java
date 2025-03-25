package com.qtech.im.auth.dto;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/21 08:58:40
 * desc   :
 */


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GenerateClientTokenRequest {
    private String clientId;
    private String clientName;
    private String systemName;
    private String clientSecret;
    private String grantType;
    private String redirectUris;
    private String scope;
    private LocalDateTime createAt;
}
