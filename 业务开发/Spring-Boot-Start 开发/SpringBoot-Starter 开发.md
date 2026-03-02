# Spring Boot Starter 开发指南：v2 vs v3

本文档总结了 Spring Boot 2 和 Spring Boot 3 在开发 Starter 模块时的核心差异，帮助开发者快速适配新版本。

## 1. 核心差异概览

| 特性             | Spring Boot 2.x (旧版)         | Spring Boot 3.x (新版)                                                                   | 迁移/开发建议                                                    |
| :------------- | :--------------------------- | :------------------------------------------------------------------------------------- | :--------------------------------------------------------- |
| **JDK 版本**     | Java 8+                      | **Java 17+** (强制)                                                                      | 确保开发环境和部署环境均为 JDK 17 及以上。                                  |
| **Java EE 规范** | `javax.*` (Java EE)          | **`jakarta.*`** (Jakarta EE)                                                           | 涉及 Web、JPA、Validation 等组件时，需全局替换包名为 `jakarta.*`。           |
| **自动配置注册**     | `META-INF/spring.factories`  | **`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`** | 2.7+ 引入新机制，3.x 继续兼容旧版但推荐使用新版文件。                            |
| **核心注解**       | `@Configuration`             | **`@AutoConfiguration`**                                                               | 3.x 推荐使用 `@AutoConfiguration` 替代 `@Configuration` 用于自动配置类。 |
| **配置排序**       | `@AutoConfigureAfter/Before` | `@AutoConfiguration(after=..., before=...)`                                            | 新注解内置了排序属性，使用更简洁。                                          |
| **构建工具**       | Maven 3.5+                   | Maven 3.6.3+                                                                           | 升级 Maven 版本以支持新特性。                                         |

## 2. 自动配置机制详解

### 2.1 文件位置与格式变化

**Spring Boot 2.x (传统方式)**:
- **文件路径**: `src/main/resources/META-INF/spring.factories`
- **格式**: Key-Value 属性文件
```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.example.project.MyAutoConfiguration,\
com.example.project.AnotherAutoConfiguration
```

**Spring Boot 3.x (推荐方式)**:
- **文件路径**: `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- **格式**: 纯文本列表，每行一个全限定类名，支持 `#` 注释
```text
com.example.project.MyAutoConfiguration
com.example.project.AnotherAutoConfiguration
```

### 2.2 注解升级：@AutoConfiguration

在 Spring Boot 2.7 引入并在 3.x 中成为标准的 `@AutoConfiguration` 注解，专门用于标记自动配置类。

**优势**:
1.  **语义清晰**: 明确区分“自动配置”和“普通配置”。
2.  **内置排序**: 不再需要额外的 `@AutoConfigureAfter` 注解。
3.  **代理模式**: `proxyBeanMethods` 默认为 `true`，与 `@Configuration` 一致，但建议在不依赖 Bean 间方法调用时设置为 `false` 以提升启动性能。

**代码对比**:

*Old (v2.x)*:
```java
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MyStarterConfig { ... }
```

*New (v3.x)*:
```java
@AutoConfiguration(after = DataSourceAutoConfiguration.class)
public class MyStarterConfig { ... }
```

## 3. 常见迁移痛点 (Jakarta EE)

Spring Boot 3 基于 Jakarta EE 9/10，这是从 v2 升级到 v3 最大的破坏性变更。

如果你的 Starter 依赖以下规范，必须修改 `import` 语句：

| 组件 | 旧包名 (v2) | 新包名 (v3) |
| :--- | :--- | :--- |
| **Servlet (Web)** | `javax.servlet.*` | `jakarta.servlet.*` |
| **JPA (Hibernate)** | `javax.persistence.*` | `jakarta.persistence.*` |
| **Validation** | `javax.validation.*` | `jakarta.validation.*` |
| **Annotation** | `javax.annotation.*` | `jakarta.annotation.*` |
| **JAX-RS** | `javax.ws.rs.*` | `jakarta.ws.rs.*` |

**注意**: JDK 自带的 `javax.sql.*`, `javax.crypto.*` 等包名**保持不变**。

## 4. Starter 开发最佳实践 (v3)

1.  **命名规范**:
    - 官方 Starter: `spring-boot-starter-{name}`
    - 第三方 Starter: `{name}-spring-boot-starter` (如 `minio-spring-boot-starter`)

2.  **配置提示**:
    确保引入 `spring-boot-configuration-processor` 依赖，这样使用者在 `application.yml` 中编写配置时会有代码提示。
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>
    ```

3.  **条件装配 (Conditional)**:
    Starter 的核心是“按需加载”。务必使用条件注解防止 Bean 冲突或在缺少依赖时报错。
    - `@ConditionalOnProperty(prefix = "minio", name = "enabled", havingValue = "true")`
    - `@ConditionalOnMissingBean`: 仅在用户没有自定义 Bean 时才加载你的默认 Bean。
    - `@ConditionalOnClass`: 仅在类路径下存在特定类时加载。

4.  **元数据文件**:
    在 `META-INF/` 下生成 `spring-autoconfigure-metadata.properties` (通常由插件自动处理)，有助于 Spring Boot 在加载类之前过滤自动配置，提升启动速度。
