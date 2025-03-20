package com.qtech.im.auth.exception.biz;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:55:24
 * desc   :
 */

import com.qtech.im.auth.common.ResultCode;

/**
 * 状态不允许操作异常
 */
public class OperationNotAllowedException extends BusinessException {
    public OperationNotAllowedException(String message) {
        super(ResultCode.OPERATION_NOT_ALLOWED, message);
    }
}