# DataX 数据同步工具

## 1. DataX 是什么

DataX 是阿里开源的离线数据同步框架，主要用于在不同数据源之间做批量数据迁移。

常见场景：

- MySQL -> MySQL：库表迁移、分库分表汇总、测试环境数据同步。
- MySQL -> HDFS/Hive：业务库数据进入数仓。
- HDFS/Hive -> MySQL：离线计算结果回流业务库。
- MySQL -> Oracle、PostgreSQL、SQL Server、ClickHouse 等异构数据库同步。
- TXT/CSV 文件 -> 数据库，或数据库 -> TXT/CSV 文件。

DataX 的定位是“离线批量同步”，不适合替代 Canal、Flink CDC 这类实时增量同步工具。

---

## 2. 核心架构

DataX 作业由三部分组成：

| 组件 | 作用 |
| --- | --- |
| `Reader` | 从源端读取数据，例如 `mysqlreader`、`hdfsreader`、`txtfilereader` |
| `Framework` | DataX 框架层，负责切分任务、调度、限速、错误控制、插件加载 |
| `Writer` | 向目标端写入数据，例如 `mysqlwriter`、`hdfswriter`、`txtfilewriter` |

一条同步链路可以理解为：

```text
Source -> Reader -> Channel -> Writer -> Target
```

DataX 的配置文件是 JSON，一个 JSON 文件描述一个同步任务。  
实际执行时，DataX 会把 `job` 切分为多个 `task`，再通过多个 `channel` 并发执行。

---

## 3. 安装与运行

### 3.1 环境要求

推荐环境：

- JDK 8+
- Python
- Linux/macOS/Windows 均可运行，但生产环境通常部署在 Linux。

注意：DataX 的一些历史版本脚本对 Python 2 更友好。如果执行 `datax.py` 时出现 Python 语法兼容问题，需要改用 Python 2，或者使用支持 Python 3 的社区修复版本。

### 3.2 下载

官方仓库：

- GitHub：<https://github.com/alibaba/DataX>
- 使用说明：<https://github.com/alibaba/DataX/blob/master/userGuid.md>

常见安装方式：

```bash
wget http://datax-opensource.oss-cn-hangzhou.aliyuncs.com/datax.tar.gz
tar -zxvf datax.tar.gz
cd datax
```

目录结构大致如下：

```text
datax/
├── bin/
│   └── datax.py            # 启动脚本
├── conf/
├── job/                    # 可以存放 job json
├── plugin/
│   ├── reader/             # Reader 插件
│   └── writer/             # Writer 插件
└── log/
```

### 3.3 自检

DataX 自带 stream reader/writer，可用于验证安装是否成功。

```bash
python bin/datax.py job/job.json
```

如果能看到任务启动、读写速度、任务结束统计，说明 DataX 基本可用。

---

## 4. Job 配置结构

一个 DataX 任务的基本结构：

```json
{
  "job": {
    "setting": {
      "speed": {
        "channel": 3
      },
      "errorLimit": {
        "record": 0,
        "percentage": 0.02
      }
    },
    "content": [
      {
        "reader": {
          "name": "mysqlreader",
          "parameter": {}
        },
        "writer": {
          "name": "mysqlwriter",
          "parameter": {}
        }
      }
    ]
  }
}
```

关键配置：

| 配置 | 说明 |
| --- | --- |
| `job.setting.speed.channel` | 并发通道数，越大并发越高，但会增加源库和目标库压力 |
| `job.setting.speed.byte` | 按字节限速，避免把数据库或网络打满 |
| `job.setting.speed.record` | 按记录数限速 |
| `job.setting.errorLimit.record` | 最多允许多少条脏数据 |
| `job.setting.errorLimit.percentage` | 最多允许多大比例的脏数据 |
| `reader.name` | 源端插件名称 |
| `reader.parameter` | 源端读取参数 |
| `writer.name` | 目标端插件名称 |
| `writer.parameter` | 目标端写入参数 |

---

## 5. MySQL -> MySQL 示例

假设需要把 `source_db.user` 同步到 `target_db.user_backup`。

### 5.1 建表

源表：

```sql
CREATE TABLE user (
  id BIGINT PRIMARY KEY,
  username VARCHAR(64),
  age INT,
  create_time DATETIME
);
```

目标表：

```sql
CREATE TABLE user_backup (
  id BIGINT PRIMARY KEY,
  username VARCHAR(64),
  age INT,
  create_time DATETIME
);
```

### 5.2 编写 job 文件

文件：`job/mysql_to_mysql.json`

```json
{
  "job": {
    "setting": {
      "speed": {
        "channel": 3
      },
      "errorLimit": {
        "record": 0,
        "percentage": 0.02
      }
    },
    "content": [
      {
        "reader": {
          "name": "mysqlreader",
          "parameter": {
            "username": "root",
            "password": "123456",
            "column": [
              "id",
              "username",
              "age",
              "create_time"
            ],
            "splitPk": "id",
            "where": "id > 0",
            "connection": [
              {
                "jdbcUrl": [
                  "jdbc:mysql://127.0.0.1:3306/source_db?useUnicode=true&characterEncoding=utf8&useSSL=false"
                ],
                "table": [
                  "user"
                ]
              }
            ]
          }
        },
        "writer": {
          "name": "mysqlwriter",
          "parameter": {
            "username": "root",
            "password": "123456",
            "column": [
              "id",
              "username",
              "age",
              "create_time"
            ],
            "writeMode": "replace",
            "batchSize": 1024,
            "connection": [
              {
                "jdbcUrl": "jdbc:mysql://127.0.0.1:3306/target_db?useUnicode=true&characterEncoding=utf8&useSSL=false",
                "table": [
                  "user_backup"
                ]
              }
            ]
          }
        }
      }
    ]
  }
}
```

### 5.3 执行任务

```bash
python bin/datax.py job/mysql_to_mysql.json
```

执行完成后重点看日志末尾：

```text
任务启动时刻
任务结束时刻
任务总计耗时
任务平均流量
记录写入速度
读出记录总数
读写失败总数
```

`读写失败总数` 为 0，通常说明本次同步没有脏数据。

---

## 6. MySQLReader 常用参数

官方文档：<https://github.com/alibaba/DataX/blob/master/mysqlreader/doc/mysqlreader.md>

| 参数 | 是否必填 | 说明 |
| --- | --- | --- |
| `username` | 是 | 源库用户名 |
| `password` | 是 | 源库密码 |
| `column` | 是 | 读取列。可以写具体列名，不建议直接用 `*` |
| `connection.jdbcUrl` | 是 | JDBC 连接地址 |
| `connection.table` | 是 | 读取的表名 |
| `where` | 否 | 过滤条件，不需要写 `where` 关键字 |
| `splitPk` | 否 | 分片主键，用于并发切分读取 |
| `querySql` | 否 | 自定义 SQL。配置后会优先于 `column`、`table`、`where` |
| `fetchSize` | 否 | 每次从数据库拉取的数据条数 |

### 6.1 column

推荐明确写列名：

```json
"column": ["id", "username", "age", "create_time"]
```

不建议：

```json
"column": ["*"]
```

原因：源表新增字段、字段顺序变化时，可能导致目标端写入错位。

### 6.2 where

用于增量或局部同步：

```json
"where": "create_time >= '2026-01-01 00:00:00'"
```

DataX 本身不维护增量位点。如果要做每天定时同步，需要外部调度系统维护时间条件或主键范围。

### 6.3 splitPk

`splitPk` 用于把源表按主键范围切分成多个任务，提高读取并发。

```json
"splitPk": "id"
```

适合：

- 数字类型主键。
- 数据分布较均匀。
- 大表批量同步。

不适合：

- 字符串主键。
- 主键极度倾斜。
- 小表同步，没必要切分。

### 6.4 querySql

用于复杂 SQL：

```json
"querySql": [
  "select id, username, age, create_time from user where age >= 18"
]
```

注意：使用 `querySql` 后，DataX 通常无法再基于 `splitPk` 自动切分，需要自己评估性能。

---

## 7. MySQLWriter 常用参数

官方文档：<https://github.com/alibaba/DataX/blob/master/mysqlwriter/doc/mysqlwriter.md>

| 参数 | 是否必填 | 说明 |
| --- | --- | --- |
| `username` | 是 | 目标库用户名 |
| `password` | 是 | 目标库密码 |
| `column` | 是 | 写入列，顺序必须与 Reader 输出列一致 |
| `connection.jdbcUrl` | 是 | JDBC 连接地址 |
| `connection.table` | 是 | 目标表名 |
| `writeMode` | 否 | 写入模式，常见值：`insert`、`replace`、`update` |
| `batchSize` | 否 | 批量写入条数 |
| `preSql` | 否 | 写入前执行的 SQL |
| `postSql` | 否 | 写入后执行的 SQL |
| `session` | 否 | 建立连接后执行的 session 级 SQL |

### 7.1 writeMode

常见模式：

| 模式 | SQL 行为 | 适用场景 |
| --- | --- | --- |
| `insert` | 普通插入 | 目标表为空，或确定不会主键冲突 |
| `replace` | MySQL `REPLACE INTO` | 根据主键/唯一索引覆盖旧数据 |
| `update` | `ON DUPLICATE KEY UPDATE` | 主键冲突时更新部分字段 |

使用建议：

- 全量同步到空表：`insert`
- 回流结果表，允许覆盖：`replace`
- 按主键更新已有数据：`update`

### 7.2 preSql / postSql

全量覆盖时，可以先清空目标表：

```json
"preSql": [
  "truncate table user_backup"
]
```

同步后记录标记：

```json
"postSql": [
  "insert into sync_log(task_name, finish_time) values('user_backup', now())"
]
```

注意：`preSql` 执行失败会导致任务失败；生产环境执行 `truncate` 前要确认目标表是否可以被清空。

---

## 8. TXT/CSV 文件同步示例

### 8.1 MySQL -> CSV

```json
{
  "job": {
    "setting": {
      "speed": {
        "channel": 1
      }
    },
    "content": [
      {
        "reader": {
          "name": "mysqlreader",
          "parameter": {
            "username": "root",
            "password": "123456",
            "column": ["id", "username", "age"],
            "connection": [
              {
                "jdbcUrl": ["jdbc:mysql://127.0.0.1:3306/source_db?useUnicode=true&characterEncoding=utf8&useSSL=false"],
                "table": ["user"]
              }
            ]
          }
        },
        "writer": {
          "name": "txtfilewriter",
          "parameter": {
            "path": "/data/export",
            "fileName": "user",
            "writeMode": "truncate",
            "dateFormat": "yyyy-MM-dd HH:mm:ss",
            "fieldDelimiter": ",",
            "encoding": "UTF-8"
          }
        }
      }
    ]
  }
}
```

### 8.2 CSV -> MySQL

```json
{
  "job": {
    "setting": {
      "speed": {
        "channel": 1
      }
    },
    "content": [
      {
        "reader": {
          "name": "txtfilereader",
          "parameter": {
            "path": ["/data/import/user.csv"],
            "encoding": "UTF-8",
            "column": [
              {
                "index": 0,
                "type": "long"
              },
              {
                "index": 1,
                "type": "string"
              },
              {
                "index": 2,
                "type": "long"
              }
            ],
            "fieldDelimiter": ",",
            "skipHeader": true
          }
        },
        "writer": {
          "name": "mysqlwriter",
          "parameter": {
            "username": "root",
            "password": "123456",
            "column": ["id", "username", "age"],
            "writeMode": "insert",
            "connection": [
              {
                "jdbcUrl": "jdbc:mysql://127.0.0.1:3306/target_db?useUnicode=true&characterEncoding=utf8&useSSL=false",
                "table": ["user"]
              }
            ]
          }
        }
      }
    ]
  }
}
```

---

## 9. 增量同步思路

DataX 本身不保存增量状态，所以增量同步一般由外部系统控制。

常见方式：

### 9.1 按时间字段增量

```json
"where": "update_time >= '${start_time}' and update_time < '${end_time}'"
```

外部调度时替换参数：

```bash
python bin/datax.py job/user_sync.json -p "-Dstart_time='2026-06-01 00:00:00' -Dend_time='2026-06-02 00:00:00'"
```

适合：

- 表有稳定的 `create_time`、`update_time`。
- 每天、每小时批量同步。

风险：

- 源表时间字段不准会漏数据。
- 数据延迟写入时，窗口边界可能漏同步。

解决：

- 同步窗口适当回退，例如每次多同步前 5 分钟。
- 目标端使用 `replace` 或 `update`，避免重复数据。

### 9.2 按自增主键增量

```json
"where": "id > ${last_max_id} and id <= ${current_max_id}"
```

适合：

- 只同步新增数据。
- 主键单调递增。

不适合：

- 需要同步历史更新。
- 数据可能回写旧主键。

### 9.3 全量覆盖

小表或维表可以直接全量同步：

```json
"preSql": ["truncate table dim_user"]
```

优点：逻辑简单，不容易漏数据。  
缺点：数据量大时慢，且同步过程中目标表可能短暂为空。

---

## 10. 性能调优

### 10.1 channel 并不是越大越好

```json
"speed": {
  "channel": 5
}
```

`channel` 增大后会提升并发，但也会增加：

- 源库查询压力。
- 目标库写入压力。
- 网络流量。
- JVM 内存消耗。

调优建议：

1. 小表：`channel = 1`。
2. 中等表：`channel = 3 ~ 5`。
3. 大表：结合 `splitPk`、数据库负载和网络带宽逐步增加。

### 10.2 使用 splitPk

大表同步时，优先选择数字主键作为 `splitPk`：

```json
"splitPk": "id"
```

如果没有 `splitPk`，多个 channel 也不一定能有效并发读取。

### 10.3 控制 batchSize

```json
"batchSize": 1024
```

`batchSize` 太小，写入次数多；太大，单批 SQL 过大，容易占用内存或触发数据库限制。

常用范围：

- `512`
- `1024`
- `2048`
- `5000`

具体要看目标库性能和单行数据大小。

### 10.4 使用限速保护数据库

```json
"speed": {
  "byte": 10485760,
  "channel": 3
}
```

上面表示整体按约 10MB/s 限速。生产环境同步业务库时，建议先限速，观察数据库压力后再调整。

---

## 11. 常见问题与排查

### 11.1 找不到插件

现象：

```text
plugin not found
```

排查：

- `reader.name`、`writer.name` 是否拼写正确。
- `plugin/reader` 或 `plugin/writer` 下是否存在对应插件目录。
- DataX 是否完整解压。

### 11.2 JDBC 驱动问题

现象：

```text
No suitable driver
ClassNotFoundException: com.mysql.jdbc.Driver
```

排查：

- 插件目录下是否有对应 JDBC driver。
- MySQL 8 使用 `com.mysql.cj.jdbc.Driver` 相关驱动。
- JDBC URL 是否正确。

### 11.3 字段数量或类型不匹配

现象：

```text
column size not equal
dirty data
Data truncation
```

排查：

- Reader 的 `column` 顺序是否与 Writer 的 `column` 顺序一致。
- 目标表字段长度是否足够。
- `datetime`、`decimal`、`json` 等类型是否被正确处理。
- 不要随意使用 `column: ["*"]`。

### 11.4 主键冲突

现象：

```text
Duplicate entry for key PRIMARY
```

解决：

- 目标表为空时使用 `insert`。
- 允许覆盖时使用 `replace`。
- 需要更新已有记录时使用 `update`。

### 11.5 同步速度慢

排查顺序：

1. 是否配置了 `splitPk`。
2. `channel` 是否过小。
3. 源库查询是否走索引。
4. 目标库是否有过多索引影响写入。
5. 网络带宽是否成为瓶颈。
6. `batchSize` 是否太小。

---

## 12. 生产使用建议

- 配置文件不要写死生产密码，优先用环境变量、配置中心或脚本注入。
- 源表和目标表字段顺序必须明确，不建议使用 `*`。
- 大表同步前先用小范围 `where` 测试。
- 对业务库同步要限速，避免影响线上业务。
- 增量同步要记录同步窗口，例如 `start_time`、`end_time`、`last_max_id`。
- 目标表涉及覆盖写入时，必须确认主键或唯一索引设计。
- `preSql` 中的 `truncate`、`delete` 要谨慎使用。
- 定时任务建议接入调度系统，例如 DolphinScheduler、Azkaban、Airflow、XXL-JOB。
- 任务日志需要保留，便于排查脏数据和同步失败。

---

## 13. 常用命令

### 13.1 执行 job

```bash
python bin/datax.py job/mysql_to_mysql.json
```

### 13.2 传递动态参数

job 中使用：

```json
"where": "update_time >= '${start_time}' and update_time < '${end_time}'"
```

执行时传参：

```bash
python bin/datax.py job/user_sync.json -p "-Dstart_time='2026-06-01 00:00:00' -Dend_time='2026-06-02 00:00:00'"
```

### 13.3 查看帮助

```bash
python bin/datax.py -h
```

---

## 14. 学习路线

1. 先跑通 streamreader -> streamwriter 自检任务。
2. 再跑 MySQL -> MySQL，全量同步一张小表。
3. 学会配置 `column`、`where`、`splitPk`、`writeMode`。
4. 做一个按时间窗口的增量同步。
5. 接入定时调度系统。
6. 学会看日志中的速度、失败数、脏数据。
7. 再扩展到 HDFS、Hive、Oracle、PostgreSQL 等数据源。

---

## 15. 参考资料

- DataX GitHub 仓库：<https://github.com/alibaba/DataX>
- DataX 官方使用说明：<https://github.com/alibaba/DataX/blob/master/userGuid.md>
- MySQLReader 官方文档：<https://github.com/alibaba/DataX/blob/master/mysqlreader/doc/mysqlreader.md>
- MySQLWriter 官方文档：<https://github.com/alibaba/DataX/blob/master/mysqlwriter/doc/mysqlwriter.md>
- TxtFileReader 官方文档：<https://github.com/alibaba/DataX/blob/master/txtfilereader/doc/txtfilereader.md>
- TxtFileWriter 官方文档：<https://github.com/alibaba/DataX/blob/master/txtfilewriter/doc/txtfilewriter.md>
