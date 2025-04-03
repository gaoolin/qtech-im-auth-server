package com.qtech.im.auth.utils;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/02 14:32:45
 * desc   :  数据库中的status字段的枚举类
 */

public enum Status {
    ACTIVE('0'),
    INACTIVE('1'),
    PENDING('2'),
    UNKNOWN('3');
    private static final String ACTIVE_DESCRIPTION = "启用";
    private static final String INACTIVE_DESCRIPTION = "禁用";
    private static final String PENDING_DESCRIPTION = "待审核";


    private final char code;

    Status(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static Status fromCode(char code) {
        for (Status status : Status.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}


