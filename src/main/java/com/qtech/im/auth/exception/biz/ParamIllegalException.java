package com.qtech.im.auth.exception.biz;

import com.qtech.im.auth.common.ResultCode;
/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:53:42
 * desc   :
 */

/**
 * 参数非法异常
 */
public class ParamIllegalException extends BusinessException {
    public ParamIllegalException(String message) {
        super(ResultCode.PARAM_ILLEGAL, message);
    }
}