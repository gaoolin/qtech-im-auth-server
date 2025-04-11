package com.qtech.im.auth.config.datasource;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/10 09:55:59
 * desc   :  数据源上下文持有器
 */

public class DataSourceContextHolder {
    private static final ThreadLocal<String> CONTEXT = new InheritableThreadLocal<>();

    public static void set(String key) {
        CONTEXT.set(key);
    }

    public static String get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}