package com.qtech.im.auth.utils;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/02 14:33:54
 * desc   :  数据库中删除标识
 */

public enum DelFlag {
    EXISTS('0'),
    DELETED('1'),
    UNKNOWN('2');

    private final char code;

    DelFlag(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static DelFlag fromCode(char code) {
        for (DelFlag delFlag : DelFlag.values()) {
            if (delFlag.getCode() == code) {
                return delFlag;
            }
        }
        throw new IllegalArgumentException("Unknown delete flag code: " + code);
    }
}
