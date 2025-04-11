# 🌐 Spring Boot 动态数据源切换配置文档

## 📌 一、项目结构
```text
└── com
    └── qtech
        └── im
            └── auth
                └── config
                    ├── datasource
                    │   ├── DataSource.java               // 数据源实体类
                    │   ├── DataSource.md                // 配置文档
                    │   ├── DataSourceAspect.java        // 切面类，数据源切换逻辑
                    │   ├── DataSourceContextHolder.java // 线程上下文管理数据源
                    │   ├── DataSourceKey.java           // 数据源 key 枚举类
                    │   ├── DynamicDataSourceConfig.java // 动态数据源配置
                    │   ├── DynamicDataSourceProperties.java // 配置属性类
                    │   └── DynamicRoutingDataSource.java  // 自定义数据源路由

```
# 🧩 二、功能简介
本配置实现了基于 @ConfigurationProperties 和自定义的路由数据源 DynamicRoutingDataSource 实现 多数据源动态切换，适用于 JPA 和 MyBatis 等持久层框架。通过切面、上下文和 @TargetDataSource 注解方式，可以灵活地切换数据源，满足复杂业务场景。
# 🔧 三、配置文件说明
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

# 🧾 四、代码文件说明
1. DynamicDataSourceProperties.java
```java
@Data
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class DynamicDataSourceProperties {
    private String primary;
    private Map<String, DataSourceProperty> dynamic = new HashMap<>();

    @Data
    public static class DataSourceProperty {
        private String driverClassName;
        private String url;
        private String username;
        private String password;
        private Map<String, String> hikari = new HashMap<>();
    }
}

```
* 用于绑定 YAML 配置文件中的数据源信息。

* primary 字段标识默认的数据源，dynamic 存储其他动态数据源的配置。
2. DynamicDataSourceConfig.java
```java
@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceConfig {

    @Autowired
    private DynamicDataSourceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();

        properties.getDynamic().forEach((name, prop) -> {
            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(prop.getUrl());
            ds.setUsername(prop.getUsername());
            ds.setPassword(prop.getPassword());
            ds.setDriverClassName(prop.getDriverClassName());

            if (prop.getHikari() != null) {
                prop.getHikari().forEach(ds::addDataSourceProperty);
            }

            targetDataSources.put(name, ds);
            log.info("数据源 [{}] 注册成功: {}", name, prop.getUrl());
        });

        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(
            targetDataSources.get(properties.getPrimary())
        );
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.afterPropertiesSet();

        log.info("默认数据源为: {}", properties.getPrimary());
        return routingDataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}

```
* 配置了动态数据源路由，将 primary 数据源设为默认。

* 使用 HikariCP 连接池，支持多数据源配置。

* 配置了事务管理器，确保支持 @Transactional 注解。

3. DynamicRoutingDataSource.java
```java
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceKey();
    }
}

```
* 继承 AbstractRoutingDataSource，实现数据源路由选择。

* 根据当前线程的上下文，选择合适的数据源。
4. DataSourceContextHolder.java
```java
public class DataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSource(String dataSourceKey) {
        contextHolder.set(dataSourceKey);
    }

    public static String getDataSourceKey() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}

```
* 使用 ThreadLocal 来管理当前线程的数据源上下文，实现线程安全。

* 提供 setDataSource、getDataSourceKey 和 clear 方法，方便数据源切换。
5. DataSourceAspect.java
```java
@Aspect
@Component
public class DataSourceAspect {

    @Before("@annotation(targetDataSource)")
    public void changeDataSource(TargetDataSource targetDataSource) {
        String dataSourceKey = targetDataSource.value();
        DataSourceContextHolder.setDataSource(dataSourceKey);
    }

    @After("@annotation(targetDataSource)")
    public void restoreDataSource(TargetDataSource targetDataSource) {
        DataSourceContextHolder.clear();
    }
}

```
* 利用切面 AOP 在方法执行前后切换数据源。

* 通过 @TargetDataSource 注解标注需要切换数据源的方法。
6. TargetDataSource.java
```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetDataSource {

    String value(); // 数据源名称
}

```
* 自定义注解，标记需要切换的数据源。
# 🚦 五、数据源切换使用说明
方法级数据源切换：

1. 使用 @TargetDataSource("secondary") 注解标记需要使用 secondary 数据源的方法。
```java
@TargetDataSource("secondary")
public void someMethod() {
    // 此方法会使用 secondary 数据源
}

```
2. 手动切换数据源：

使用 DataSourceContextHolder 类进行手动切换。
```java
DataSourceContextHolder.setDataSource("secondary");
// 执行数据库操作
DataSourceContextHolder.clear();
```
# ✅ 六、常见问题排查
问题描述	原因分析	解决方案
默认数据源为空	YAML 配置字段错误	确保 spring.datasource.dynamic.primary 正确配置
数据源无法连接	数据库连接配置错误	检查数据库地址、用户名、密码等配置
注解切换失败	未正确应用切面（AOP）	确保 @EnableAspectJAutoProxy 生效，并正确使用 @TargetDataSource 注解
# 🧰 七、相关依赖
```xml
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