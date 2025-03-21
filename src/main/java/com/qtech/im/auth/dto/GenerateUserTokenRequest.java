package com.qtech.im.auth.dto;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/21 08:57:54
 * desc   :  将生成 token 和返回 token 的请求参数与响应包装为 DTO，便于接口调用和统一管理
 */

import lombok.Data;

@Data
public class GenerateUserTokenRequest {
    private String employeeId;
    private String systemName;
    private String clientId;
}

