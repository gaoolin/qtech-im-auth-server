package com.qtech.im.auth.utils;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 11:49:52
 * desc   :  菜单类型（M目录 C菜单 F按钮）
 */


public enum MenuType {
    M("M"),
    C("C"),
    F("F"),
    UNKNOWN("9");

    private char code;

    MenuType(String code) {
        this.code = code.charAt(0);
    }

    public char getCode() {
        return code;
    }

    public static MenuType fromCode(char code) {
        for (MenuType menuType : MenuType.values()) {
            if (menuType.getCode() == code) {
                return menuType;
            }
        }
        return null;
   }
}
