# MyBatis 操作多数据库
## 实现方案

### 1. 依赖

```xml
<!-- MySQL -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>

<!-- PostgreSQL -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>

<!-- MyBatis Plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.5</version>
</dependency>
```

---

### 2. application.yml

```yaml
spring:
  datasource:
    # MySQL 数据源
    mysql:
      driver-class-name: com.mysql.cj.jdbc.Driver
      jdbc-url: jdbc:mysql://localhost:3306/mysql_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
      username: root
      password: yourpassword
      hikari:
        pool-name: HikariPool-MySQL
        minimum-idle: 5
        maximum-pool-size: 20

    # PostgreSQL 数据源
    postgresql:
      driver-class-name: org.postgresql.Driver
      jdbc-url: jdbc:postgresql://localhost:5432/pg_db
      username: postgres
      password: yourpassword
      hikari:
        pool-name: HikariPool-PostgreSQL
        minimum-idle: 5
        maximum-pool-size: 20

# MyBatis-Plus 公共配置
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    jdbc-type-for-null: null
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
    banner: false
```

---

### 3. 项目结构

```
src/main/java/com/zkxt/
├── config/
│   ├── MysqlDataSourceConfig.java       # MySQL 数据源配置
│   └── PostgresqlDataSourceConfig.java  # PostgreSQL 数据源配置
├── mapper/
│   ├── mysql/                           # MySQL 的 Mapper 接口
│   │   └── UserMapper.java
│   └── postgresql/                      # PostgreSQL 的 Mapper 接口
│       └── SysRegionMapper.java
└── resources/
    └── mapper/
        ├── mysql/                       # MySQL 的 XML
        │   └── UserMapper.xml
        └── postgresql/                  # PostgreSQL 的 XML
            └── SysRegionMapper.xml
```

---

### 4. MySQL 数据源配置

```java
@Configuration
@MapperScan(
    basePackages = "com.zkxt.mapper.mysql",        // 只扫描 mysql 包下的 Mapper
    sqlSessionFactoryRef = "mysqlSqlSessionFactory"
)
public class MysqlDataSourceConfig {

    // 读取 mysql 数据源配置，@Primary 表示默认数据源
    @Primary
    @Bean("mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean("mysqlSqlSessionFactory")
    public SqlSessionFactory mysqlSqlSessionFactory(
            @Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {

        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        // 指定 XML 路径
        factory.setMapperLocations(
            new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/mysql/**/*.xml")
        );

        // 实体类别名包
        factory.setTypeAliasesPackage("com.zkxt.entity.mysql");

        // MySQL 的 MyBatis-Plus 全局配置
        GlobalConfig globalConfig = GlobalConfigUtils.defaults();
        globalConfig.getDbConfig().setIdType(IdType.AUTO);
        globalConfig.getDbConfig().setLogicDeleteField("deleted");
        globalConfig.getDbConfig().setLogicDeleteValue("1");
        globalConfig.getDbConfig().setLogicNotDeleteValue("0");
        factory.setGlobalConfig(globalConfig);

        // 公共 MyBatis 配置
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        factory.setConfiguration(configuration);

        return factory.getObject();
    }

    @Primary
    @Bean("mysqlTransactionManager")
    public DataSourceTransactionManager mysqlTransactionManager(
            @Qualifier("mysqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean("mysqlSqlSessionTemplate")
    public SqlSessionTemplate mysqlSqlSessionTemplate(
            @Qualifier("mysqlSqlSessionFactory") SqlSessionFactory factory) {
        return new SqlSessionTemplate(factory);
    }
}
```

---

### 5. PostgreSQL 数据源配置

```java
@Configuration
@MapperScan(
    basePackages = "com.zkxt.mapper.postgresql",   // 只扫描 postgresql 包下的 Mapper
    sqlSessionFactoryRef = "postgresqlSqlSessionFactory"
)
public class PostgresqlDataSourceConfig {

    @Bean("postgresqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.postgresql")
    public DataSource postgresqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("postgresqlSqlSessionFactory")
    public SqlSessionFactory postgresqlSqlSessionFactory(
            @Qualifier("postgresqlDataSource") DataSource dataSource) throws Exception {

        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        factory.setMapperLocations(
            new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/postgresql/**/*.xml")
        );

        factory.setTypeAliasesPackage("com.zkxt.entity.postgresql");

        // 注册 geometry TypeHandler
        factory.setTypeHandlers(new GeometryTypeHandler());

        GlobalConfig globalConfig = GlobalConfigUtils.defaults();
        globalConfig.getDbConfig().setIdType(IdType.AUTO);
        globalConfig.getDbConfig().setLogicDeleteField("deleted");
        globalConfig.getDbConfig().setLogicDeleteValue("1");
        globalConfig.getDbConfig().setLogicNotDeleteValue("0");
        factory.setGlobalConfig(globalConfig);

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        factory.setConfiguration(configuration);

        return factory.getObject();
    }

    @Bean("postgresqlTransactionManager")
    public DataSourceTransactionManager postgresqlTransactionManager(
            @Qualifier("postgresqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("postgresqlSqlSessionTemplate")
    public SqlSessionTemplate postgresqlSqlSessionTemplate(
            @Qualifier("postgresqlSqlSessionFactory") SqlSessionFactory factory) {
        return new SqlSessionTemplate(factory);
    }
}
```

---

### 6. 使用时指定事务管理器

```java
@Service
public class UserService {

    // 默认使用 MySQL（@Primary）
    @Transactional(transactionManager = "mysqlTransactionManager")
    public void mysqlOperation() {
        // 操作 MySQL
    }
}

@Service
public class RegionService {

    // 明确指定 PostgreSQL 事务管理器
    @Transactional(transactionManager = "postgresqlTransactionManager")
    public void postgresqlOperation() {
        // 操作 PostgreSQL
    }
}
```

---

## 注意事项

**跨库事务问题**是多数据源最大的坑：

```
单个 @Transactional 只能管一个数据库
如果一个方法同时写 MySQL 和 PostgreSQL，其中一个失败，另一个不会回滚
```

解决方式：

|方案|说明|复杂度|
|---|---|---|
|避免跨库事务|业务上隔离，不在同一方法操作两个库|低|
|手动补偿|失败后手动回滚另一个库|中|
|Seata 分布式事务|引入 Seata 框架统一管理|高|

---

## 关键点总结

```
多数据源核心三要素，每个数据库都需要独立配置：

DataSource          →  连接哪个数据库
SqlSessionFactory   →  扫描哪些 Mapper 和 XML
TransactionManager  →  管理哪个库的事务

通过 @MapperScan 的 basePackages 隔离不同数据库的 Mapper
通过 @Qualifier 在注入时指定使用哪个 Bean
通过 @Primary 指定默认数据源（不加 @Qualifier 时使用）
```