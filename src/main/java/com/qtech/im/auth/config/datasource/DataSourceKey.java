package com.qtech.im.auth.config.datasource;

import lombok.Getter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/10 09:56:26
 * desc   :  数据源枚举
 */
@Getter
public enum DataSourceKey {
    PRIMARY("primary"),
    SECONDARY("secondary");

    private final String key;

    DataSourceKey(String key) {
        this.key = key;
    }
}
