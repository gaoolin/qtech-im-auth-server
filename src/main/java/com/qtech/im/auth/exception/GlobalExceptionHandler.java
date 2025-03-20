package com.qtech.im.auth.exception;

import com.qtech.im.auth.common.Result;
import com.qtech.im.auth.common.ResultCode;
import com.qtech.im.auth.exception.authentication.*;
import com.qtech.im.auth.exception.biz.BusinessException;
import com.qtech.im.auth.exception.biz.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:32:00
 * desc   :  全局异常处理
 */

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    public Result<?> handleAuthenticationException(AuthenticationException ex) {
        logger.error("Authentication error: ", ex);
        return Result.failure(ResultCode.UNAUTHORIZED.getCode(), ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public Result<?> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        logger.error("Invalid credentials: ", ex);
        return Result.failure(ResultCode.UNAUTHORIZED.getCode(), "Invalid username or password.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Result<?> handleUserNotFoundException(UserNotFoundException ex) {
        logger.error("User not found: ", ex);
        return Result.failure(ResultCode.NOT_FOUND.getCode(), "User not found.");
    }

    @ExceptionHandler(AccountLockedException.class)
    public Result<?> handleAccountLockedException(AccountLockedException ex) {
        logger.error("Account locked: ", ex);
        return Result.failure(ResultCode.FORBIDDEN.getCode(), "Your account is locked.");
    }

    @ExceptionHandler(AccountExpiredException.class)
    public Result<?> handleAccountExpiredException(AccountExpiredException ex) {
        logger.error("Account expired: ", ex);
        return Result.failure(ResultCode.FORBIDDEN.getCode(), "Your account has expired.");
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public Result<?> handleCredentialsExpiredException(CredentialsExpiredException ex) {
        logger.error("Credentials expired: ", ex);
        return Result.failure(ResultCode.FORBIDDEN.getCode(), "Your password has expired.");
    }

    @ExceptionHandler(DisabledException.class)
    public Result<?> handleDisabledException(DisabledException ex) {
        logger.error("Account disabled: ", ex);
        return Result.failure(ResultCode.FORBIDDEN.getCode(), "Your account is disabled.");
    }


    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException ex) {
        return Result.failure(ex.getCode(), ex.getMessage());
    }
}
