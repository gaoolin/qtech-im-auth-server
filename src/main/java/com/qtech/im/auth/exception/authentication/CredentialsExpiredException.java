package com.qtech.im.auth.exception.authentication;

import com.qtech.im.auth.common.ResultCode;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:33:46
 * desc   :
 */

public class CredentialsExpiredException extends AuthenticationException {
    public CredentialsExpiredException(String message) {
        super(ResultCode.NO_PERMISSION, message);
    }
}
