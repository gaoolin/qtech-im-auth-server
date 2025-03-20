package com.qtech.im.auth.exception.authentication;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:33:46
 * desc   :
 */

public class CredentialsExpiredException extends AuthenticationException {
    public CredentialsExpiredException(String message) {
        super(message);
    }

    public CredentialsExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
