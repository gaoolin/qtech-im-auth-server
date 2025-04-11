package com.qtech.im.auth.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/10 09:58:26
 * desc   :  配置类
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceConfig {

    @Autowired
    private DynamicDataSourceProperties properties;

    @PostConstruct
    public void init() {
        log.info(">>>>> 加载的数据源配置: {}", properties.getDynamic());
    }

    /**
     * 注册动态数据源路由器，作为主数据源使用（JPA、MyBatis、事务都基于它）
     */
    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();

        // 初始化每个数据源并放入目标 Map
        properties.getDynamic().forEach((name, prop) -> {
            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(prop.getJdbcUrl());
            ds.setUsername(prop.getUsername());
            ds.setPassword(prop.getPassword());
            ds.setDriverClassName(prop.getDriverClassName());

            if (prop.getHikari() != null) {
                prop.getHikari().forEach(ds::addDataSourceProperty);
            }

            targetDataSources.put(name, ds);
            log.info(">>>>> 数据源 [{}] 注册成功: {}", name, prop.getJdbcUrl());
        });

        // 设置动态数据源
        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(targetDataSources.get(properties.getPrimary()));
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.afterPropertiesSet();

        log.info(">>>>> 默认数据源为: {}", properties.getPrimary());
        return routingDataSource;
    }

    /**
     * 注册事务管理器（支持 @Transactional）
     * 名称	类型	适用场景	数据访问方式
     * DataSourceTransactionManager	JDBC事务管理器	MyBatis、JDBC	基于 DataSource 直接操作数据库
     * JpaTransactionManager（继承自 PlatformTransactionManager）	JPA事务管理器	JPA / Hibernate	基于 EntityManagerFactory
     * <p>
     * 使用JPA的ORM框架，需要使用JpaTransactionManager，它继承自PlatformTransactionManager，而不是DataSourceTransactionManager。
     */
    // @Bean
    // public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    //     return new DataSourceTransactionManager(dataSource);
    // }
    @Bean(name = "primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.dynamic.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.dynamic.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
