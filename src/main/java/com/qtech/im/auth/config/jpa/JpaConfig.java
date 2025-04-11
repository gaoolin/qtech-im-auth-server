package com.qtech.im.auth.config.jpa;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/11 09:05:48
 * desc   :
 */

@Configuration
@EnableTransactionManagement
public class JpaConfig {

    // @Bean
    // public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    //     LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    //     factory.setDataSource(dataSource); // 动态数据源
    //     factory.setPackagesToScan("com.qtech.im.auth.model"); // 设置你的实体类路径
    //     factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    //
    //     // JPA 属性设置（可选）
    //     Map<String, Object> jpaProperties = new HashMap<>();
    //     // jpaProperties.put("hibernate.hbm2ddl.auto", "none");
    //     // jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
    //     jpaProperties.put("hibernate.show_sql", true);
    //     jpaProperties.put("hibernate.format_sql", true);
    //     factory.setJpaPropertyMap(jpaProperties);
    //
    //     return factory;
    // }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
