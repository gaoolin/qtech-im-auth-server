package com.qtech.im.auth.config.datasource;

import java.lang.annotation.*;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/10 09:55:21
 * desc   :  注解定义
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DataSourceKey value();       // 枚举替代原字符串
    boolean strict() default false; // 若开启 strict=true 且找不到数据源将抛异常
}