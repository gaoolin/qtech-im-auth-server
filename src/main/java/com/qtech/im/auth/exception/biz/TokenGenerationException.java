package com.qtech.im.auth.exception.biz;

import com.qtech.im.auth.common.ResultCode;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/21 09:08:21
 * desc   :  令牌生成异常
 */

public class TokenGenerationException extends BusinessException {
    public TokenGenerationException(String message) {
        super(ResultCode.UNAUTHORIZED, message);
    }
}
