package com.qtech.im.auth.config.datasource;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/10 10:01:31
 * desc   :  AOP切面类
 */

@Slf4j
@Aspect
@Order(-1)
@Component
@RequiredArgsConstructor
public class DataSourceAspect {

    private final DynamicDataSourceProperties properties;
    private Set<String> available;

    @PostConstruct
    public void init() {
        available = properties.getDynamic().keySet();
    }

    @Around("@annotation(com.qtech.im.auth.config.datasource.DataSource) || @within(com.qtech.im.auth.config.datasource.DataSource)")
    public Object switchDataSource(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = point.getTarget().getClass();

        DataSource dataSource = method.getAnnotation(DataSource.class);
        if (dataSource == null) {
            dataSource = clazz.getAnnotation(DataSource.class);
        }

        String key = dataSource != null ? dataSource.value().name() : properties.getPrimary();  // 使用枚举的 name
        boolean strict = dataSource != null && dataSource.strict();

        if (!available.contains(key)) {
            if (strict) {
                throw new IllegalArgumentException("数据源 [" + key + "] 不存在！");
            } else {
                log.warn(">>>>> 数据源 [{}] 不存在，使用默认数据源 [{}]", key, properties.getPrimary());
                key = properties.getPrimary();
            }
        }

        log.info(">>>>> 切换数据源 => [{}]", key);
        DataSourceContextHolder.set(key);
        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.clear();
        }
    }
}