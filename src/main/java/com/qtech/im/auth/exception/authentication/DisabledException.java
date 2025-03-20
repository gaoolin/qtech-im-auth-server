package com.qtech.im.auth.exception.authentication;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:34:15
 * desc   :
 */

public class DisabledException extends AuthenticationException {
    public DisabledException(String message) {
        super(message);
    }

    public DisabledException(String message, Throwable cause) {
        super(message, cause);
    }
}
