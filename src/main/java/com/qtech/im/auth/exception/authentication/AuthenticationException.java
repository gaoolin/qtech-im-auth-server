package com.qtech.im.auth.exception.authentication;

import com.qtech.im.auth.common.ResultCode;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:31:21
 * desc   :  认证异常
 */

public class AuthenticationException extends RuntimeException {
    private final int code;
    private final String message;

    public AuthenticationException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public AuthenticationException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public AuthenticationException(ResultCode resultCode, String customMessage) {
        super(customMessage);
        this.code = resultCode.getCode();
        this.message = customMessage;
    }
}