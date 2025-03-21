package com.qtech.im.auth.exception.authentication;

import com.qtech.im.auth.common.ResultCode;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:34:15
 * desc   :
 */

public class DisabledException extends AuthenticationException {
    public DisabledException(String message) {
        super(ResultCode.FORBIDDEN, message);
    }
}
