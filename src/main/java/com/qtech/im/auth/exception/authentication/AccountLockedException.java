package com.qtech.im.auth.exception.authentication;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:33:09
 * desc   :
 */

public class AccountLockedException extends AuthenticationException {
    public AccountLockedException(String message) {
        super(message);
    }

    public AccountLockedException(String message, Throwable cause) {
        super(message, cause);
    }
}