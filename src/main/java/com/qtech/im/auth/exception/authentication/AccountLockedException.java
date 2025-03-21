package com.qtech.im.auth.exception.authentication;

import com.qtech.im.auth.common.ResultCode;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:33:09
 * desc   :
 */

public class AccountLockedException extends AuthenticationException {
    public AccountLockedException(String message) {
        super(ResultCode.NO_PERMISSION, message);
    }
}