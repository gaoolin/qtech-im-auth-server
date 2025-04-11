package com.qtech.im.auth.config.datasource;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/10 09:56:51
 * desc   :  数据源配置属性类
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class DynamicDataSourceProperties {
    private String primary;
    private Map<String, DataSourceProperty> dynamic = new HashMap<>();

    @PostConstruct
    public void init() {
        log.info(">>>>> 当前primary为：{}", primary);
        log.info(">>>>> 所有数据源为：{}", dynamic.keySet());
    }

    @Data
    public static class DataSourceProperty {
        private String driverClassName;
        private String jdbcUrl;
        private String username;
        private String password;
        private Map<String, String> hikari = new HashMap<>();
    }
}

