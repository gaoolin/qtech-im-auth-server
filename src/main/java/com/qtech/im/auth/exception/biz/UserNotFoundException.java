package com.qtech.im.auth.exception.biz;

import com.qtech.im.auth.exception.authentication.AuthenticationException;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:32:32
 * desc   :
 */

public class UserNotFoundException extends AuthenticationException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
