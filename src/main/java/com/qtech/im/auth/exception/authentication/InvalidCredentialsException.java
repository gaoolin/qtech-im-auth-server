package com.qtech.im.auth.exception.authentication;

import com.qtech.im.auth.common.ResultCode;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:31:55
 * desc   :
 */

public class InvalidCredentialsException extends AuthenticationException {
    public InvalidCredentialsException() {
        super(ResultCode.NO_PERMISSION);
    }

    public InvalidCredentialsException(String message) {
        super(ResultCode.NO_PERMISSION, message);
    }
}
