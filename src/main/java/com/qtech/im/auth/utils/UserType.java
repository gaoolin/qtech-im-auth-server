package com.qtech.im.auth.utils;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/02 16:40:08
 * desc   :  用户类型枚举类
 */

public enum UserType {
    SYSTEM_USER('0'),
    ORDINARY_USER('1'),
    UNKNOWN('2');

    private final char code;

    UserType(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static UserType fromCode(char code) {
        for (UserType userType : UserType.values()) {
            if (userType.getCode() == code) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Unknown user type code: " + code);
    }
}
