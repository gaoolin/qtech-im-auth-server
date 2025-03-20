package com.qtech.im.auth.common;

import lombok.Getter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 08:41:54
 * desc   :
 */

@Getter
public enum ResultCode {
    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未认证或Token失效"),
    FORBIDDEN(403, "没有权限"),
    NOT_FOUND(404, "资源不存在"),
    SERVER_ERROR(500, "服务器内部错误"),
    VALIDATE_FAILED(422, "参数校验失败"),
    DUPLICATE_KEY(409, "数据已存在"),

    PARAM_ILLEGAL(400, "请求参数非法"),
    AUTH_EXPIRED(401, "认证已过期"),
    NO_PERMISSION(403, "没有权限"),
    USER_NOT_FOUND(404, "用户不存在"),
    OPERATION_NOT_ALLOWED(405, "当前状态不允许该操作"),

    BUSINESS_ERROR(600, "业务异常"),
    CUSTOM_ERROR(999, "自定义错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}