package com.qtech.im.auth.exception.biz;

import com.qtech.im.auth.common.ResultCode;
/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:54:22
 * desc   :
 */

/**
 * 没有权限操作异常
 */
public class NoPermissionException extends BusinessException {
    public NoPermissionException(String message) {
        super(ResultCode.NO_PERMISSION, message);
    }
}
