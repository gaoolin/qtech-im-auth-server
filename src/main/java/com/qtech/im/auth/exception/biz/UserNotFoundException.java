package com.qtech.im.auth.exception.biz;

import com.qtech.im.auth.common.ResultCode;
import com.qtech.im.auth.exception.authentication.AuthenticationException;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:32:32
 * desc   :
 */

public class UserNotFoundException extends AuthenticationException {
    public UserNotFoundException() {
        super(ResultCode.USER_NOT_FOUND);
    }
    public UserNotFoundException(String message) {
        super(ResultCode.USER_NOT_FOUND, message);
    }
}