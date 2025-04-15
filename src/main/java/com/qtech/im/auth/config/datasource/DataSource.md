# 🌐 Spring Boot 动态数据源使用说明文档
本项目自定义实现了基于注解的多数据源动态切换机制，支持通过 `@DataSource` 注解灵活控制方法或类的数据库连接。

---
## 📌 一、项目结构
```text
└── com
    └── qtech
        └── im
            └── auth
                └── config
                    └── datasource
                        ├── DataSource.java               // 数据源实体类
                        ├── DataSource.md                // 配置文档
                        ├── DataSourceAspect.java        // 切面类，数据源切换逻辑
                        ├── DataSourceContextHolder.java // 线程上下文管理数据源
                        ├── DataSourceKey.java           // 数据源 key 枚举类
                        ├── DynamicDataSourceConfig.java // 动态数据源配置
                        ├── DynamicDataSourceProperties.java // 配置属性类
                        └── DynamicRoutingDataSource.java  // 自定义数据源路由

```
# 🧩 功能简介
- 支持多数据源配置与初始化。
- 通过注解方式进行数据源切换。
- 使用 AOP + ThreadLocal 实现线程级别的数据源隔离。
- 支持数据源严格校验（strict）。
- 支持 JPA / MyBatis 等数据库访问框架。
本配置实现了基于 @ConfigurationProperties 和自定义的路由数据源 DynamicRoutingDataSource 实现 多数据源动态切换，适用于 JPA 和 MyBatis 等持久层框架。通过切面、上下文和 @TargetDataSource 注解方式，可以灵活地切换数据源，满足复杂业务场景。
---
# 🔧 配置文件说明
1. YAML 配置文件（application.yml）
```yaml
spring:
  datasource:
    dynamic:
      primary: primary
      dynamic:
        primary:
          driverClassName: oracle.jdbc.OracleDriver
          url: jdbc:oracle:thin:@//10.170.6.140:1521/imTest
          username: IMWEB
          password: NzmNXWDwxm2
          hikari:
            maximum-pool-size: 10
        secondary:
          driverClassName: oracle.jdbc.OracleDriver
          url: jdbc:oracle:thin:@//10.170.6.144:1521/qtechIm
          username: imBiz
          password: M5F5JkfnQ
          hikari:
            maximum-pool-size: 10

```
在 spring.datasource.dynamic 中，配置了 primary 数据源作为默认数据源，dynamic 下包含了多个数据源的配置（例如 primary 和 secondary）。

# 🧾 代码文件说明
1. 切面类：DataSourceAspect
```java
@Aspect
@Component
@Order(-1)
@Slf4j
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

        String key = dataSource != null ? dataSource.value().name() : properties.getPrimary();
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

```

2. 上下文持有器：DataSourceContextHolder
```java
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
```

3. 动态数据源配置类：DynamicDataSourceConfig
```java
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

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();

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

        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(targetDataSources.get(properties.getPrimary()));
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.afterPropertiesSet();

        log.info(">>>>> 默认数据源为: {}", properties.getPrimary());
        return routingDataSource;
    }

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

```

4. 配置属性类：DynamicDataSourceProperties
```java
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
```

5. 数据源路由类：DynamicRoutingDataSource
```java
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.get();
    }
}
```

6. 数据源枚举类：DataSourceKey
```java
@Getter
public enum DataSourceKey {
    PRIMARY("primary"),
    SECONDARY("secondary");

    private final String key;

    DataSourceKey(String key) {
        this.key = key;
    }
}
```

7. 配置文件示例（application.yml）
```java
@Service
public class DemoService {

    @DataSource(value = DataSourceKey.SECONDARY)
    public void queryFromSecondary() {
        // 查询逻辑，这里使用 secondary 数据源
    }

    public void queryFromPrimary() {
        // 默认使用 primary 数据源
    }
}
```

---

# 使用方式
1. 自动切换数据源：
```java
@Service
public class DemoService {

    @DataSource(value = DataSourceKey.SECONDARY)
    public void queryFromSecondary() {
        // 查询逻辑，这里使用 secondary 数据源
    }

    public void queryFromPrimary() {
        // 默认使用 primary 数据源
    }
}
```
2. 手动切换数据源：
使用 DataSourceContextHolder 类进行手动切换。
```java
public class DemoService {
    public void queryFromPrimary() {
        DataSourceContextHolder.setDataSource("secondary");
        // 执行数据库操作
        DataSourceContextHolder.clear();
    }
}
```

---

# 注意事项
建议所有方法均显式标注数据源，避免因 AOP 失效导致异常。

strict=true 模式下，一旦数据源未配置将直接抛出异常。

多线程异步任务中需注意 ThreadLocal 传递问题，可考虑使用 InheritableThreadLocal 或手动传递上下文。

如需扩展更多数据源，只需在配置文件中增加数据源定义，并在枚举中添加对应的 DataSourceKey 即可。

---

# ✅ 常见问题排查
问题描述	原因分析	解决方案
默认数据源为空	YAML 配置字段错误	确保 spring.datasource.dynamic.primary 正确配置
数据源无法连接	数据库连接配置错误	检查数据库地址、用户名、密码等配置
注解切换失败	未正确应用切面（AOP）	确保 @EnableAspectJAutoProxy 生效，并正确使用 @TargetDataSource 注解
# 🧰 相关依赖
```
<dependency>
  <groupId>com.zaxxer</groupId>
  <artifactId>HikariCP</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-configuration-processor</artifactId>
  <optional>true</optional>
</dependency>
```